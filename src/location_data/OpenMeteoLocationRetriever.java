package location_data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.util.Scanner;

import static utils.HttpURLConnector.fetchAPIResponse;

public class OpenMeteoLocationRetriever implements ILocationRetriever {
  public OpenMeteoLocationRetriever() {
  }

  /**
   * Gets the data for a specified location
   *
   * @param location - The city to gather information on
   * @return JSONObject - The data on the city
   */
  @Override
  public JSONObject getLocationData(String location) {
    JSONObject data = new JSONObject();

    location = location.replaceAll(" ", "+");

    String url = "https://geocoding-api.open-meteo.com/v1/search?name=" + location + "&count=1";

    try {
      HttpURLConnection connection = fetchAPIResponse(url);

      final int SUCCESSFUL_RESPONSE_CODE = 200;
      if (SUCCESSFUL_RESPONSE_CODE != connection.getResponseCode()) {
        return null;
      }

      StringBuilder apiResponse = new StringBuilder();
      Scanner scanner = new Scanner(connection.getInputStream());

      while (scanner.hasNextLine()) {
        apiResponse.append(scanner.nextLine());
      }

      scanner.close();
      connection.disconnect();

      JSONParser parser = new JSONParser();
      JSONObject responseJSONObj = (JSONObject) parser.parse(apiResponse.toString());

      JSONArray responseArray = (JSONArray) responseJSONObj.get("results");

      data = (JSONObject) responseArray.getFirst();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return data;
  }
}
