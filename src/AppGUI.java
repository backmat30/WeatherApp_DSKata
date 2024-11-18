import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import weather_data.IWeatherRetriever;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.image.BufferedImage;

/**
 * Class for the basic weather app GUI
 */
public class AppGUI extends JFrame {
  private IWeatherRetriever m_weatherRetriever;

  private JSONObject currentWeather;

  public AppGUI(IWeatherRetriever weatherRetriever) {
    super("Weather App");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(600, 600);
    setLocationRelativeTo(null);
    setLayout(null);
    setResizable(false);

    addGuiComponents();

    m_weatherRetriever = weatherRetriever;
  }

  /**
   * Adds the components of the GUI to the frame
   */
  private void addGuiComponents() {
    // Search bar
    JTextField locationSearchField = new JTextField();
    locationSearchField.setBounds(50, 15, 300, 45);
    locationSearchField.setFont(new Font("DEFAULT", Font.PLAIN, 30));

    add(locationSearchField);

    // Location label
    JLabel locationLabel = new JLabel("Weather for: ");
    locationLabel.setBounds(50, 70, 500, 45);
    locationLabel.setFont(new Font("DEFAULT", Font.PLAIN, 30));
    locationLabel.setHorizontalAlignment(SwingConstants.CENTER);

    add(locationLabel);

    // Weather effects image label
    JLabel weatherImageLabel = new JLabel(resizeImage(loadImageFromURL("https://openweathermap.org/img/wn/10d@2x.png"), 250, 250));
    weatherImageLabel.setBounds(175, 120, 250, 250);
    weatherImageLabel.setHorizontalAlignment(SwingConstants.CENTER);

    add(weatherImageLabel);

    // Weather effects label
    JLabel weatherLabel = new JLabel("N/A");
    weatherLabel.setBounds(50, 330, 500, 45);
    weatherLabel.setFont(new Font("DEFAULT", Font.PLAIN, 20));
    weatherLabel.setHorizontalAlignment(SwingConstants.CENTER);

    add(weatherLabel);

    // Temperature label
    JLabel temperatureLabel = new JLabel("-°F");
    temperatureLabel.setBounds(50, 385, 250, 45);
    temperatureLabel.setFont(new Font("DEFAULT", Font.PLAIN, 20));
    temperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);

    add(temperatureLabel);

    // Feels like temperature label
    JLabel feelsLikeTemperatureLabel = new JLabel("Feels like -°F");
    feelsLikeTemperatureLabel.setBounds(300, 385, 250, 45);
    feelsLikeTemperatureLabel.setFont(new Font("DEFAULT", Font.PLAIN, 20));
    feelsLikeTemperatureLabel.setHorizontalAlignment(SwingConstants.CENTER);

    add(feelsLikeTemperatureLabel);

    // Humidity label
    JLabel humidityLabel = new JLabel("Humidity: -%");
    humidityLabel.setBounds(50, 440, 250, 45);
    humidityLabel.setFont(new Font("DEFAULT", Font.PLAIN, 20));
    humidityLabel.setHorizontalAlignment(SwingConstants.CENTER);

    add(humidityLabel);

    // Wind speed label
    JLabel windSpeedLabel = new JLabel("Wind speed: -mph");
    windSpeedLabel.setBounds(300, 440, 250, 45);
    windSpeedLabel.setFont(new Font("DEFAULT", Font.PLAIN, 20));
    windSpeedLabel.setHorizontalAlignment(SwingConstants.CENTER);
    add(windSpeedLabel);

    // Search button
    JButton searchButton = new JButton("Search");
    searchButton.setBounds(360, 15, 120, 45);
    searchButton.setFont(new Font("DEFAULT", Font.PLAIN, 24));
    searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String locationInput = locationSearchField.getText();
        String locationOutput = locationInput;

        if(locationInput.replaceAll("\\s", "").isEmpty()) {
          return;
        }

        currentWeather = m_weatherRetriever.getWeatherData(locationInput);

        JSONArray weatherConditions = (JSONArray) currentWeather.get("weather");
        JSONObject currentConditions = (JSONObject) weatherConditions.getFirst();
        String iconURL = "https://openweathermap.org/img/wn/" + (String) currentConditions.get("icon") + "@2x.png";
        weatherImageLabel.setIcon(resizeImage(loadImageFromURL(iconURL), 250, 250));

        String currentConditionString = (String) currentConditions.get("description");
        weatherLabel.setText(currentConditionString);

        String currentTemp = (String) currentWeather.get("temp");
        temperatureLabel.setText(currentTemp + "°F");

        String currentFeelsLike = (String) currentWeather.get("feels_like");
        feelsLikeTemperatureLabel.setText(currentFeelsLike + "°F");

        String currentHumidity = (String) currentWeather.get("humidity");
        humidityLabel.setText(currentHumidity + "%");

        String currentWindSpeed = (String) currentWeather.get("wind_speed");
        windSpeedLabel.setText(currentWindSpeed + " mph");
      }
    });

    add(searchButton);
  }

  /**
   * Takes a string representing the url of an online image and makes an ImageIcon from it
   *
   * @param url The url of the image
   * @return ImageIcon - A new ImageIcon using the image found
   */
  private ImageIcon loadImageFromURL(String url) {
    URI uri = null;
    URL imageURL = null;
    BufferedImage image = null;
    try {
      uri = new URI(url);
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return null;
    }
    try {
      imageURL = uri.toURL();
    } catch (MalformedURLException e) {
      e.printStackTrace();
      return null;
    }
    try {
      image = ImageIO.read(imageURL);
    } catch (java.io.IOException e) {
      e.printStackTrace();
      return null;
    }

    return new ImageIcon(image);
  }

  /**
   * Takes an image icon and resizes it
   *
   * @param image  The image being resized
   * @param width  The new width of the image
   * @param height The new height of the image
   * @return ImageIcon - A new ImageIcon at the desired size
   */
  private ImageIcon resizeImage(ImageIcon image, int width, int height) {
    Image resizedImage = null;
    if (image != null) {
      resizedImage = image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
    return new ImageIcon(resizedImage);
  }
}
