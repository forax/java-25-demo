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
  try(var scope = StructuredTaskScope.open(
      StructuredTaskScope.Joiner.<WeatherResponse>allSuccessfulOrThrow(),
            config -> config.withTimeout(Duration.ofMillis(2_000)))) {
    var callables = latlongs.stream()
        .map(this::task)
        .toList();
    callables.forEach(scope::fork);

    var results = scope.join().toList();

    for(var subtask : results) {
      var response = subtask.get();
      IO.println(response);
    }
  }
}
