import location_data.ILocationRetriever;
import location_data.OpenMeteoLocationRetriever;
import weather_data.IWeatherRetriever;
import weather_data.OWMWeatherRetriever;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        ILocationRetriever locationRetriever = new OpenMeteoLocationRetriever();
        IWeatherRetriever weatherRetriever = new OWMWeatherRetriever(locationRetriever);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AppGUI(weatherRetriever).setVisible(true);
            }
        });
    }
}
