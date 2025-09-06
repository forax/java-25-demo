import openmeteo.OpenMeteo;
import openmeteo.OpenMeteo.LatLong;
import openmeteo.OpenMeteo.WeatherResponse;

final class StreamJoiner<T> implements StructuredTaskScope.Joiner<T, Void> {
  private int counter;
  private volatile boolean done;
  private final LinkedBlockingDeque<StructuredTaskScope.Subtask<T>> queue = new LinkedBlockingDeque<>();

  @Override
  public boolean onFork(StructuredTaskScope.Subtask<? extends T> subtask) {
    StructuredTaskScope.Joiner.super.onFork(subtask);
    counter++;
    return false;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean onComplete(StructuredTaskScope.Subtask<? extends T> subtask) {
    StructuredTaskScope.Joiner.super.onComplete(subtask);
    if (done) {
      return true;
    }
    try {
      queue.put((StructuredTaskScope.Subtask<T>) subtask);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return false;
  }

  @Override
  public Void result() {
    return null;
  }

  public <R> R compute(Function<? super Stream<T>, ? extends R> function) throws InterruptedException {
    var spliterator = new Spliterator<T>() {
      private int remaining = counter;

      @Override
      public boolean tryAdvance(Consumer<? super T> action) {
        if (remaining == 0) {
          return false;
        }
        StructuredTaskScope.Subtask<T> subtask;
        try {
          subtask = queue.take();
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
        if (subtask.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
          action.accept(subtask.get());
        }
        remaining--;
        return true;
      }

      @Override
      public Spliterator<T> trySplit() {
        return null;
      }

      @Override
      public long estimateSize() {
        return remaining;
      }

      @Override
      public int characteristics() {
        return 0;
      }
    };
    var stream = StreamSupport.stream(spliterator, false);
    var runnable = new Runnable() {
      private R result;

      @Override
      public void run() {
        result = function.apply(stream);
      }
    };
    Thread.ofVirtual().start(runnable).join();
    done = true;
    return runnable.result;
  }
}

Callable<WeatherResponse> task(LatLong latLong) {
  return () -> OpenMeteo.getWeatherResponse(latLong);
}

void main() throws InterruptedException {
  var paris = new LatLong(48.864716, 2.349014);  // use 30_000
  var nantes = new LatLong(47.2181, -1.5528);
  var marseille = new LatLong(43.2964, 5.37);

  var latlongs = List.of(paris, nantes, marseille);
  var joiner = new StreamJoiner<WeatherResponse>();
  try(var scope = StructuredTaskScope.open(joiner)) {
    var callables = latlongs.stream()
        .map(this::task)
        .toList();
    callables.forEach(scope::fork);

    var response = joiner.compute(Stream::toList);
    //var response = joiner.compute(s -> s.findFirst().orElseThrow());
    scope.join();

    IO.println(response);
  }
}
