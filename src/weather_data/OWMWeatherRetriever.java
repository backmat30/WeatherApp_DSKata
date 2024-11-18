package weather_data;

import location_data.ILocationRetriever;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import utils.HttpURLConnector;

import java.net.HttpURLConnection;
import java.util.Scanner;

public class OWMWeatherRetriever implements IWeatherRetriever {
  private final String OWM_API_KEY = System.getenv("OpenWeatherAPIKey");

  private ILocationRetriever m_locationRetriever;

  /**
   * Creates a new Weather Retriever using Open Weather Map
   */
  public OWMWeatherRetriever(ILocationRetriever locationRetriever) {
    m_locationRetriever = locationRetriever;
  }

  /**
   * Creates a JSONObject holding the desired data from the API
   *
   * @param locationName The location to use in the API call
   * @return JSONObject - The set of data needed by the user
   */
  @Override
  public JSONObject getWeatherData(String locationName) {
    JSONObject weatherData = null;

    JSONObject locationData = m_locationRetriever.getLocationData(locationName);

    double latitude = (double) locationData.get("latitude");
    double longitude = (double) locationData.get("longitude");

    long currentTime = System.currentTimeMillis() / 1000;
    String units = "imperial";

    String url = "https://api.openweathermap.org/data/3.0/onecall/timemachine?lat=" + latitude +
            "&lon=" + longitude +
            "&dt=" + currentTime +
            "&units=" + units +
            "&appid=" + OWM_API_KEY;

    try {
      HttpURLConnection connection = HttpURLConnector.fetchAPIResponse(url);

      final int SUCCESSFUL_RESPONSE_CODE = 200;
      if (SUCCESSFUL_RESPONSE_CODE != connection.getResponseCode()) {
        return null;
      }

      StringBuilder response = new StringBuilder();
      Scanner scanner = new Scanner(connection.getInputStream());

      while (scanner.hasNextLine()) {
        response.append(scanner.nextLine());
      }
      scanner.close();
      connection.disconnect();

      JSONParser parser = new JSONParser();
      JSONObject responseJSONObj = (JSONObject) parser.parse(response.toString());

      JSONArray data = (JSONArray) responseJSONObj.get("data");
      JSONObject currentWeather = (JSONObject) data.getFirst();
      String weatherIcon = (String) currentWeather.get("icon");

      currentWeather.put("icon", "https://openweathermap.org/img/wn/" + weatherIcon + "@2x.png");

      weatherData = currentWeather;

    } catch (Exception e) {
      e.printStackTrace();
    }
    return weatherData;
  }
}
