package openmeteo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import module java.base;
import module java.net.http;
//import module com.fasterxml.jackson.databind;

public final class OpenMeteo {
  /**
   * Open Meteo API response structure.
   * see <a href="https://api.open-meteo.com/v1/forecast?latitude=48.864716&longitude=2.349014&current=temperature_2m">open meteo api</a>
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record WeatherResponse(
      double latitude,
      double longitude,
      CurrentWeather current) {
  }

  /**
   * Current weather data from the API response.
   */
  @JsonIgnoreProperties(ignoreUnknown = true)
  public record CurrentWeather(@JsonProperty("temperature_2m") double temperature) {
  }

  static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public record LatLong(double latitude, double longitude) {
  }

  public static WeatherResponse getWeatherResponse(LatLong latLong)
      throws IOException, InterruptedException {

    var uri = URI.create("""
        https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current=temperature_2m\
        """.formatted("" + latLong.latitude, "" + latLong.longitude));
    IO.println("Requesting weather data from " + uri);

    String body;
    try {
      body = Fetch.fetch(uri);                         // try a direct call
    } catch (ConnectException _) {
      body = Fetch.cache(uri, Fetch::fetch);   // uses the cache
    }

    return OBJECT_MAPPER.readValue(body, WeatherResponse.class);
  }

  static void main() throws IOException, InterruptedException {
    var paris = new LatLong(48.864716, 2.349014);

    var weather = getWeatherResponse(paris);

    IO.println("Lat,Long " + weather.latitude + ", " + weather.longitude);
    IO.println("Temperature " + weather.current().temperature() + "°C");
  }
}
