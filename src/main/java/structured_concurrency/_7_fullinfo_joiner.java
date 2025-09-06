import openmeteo.OpenMeteo;
import openmeteo.OpenMeteo.LatLong;
import openmeteo.OpenMeteo.WeatherResponse;

final class FullInfoJoiner<T, R> implements StructuredTaskScope.Joiner<T, List<T>> {
  private final Object lock = new Object();
  private final ArrayList<T> list = new ArrayList<>();
  private Throwable throwable;

  @Override
  public boolean onComplete(StructuredTaskScope.Subtask<? extends T> subtask) {
    //StructuredTaskScope.Joiner.super.onComplete(subtask);
    synchronized (lock) {
      switch (subtask.state()) {
        case FAILED -> {
          if (throwable == null) {
            this.throwable = new Throwable("Subtask failed");
          }
          throwable.addSuppressed(subtask.exception());
        }
        case SUCCESS -> list.add(subtask.get());
        case UNAVAILABLE -> throw new IllegalArgumentException();
      }
    }
    return false;
  }

  @Override
  public List<T> result() throws Throwable {
    synchronized (lock) {
      if (throwable != null) {
        throw throwable;
      }
      return List.copyOf(list);
    }
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
  var joiner = new FullInfoJoiner<WeatherResponse, List<WeatherResponse>>();
  try(var scope = StructuredTaskScope.open(joiner)) {
    var callables = latlongs.stream()
        .map(this::task)
        .toList();
    callables.forEach(scope::fork);

    var completes = scope.join();

    for(var complete : completes) {
      IO.println(complete);
    }
  }
}
