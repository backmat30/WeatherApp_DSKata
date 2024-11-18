package utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnector {
  /**
   * Fetches a response from an api using the given url
   * @param urlString The URL of the api as a String
   * @return  HttpURLConnection - A connection to the api
   */
  public static HttpURLConnection fetchAPIResponse(String urlString) {
    HttpURLConnection connection = null;
    try {
      URL url = new URL(urlString);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      connection.connect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return connection;
  }
}
