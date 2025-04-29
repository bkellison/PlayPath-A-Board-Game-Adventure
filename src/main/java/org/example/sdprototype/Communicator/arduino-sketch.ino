#include <WiFiS3.h>
#include <Adafruit_NeoPixel.h>
#include <WiFi.h>

#define LED_PIN 6       // Pin connected to the LED strip
#define NUM_LEDS 1      // Only controlling the first LED (temp)

char ssid[] = "MarissaiPhone";     // WiFi SSID (WiFi name)
char pass[] = "MolKinDaxWilLucBlu$&2016"; // WiFi password

WiFiServer server(80);
WiFiClient client;
int status = WL_IDLE_STATUS;

Adafruit_NeoPixel strip(NUM_LEDS, LED_PIN, NEO_GRB + NEO_KHZ800);

void setup() {
  Serial.begin(9600);

  // Initialize LED strip
  strip.begin();
  strip.show(); // Initialize all pixels to 'off'
  strip.setPixelColor(0, strip.Color(0, 0, 255)); // Blue
  strip.show();

  // Connect to WiFi
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to SSID: ");
    Serial.println(ssid);
    status = WiFi.begin(ssid, pass);
    delay(10000);

    // Debugging wifi connection
    Serial.print("WiFi status code: ");
    Serial.println(WiFi.status());
  }

  Serial.println("Connected to WiFi");
  server.begin();
  Serial.print("IP Address: ");
  Serial.println(WiFi.localIP());
}

void loop() {
  client = server.available();

  if (client) {
    Serial.println("Client connected");
    String request = client.readStringUntil('\r');
    Serial.println("Request: " + request);

    // Call helper functions depending on type of request received
    String responseMessage = "Unrecognized request";

    if (request.indexOf("GET /setTargets") >= 0) {
      responseMessage = handleTargetRequest(request);
    }
    else if (request.indexOf("GET /setMode") >= 0) {
      responseMessage = handleModeRequest(request);
    }
    else if (request.indexOf("GET /connect") >= 0) {
      strip.setPixelColor(0, strip.Color(255, 0, 0)); // Red
      strip.show();
      responseMessage = "Connected successfully";
    }

    // Send basic HTTP response
    client.println("HTTP/1.1 200 OK");
    client.println("Content-Type: text/plain");
    client.println("Connection: close");
    client.println();
    client.println(responseMessage);
    client.stop();
  }
}

// Helper function to handle receiving the game mode selected from the JFX application
String handleModeRequest(String request) {
  // Define initial mode to be extracted from URL query parameter
  int mode = -1;

  // Look for parameter in request
  int modePos = request.indexOf("mode=");

  if (modePos >= 0) {
    // Extract query parameter value
    mode = request.substring(modePos + 5).toInt();
    Serial.print("Game Mode: ");
    Serial.println(mode);

    if (mode == 1) {
      strip.setPixelColor(0, strip.Color(0, 255, 0)); // Green
    } else if (mode == 2) {
      strip.setPixelColor(0, strip.Color(255, 165, 0)); // Orange
    } else if (mode == 3) {
      strip.setPixelColor(0, strip.Color(255, 255, 0)); // Yellow
    } else {
      return "Invalid mode value";
    }
    strip.show();
    return "Mode set to " + String(mode);
  }
  return "Invalid mode request";
}

// Helper function to handle receiving the initial and final target indices of board squares
String handleTargetRequest(String request) {
  // Define initial and final target index to be extracted from URL query parameters
    int initialIndex = -1;
    int finalIndex = -1;

    // Look for parameters in the request
    int initialPos = request.indexOf("initial=");
    int finalPos = request.indexOf("final=");

    String color = "";

    if (initialPos >=0 && finalPos >=0) {
      // Extract query parameter values
      initialIndex = request.substring(initialPos + 8, request.indexOf('&', initialPos)).toInt();
      finalIndex = request.substring(finalPos + 6).toInt();

      // Debugging in Serial Monitor terminal:
      Serial.print("Received initial index: ");
      Serial.println(initialIndex);
      Serial.print("Received final index: ");
      Serial.println(finalIndex);

      // Compare received indices and change lights accordingly
      if (initialIndex == finalIndex) {
        strip.setPixelColor(0, strip.Color(128, 0, 128));  // Purple
        strip.show();
        return "Set to Purple, target matched";
      }
      else {
        strip.setPixelColor(0, strip.Color(0, 255, 0));   // Green
        strip.show();
        return "Set to Green, target mismatched";
      }

      // If neither of the above statements execute, request is invalid
      return "Invalid target request";
    }
}
