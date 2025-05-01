#include <WiFiS3.h>
#include <Adafruit_NeoPixel.h>
#include <WiFi.h>

#define LED_PIN 6       // Pin connected to the LED strip
#define NUM_LEDS 439

char ssid[] = "MarissaiPhone";     // WiFi SSID (WiFi name)
char pass[] = "MolKinDaxWilLucBlu$&2016"; // WiFi password

WiFiServer server(80);
WiFiClient client;
int status = WL_IDLE_STATUS;

Adafruit_NeoPixel strip(NUM_LEDS, LED_PIN, NEO_GRB + NEO_KHZ800);

/*
 * LED Light Indices:
 */

/*
 * Mode 2: Rainforest rumble
 */
int mode2Space0[] = {436, 435, 364};
int mode2Space1[] = {431, 377, 378};
int mode2Space2[] = {278, 279, 282};
int mode2Space3[] = {207, 211};
int mode2Space4[] = {82, 140};
int mode2Space5[] = {156, 157, 86};
int mode2Space6[] = {127, 128, 90};
int mode2Space7[] = {202, 203, 198, 199};
int mode2Space8[] = {274, 351};
int mode2Space9[] = {347, 236};
int mode2Space10[] = {342, 341, 261, 262};
int mode2Space11[] = {189, 190, 194};
int mode2Space12[] = {99, 123};
int mode2Space13[] = {173, 174, 103, 104};
int mode2Space14[] = {177, 178, 110, 111};
int mode2Space15[] = {45, 41, 40};

int mode2Lengths[] = {3, 3, 4, 2, 2, 3, 3, 4, 2, 2, 4, 3, 2, 4, 4, 2};

int* mode2Path[] = {mode2Space0, mode2Space1, mode2Space2, mode2Space3, mode2Space4, mode2Space5,
mode2Space6, mode2Space7, mode2Space8, mode2Space9, mode2Space10, mode2Space11, mode2Space12,
mode2Space13, mode2Space14, mode2Space15};

void setup() {
  Serial.begin(9600);

  // Initialize LED strip
  strip.begin();
  strip.show(); // Initialize all pixels to 'off'

  // Show a static rainbow
  rainbow();

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

  strip.clear();
  strip.show();

  // TESTING LIGHTS
  //strip.setPixelColor(50, strip.Color(0, 0, 255));
  //strip.setPixelColor(100, strip.Color(0, 0, 255));
  //strip.setPixelColor(150, strip.Color(0, 0, 255));
  //strip.setPixelColor(200, strip.Color(0, 0, 255));
  //strip.setPixelColor(250, strip.Color(0, 0, 255));
  //strip.setPixelColor(300, strip.Color(0, 0, 255));
  //strip.setPixelColor(350, strip.Color(0, 0, 255));
  //strip.setPixelColor(400, strip.Color(0, 0, 255));

  strip.show();
}

// Maps a value from 0–255 to a color
uint32_t Wheel(byte WheelPos) {
  WheelPos = 255 - WheelPos;
  if (WheelPos < 85) {
    return strip.Color(255 - WheelPos * 3, 0, WheelPos * 3);
  } else if (WheelPos < 170) {
    WheelPos -= 85;
    return strip.Color(0, WheelPos * 3, 255 - WheelPos * 3);
  } else {
    WheelPos -= 170;
    return strip.Color(WheelPos * 3, 255 - WheelPos * 3, 0);
  }
}

// Displays a rainbow across the LED strip
void rainbow() {
  for (int i = 0; i < NUM_LEDS; i++) {
    int colorIndex = (i * 256 / NUM_LEDS);  // Spread the rainbow over the strip
    strip.setPixelColor(i, Wheel(colorIndex & 255));
  }
  strip.show();
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

    Serial.println(responseMessage);

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
      // Clear last space
      strip.clear();
      strip.show();

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
        // Get indices of lights to light up the space
        int* lights = mode2Path[finalIndex];
        int numLights = mode2Lengths[finalIndex];

        if (finalIndex > 15) {
          return "Won";
        }

        for (int i = 0; i < numLights; i++) {
          strip.setPixelColor(lights[i], strip.Color(255, 0, 0));
        }

        strip.show();
        return "Target matched";
      }
      else {
        // Get indices of lights to light up the space
        int* lights = mode2Path[finalIndex];
        int numLights = mode2Lengths[finalIndex];

        if (finalIndex > 15) {
          return "Won";
        }

        for (int i = 0; i < numLights; i++) {
          strip.setPixelColor(lights[i], strip.Color(255, 0, 0));
        }

        strip.show();
        return "Set to Green, target mismatched";
      }

      // If neither of the above statements execute, request is invalid
      return "Invalid target request";
    }
}
