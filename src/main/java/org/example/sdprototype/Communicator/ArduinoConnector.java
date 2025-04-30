package org.example.sdprototype.Communicator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ArduinoConnector {

    private static final String ARDUINO_IP = "http://172.20.10.6";

    // Flag to enable/disable Arduino communication
    private static final boolean ARDUINO_ENABLED = false; // Set to false to disable Arduino communication

    // Function to send HTTP request to arduino: called within helper functions to send specific requests
    private static void sendRequest(String urlString) {
        // Skip if Arduino is disabled
        if (!ARDUINO_ENABLED) {
            System.out.println("Arduino communication disabled. Would have sent: " + urlString);
            return;
        }

        try {
            // Form the URL from the url string, and attempt to open an HTTP connection, setting the method to "GET"
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Add short timeout to prevent long waits
            connection.setConnectTimeout(2000); // 2 second timeout

            // Get response code to ensure connection successful
            System.out.println("Sending request to " + urlString);
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            // Create buffered reader for input stream
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            System.out.println("Response from Arduino:");
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            // Close connection when received reponse and done reading
            in.close();
            connection.disconnect();
        }
        catch (Exception e) {
            System.out.println("Error sending request to Arduino: Arduino might not be connected");
            // Don't print stack trace to avoid cluttering console
            // e.printStackTrace();
        }
    }

    // Helper function to send the selected game mode to the arduino
    public static void sendGameMode(int mode) {
        // Form url string with game mode as a query parameter in the HTTP request
        String url = ARDUINO_IP + "/setMode?mode=" + mode;
        sendRequest(url);
    }

    // Helper function to send the initial and final target indices for board spaces to the arduino
    public static void sendTargetIndices(int initial, int fin) {
        String url = ARDUINO_IP + "/setTargets?initial=" + initial + "&final=" + fin;
        sendRequest(url);
    }

    // MAIN METHOD FOR TESTING LED BEHAVIOR
    public static void main(String[] args) {
        // Skip if Arduino is disabled
        if (!ARDUINO_ENABLED) {
            System.out.println("Arduino communication disabled. Testing cannot proceed.");
            return;
        }

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