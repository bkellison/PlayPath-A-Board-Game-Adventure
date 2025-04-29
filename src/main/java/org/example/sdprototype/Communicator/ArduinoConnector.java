package org.example.sdprototype.Communicator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ArduinoConnector {

    private static final String ARDUINO_IP = "http://172.20.10.6";
    private static final int PORT = 80;

    public static void main(String[] args) {
        try {
            URL url = new URL(ARDUINO_IP + "/connect");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            System.out.println("Attempting connection to " + url.toString());

            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String inputLine;

            System.out.println("Response from Arduino:");
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }

            in.close();
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
