import openmeteo.OpenMeteo;
import openmeteo.OpenMeteo.LatLong;
import openmeteo.OpenMeteo.WeatherResponse;

Callable<WeatherResponse> task(LatLong latLong) {
  return () -> OpenMeteo.getWeatherResponse(latLong);
}

void main() throws InterruptedException {
  var paris = new LatLong(48.864716, 2.349014);  // use 30_000
  var nantes = new LatLong(47.2181, -1.5528);
  var marseille = new LatLong(43.2964, 5.37);

  var latlongs = List.of(paris, nantes, marseille);
  try(var scope = StructuredTaskScope.<WeatherResponse>open()) {
    var callables = latlongs.stream()
        .map(this::task)
        .toList();
    var subtasks = callables.stream()
        .map(scope::fork)
        .toList();

    scope.join();

    for(var subtask : subtasks) {
      var response = subtask.get();
      IO.println(response);
    }
  }
}
