import openmeteo.OpenMeteo;

import java.util.concurrent.StructuredTaskScope.Joiner;
import java.util.concurrent.StructuredTaskScope.Subtask;

import static java.util.concurrent.StructuredTaskScope.Subtask.State.SUCCESS;
import static java.util.concurrent.StructuredTaskScope.Subtask.State.UNAVAILABLE;

<T> Joiner<T, List<Subtask<T>>> all(Joiner<T, ?> joiner) {
  return new Joiner<>() {
    private final ArrayList<Subtask<T>> subtasks = new ArrayList<>();

    @Override
    public boolean onFork(Subtask<? extends T> subtask) {
      @SuppressWarnings("unchecked")
      var task = (Subtask<T>) subtask;
      subtasks.add(task);
      return joiner.onFork(subtask);
    }

    @Override
    public boolean onComplete(Subtask<? extends T> subtask) {
      return joiner.onComplete(subtask);
    }

    @Override
    public List<Subtask<T>> result() throws Throwable {
      joiner.result();  // can throw
      return Collections.unmodifiableList(subtasks);
    }
  };
}

<T> Joiner<T, List<Subtask<T>>> filtered(Joiner<T, ?> joiner, Predicate<? super Subtask<T>> filter) {
  return new Joiner<>() {
    private final ArrayList<Subtask<T>> subtasks = new ArrayList<>();

    @Override
    public boolean onFork(Subtask<? extends T> subtask) {
      @SuppressWarnings("unchecked")
      var task = (Subtask<T>) subtask;
      subtasks.add(task);
      return joiner.onFork(subtask);
    }

    @Override
    public boolean onComplete(Subtask<? extends T> subtask) {
      return joiner.onComplete(subtask);
    }

    @Override
    public List<Subtask<T>> result() throws Throwable {
      joiner.result();  // can throw
      return subtasks.stream().filter(filter).toList();
    }
  };
}

<T> Joiner<T, List<Subtask<T>>> completed(Joiner<T, ?> joiner) {
  return filtered(joiner, subtask -> subtask.state() != UNAVAILABLE);
}

<T> Joiner<T, List<Subtask<T>>> succeed(Joiner<T, ?> joiner) {
  return filtered(joiner, subtask -> subtask.state() == SUCCESS);
}



void main() throws InterruptedException {
  var paris = new OpenMeteo.LatLong(48.864716, 2.349014);  // use 30_000
  var nantes = new OpenMeteo.LatLong(47.2181, -1.5528);
  var marseille = new OpenMeteo.LatLong(43.2964, 5.37);

  var latlongs = List.of(paris, nantes, marseille);
  try(var scope = StructuredTaskScope.open(
      //all(Joiner.anySuccessfulResultOrThrow()))) {
      completed(Joiner.anySuccessfulResultOrThrow()))) {
      //succeed(Joiner.anySuccessfulResultOrThrow()))) {

    scope.fork(() -> { throw null; });
    scope.fork(() -> {
      Thread.sleep(3_000);
      return null;
    });

    for(var latLong : latlongs) {
      scope.fork(() -> OpenMeteo.getWeatherResponse(latLong));
    }

    var subtasks = scope.join();
    IO.println(subtasks);
  }
}
