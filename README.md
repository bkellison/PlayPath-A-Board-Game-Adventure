# PlayPath: A Board Game Adventure

PlayPath is an interactive physical-digital board game that combines traditional board game play with digital components and LED lighting. The game offers three different themed adventures, each with its own unique storyline, game board layout, and special events.

## Accessibility Mission
PlayPath was specifically designed for a child with Canavan disease, a condition that affects gross motor skills. Traditional video games often require fine motor control that can be challenging or impossible for children with certain disabilities.
This hybrid physical-digital game creates an accessible gaming experience that allows the child to play with family members through the physical LED board interface, while the digital component handles game logic. The large, bright LED lights provide clear visual feedback that's easy to track, and the simplified interaction model makes it possible for everyone to participate regardless of physical abilities.
By merging technology with traditional board game concepts, PlayPath creates an inclusive gaming environment where the focus is on shared fun rather than physical limitations.

## Overview

PlayPath is a Java and Arduino-based game with a physical LED-enabled game board and a digital interface. Players roll virtual dice using the digital interface, and the game tracks their progress on both the digital display and physical LED board. Special events occur when landing on certain spaces, adding more excitement to the gameplay.

## Game Themes

PlayPath offers three unique gaming experiences:

### 1. Shrek's Swamp Trek

### 2. Rainforest Rumble

### 3. Pirate's Gold Rush


## User Interface

The game features a user-friendly JavaFX interface with:
- Welcome screen
- Track selection screen (choose your adventure)
- Game board with digital representation of the physical board
- Dice rolling mechanism
- Player turn information
- Special event messages
- Win screen

## How It Works

1. **Game Start**: Users launch the Java application which displays the welcome screen.
2. **Track Selection**: Players select one of three themed game tracks.
3. **Board Setup**: The selected track is highlighted on the digital interface and the corresponding LED pattern lights up on the physical board.
4. **Gameplay**:
   - Player rolls the virtual dice using the interface
   - The game moves the player token on both the digital and physical boards
   - Special spaces trigger unique game events (move forward/backward)
   - First player to reach the end wins!

## Technical Architecture

### Software Components

- **Java Application (JavaFX)**
  - `MainApplication.java`: Entry point for the application
  - Game controllers manage gameplay logic
  - Board representation handles visual display
  - Sound effects enhance gameplay experience

- **Arduino Program**
  - Controls the LED strip on the physical board
  - Communicates with Java application via WiFi
  - Displays player position and win animations

### Hardware Components

- **Arduino (UNO WiFi Rev2 or similar)**
- **LED Strip** (WS2812B RGB individually addressable LEDs)
- **Power Supply** (5V)
- **Circuit Components**:
  - 1000μF capacitor (suppresses power spikes to protect LEDs)
  - 330 ohm resistor (protects the data line)

### Communication Protocol

The Java application communicates with the Arduino over WiFi using simple HTTP requests. The Arduino hosts a web server that accepts the following commands:
- `/setMode?mode=X`: Sets the game mode/theme (1, 2, or 3)
- `/setTargets?initial=X&final=Y`: Updates player position
- `/startGame`: Initializes gameplay
- `/wonGame`: Triggers winning animation

## Game Features

- **Themed Visuals**: Each track has its own visual theme and color scheme
- **Special Events**: Land on special spaces for unexpected game events
- **Sound Effects**: Dice rolls, jumps, successes, and failures have audio feedback
- **LED Animations**: Visual feedback on the physical board
- **Win Celebration**: Special light animation when a player wins

## Security Note

The Arduino code contains WiFi credentials that should be changed before use. In a production environment, these would be handled more securely.

## Project Structure

- `/org/example/sdprototype/Application`: Main application code
- `/org/example/sdprototype/Controllers`: UI controllers
- `/org/example/sdprototype/GameLogic`: Core game mechanics
- `/org/example/sdprototype/GridBoard`: Board representation
- `/org/example/sdprototype/Communicator`: Arduino communication
- `/org/example/sdprototype/Utilities`: Helper classes
- `arduino-sketch.ino`: Arduino code for LED control

## Gameplay Screenshots

## Circuit Diagram

The physical board uses a simple circuit connecting an Arduino to an addressable LED strip with proper power management components:
- Arduino (data pin 6 connected to LED strip)
- 5V power supply
- 1000μF capacitor across power rails
- 330 ohm resistor on the data line

[This section would include a circuit diagram image]
