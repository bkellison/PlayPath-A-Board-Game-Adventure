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
 * Mode 2: Shrek's Swamp Trek
 */
int mode1Space0[] = {};

int mode1Lengths[] = {0};

int* mode1Path[] = {mode1Space0};

int mode1PathLength = 24;

int mode1Color[] = {0, 255, 0}; // green for shrek

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

int mode2Lengths[] = {3, 3, 3, 2, 2, 3, 3, 4, 2, 2, 4, 3, 2, 4, 4, 3};

int* mode2Path[] = {mode2Space0, mode2Space1, mode2Space2, mode2Space3, mode2Space4, mode2Space5,
mode2Space6, mode2Space7, mode2Space8, mode2Space9, mode2Space10, mode2Space11, mode2Space12,
mode2Space13, mode2Space14, mode2Space15};

int mode2PathLength = 16;

int mode2Color[] = {255, 0, 0}; // red for rainforest

/*
 * Mode 3: Pirate's Plunder
 */
int mode3Space0[] = {321, 322, 44, 45};
int mode3Space1[] = {324, 325, 114};
int mode3Space2[] = {327, 328, 249, 250};
int mode3Space3[] = {245, 173, 174};
int mode3Space4[] = {241, 193, 194};
int mode3Space5[] = {117, 118, 99, 100};
int mode3Space6[] = {165, 166, 95};
int mode3Space7[] = {161, 162, 91};
int mode3Space8[] = {157, 86, 87};
int mode3Space9[] = {139, 140, 82, 83};
int mode3Space10[] = {206, 210, 211};
int mode3Space11[] = {359, 282};
int mode3Space12[] = {228, 229, 355};
int mode3Space13[] = {232, 233, 269, 270};
int mode3Space14[] = {382, 383, 422};
int mode3Space15[] = {346, 347, 418};
int mode3Space16[] = {342, 413, 414};
int mode3Space17[] = {337, 338, 409};
int mode3Space18[] = {333, 334, 405};

int mode3Lengths[] = {4, 3, 4, 3, 3, 4, 3, 3, 3, 4, 3, 2, 3, 4, 3, 3, 3, 3, 3};

int* mode3Path[] = {mode3Space0, mode3Space1, mode3Space2, mode3Space3, mode3Space4, mode3Space5,
mode3Space6, mode3Space7, mode3Space8, mode3Space9, mode3Space10, mode3Space11, mode3Space12,
mode3Space13, mode3Space14, mode3Space15, mode3Space16, mode3Space17, mode3Space18};

int mode3PathLength = 19;

int mode3Color[] = {0, 0, 255}; // blue for pirates

// Variables to store current mode data
int** currentModePath = nullptr;
int* currentModeLengths = nullptr;
int* currentModeColor = nullptr;
int currentModePathLength = 0;

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
  //strip.setPixelColor(25, strip.Color(255, 0, 0));  //red
  //strip.setPixelColor(50, strip.Color(0, 255, 0));  //green
  //strip.setPixelColor(75, strip.Color(0, 0, 255));  //blue
  //strip.setPixelColor(100, strip.Color(128, 0, 128)); //purple
  //strip.setPixelColor(125, strip.Color(255, 0, 0));  //red
  //strip.setPixelColor(150, strip.Color(0, 255, 0));  //green
  //strip.setPixelColor(175, strip.Color(0, 0, 255));  //blue
  //strip.setPixelColor(200, strip.Color(128, 0, 128)); //purple
  //strip.setPixelColor(225, strip.Color(255, 0, 0));  //red
  //strip.setPixelColor(250, strip.Color(0, 255, 0));  //green
  //strip.setPixelColor(275, strip.Color(0, 0, 255));  //blue
  //strip.setPixelColor(300, strip.Color(128, 0, 128)); //purple
  //strip.setPixelColor(325, strip.Color(255, 0, 0));  //red
  //strip.setPixelColor(350, strip.Color(0, 255, 0));  //green
  //strip.setPixelColor(375, strip.Color(0, 0, 255));  //blue
  //strip.setPixelColor(400, strip.Color(128, 0, 128)); //purple
  //strip.setPixelColor(425, strip.Color(255, 0, 0));  //red
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
    else if (request.indexOf("GET /startGame") >= 0) {
      responseMessage = handleStartGameRequest();
    }
    else if (request.indexOf("GET /wonGame") >= 0) {
      responseMessage = handleWonGameRequest();
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
      currentModePath = mode1Path;
      currentModeLengths = mode1Lengths;
      currentModeColor = mode1Color;
      currentModePathLength = mode1PathLength;
      lightUpPath();
    } else if (mode == 2) {
      currentModePath = mode2Path;
      currentModeLengths = mode2Lengths;
      currentModeColor = mode2Color;
      currentModePathLength = mode2PathLength;
      lightUpPath();
    } else if (mode == 3) {
      currentModePath = mode3Path;
      currentModeLengths = mode3Lengths;
      currentModeColor = mode3Color;
      currentModePathLength = mode3PathLength;
      lightUpPath();
    } else {
      return "Invalid mode value";
    }
    return "Mode set to " + String(mode);
  }
  return "Invalid mode request";
}

String handleStartGameRequest() {
  // Clear all LEDs
  strip.clear();
  strip.show();

  int* space = currentModePath[0];
  int numLights = currentModeLengths[0];

  // Light up first space of current mode
  for (int i = 0; i < numLights; i++) {
    strip.setPixelColor(space[i], strip.Color(currentModeColor[0], currentModeColor[1], currentModeColor[2]));
  }

  strip.show();
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
        int* lights = currentModePath[finalIndex];
        int numLights = currentModeLengths[finalIndex];

        if (finalIndex > 15) {
          return "Won";
        }

        for (int i = 0; i < numLights; i++) {
          strip.setPixelColor(lights[i], strip.Color(currentModeColor[0], currentModeColor[1], currentModeColor[2]));
        }

        strip.show();
        return "Target matched";
      }
      else {
        // Get indices of lights to light up the space
        int* lights = currentModePath[finalIndex];
        int numLights = currentModeLengths[finalIndex];

        if (finalIndex > 15) {
          return "Won";
        }

        for (int i = 0; i < numLights; i++) {
          strip.setPixelColor(lights[i], strip.Color(currentModeColor[0], currentModeColor[1], currentModeColor[2]));
        }

        strip.show();
        return "Target mismatched";
      }

      // If neither of the above statements execute, request is invalid
      return "Invalid target request";
    }
}

// Helper function to light up path rainbow on win
String handleWonGameRequest() {
  // Clear previous lights
  strip.clear();
  strip.show();

  int totalLEDs = 0;
  for (int i = 0; i < currentModePathLength; i++) {
    totalLEDs += currentModeLengths[i];
  }

  int ledCount = 0;
  for (int i = 0; i < currentModePathLength; i++) {
    for (int j = 0; j < currentModeLengths[i]; j++) {
      int ledIndex = currentModePath[i][j];

      // Generate rainbow colors using a hue shift
      // Map ledCount to a color on the spectrum (simple rainbow gradient)
      float hue = (float)ledCount / totalLEDs; // Range 0.0 to 1.0
      uint32_t color = Wheel((int)(hue * 255));

      strip.setPixelColor(ledIndex, color);
      ledCount++;
    }
  }

  strip.show();
  return "Player won!";
}

// Helper function to light up the path of the chosen mode
void lightUpPath() {
  // Clear previous lights
  strip.clear();
  strip.show();

  for (int i = 0; i < currentModePathLength; i++) {
    for (int j = 0; j < currentModeLengths[i]; j++) {
      int ledIndex = currentModePath[i][j];
      strip.setPixelColor(ledIndex, strip.Color(currentModeColor[0], currentModeColor[1], currentModeColor[2]));
    }
  }

  strip.show();
}
