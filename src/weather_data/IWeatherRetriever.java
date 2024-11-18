package weather_data;

import org.json.simple.JSONObject;

public interface IWeatherRetriever {
  public JSONObject getWeatherData(String locationName);
}
