package location_data;

import org.json.simple.JSONObject;

public interface ILocationRetriever {
  /**
   * Gets the data for a specified location
   * @param location  - The city to gather information on
   * @return  JSONObject - The data on the city
   */
  public JSONObject getLocationData(String location);
}
