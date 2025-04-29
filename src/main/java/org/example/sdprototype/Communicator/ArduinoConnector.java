package org.example.sdprototype.Communicator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ArduinoConnector {

    private static final String ARDUINO_IP = "http://172.20.10.6";
    private static final int PORT = 80;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            while (true) {
                System.out.println("\nChoose an action:");
                System.out.println("1 - Send game mode");
                System.out.println("2 - Send initial and final target indices");
                System.out.println("Enter a negative number to quit.");
                System.out.print("Your choice: ");
                int choice = scanner.nextInt();

                if (choice < 0) {
                    System.out.println("Exiting...");
                    break;
                }

                String urlString = "";

                if (choice == 1) {
                    System.out.print("Enter game mode (1, 2, or 3): ");
                    int mode = scanner.nextInt();
                    urlString = ARDUINO_IP + "/setMode?mode=" + mode;
                } else if (choice == 2) {
                    System.out.print("Enter initial target index: ");
                    int initial = scanner.nextInt();
                    System.out.print("Enter final target index: ");
                    int fin = scanner.nextInt();
                    urlString = ARDUINO_IP + "/setTargets?initial=" + initial + "&final=" + fin;
                } else {
                    System.out.println("Invalid choice. Please enter 1, 2, or a negative number to exit.");
                    continue;
                }

                // Make the HTTP GET request
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                System.out.println("Sending request to: " + urlString);
                int responseCode = connection.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                System.out.println("Response from Arduino:");
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
                in.close();
                connection.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
