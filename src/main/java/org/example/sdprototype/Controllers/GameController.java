package org.example.sdprototype.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.sdprototype.GameLogic.Player;
import org.example.sdprototype.GridBoard.BoardGrid;
import org.example.sdprototype.GridBoard.BoardSpace;
import org.example.sdprototype.GridBoard.GameTrack;
import org.example.sdprototype.Communicator.ArduinoConnector;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GameController {
    private BoardGrid boardGrid;
    private GameTrack selectedTrack;
    private Player currentPlayer;
    private int currentTrackIndex = 0; // Track the current position in the track
    private Node playerToken;
    private Pane trackDisplayPane;
    private boolean animationInProgress = false;
    private Button rollDiceButton;
    private Text diceResultText;
    private int[] specialIdx;
    private int[] specialActions;
    private String[] messages;
    private String specialMessage = null;

    // Arrays that hold the indices of the special spaces for each theme
    private final int[] specialIdx1 = { 6, 8, 13, 20 };
    private final int[] specialIdx2 = { 1, 3, 6, 9, 13};
    private final int[] specialIdx3 = { 3, 6, 10, 13, 16 };

    // Arrays to hold results of special spaces
    private final int[] specialActions1 = { -1, 1, 3, -2 };
    private final int[] specialActions2 = {  1, 1, -1, 1, -1};
    private final int[] specialActions3 = { -2, 2, -1, -1, -1 };

    // Arrays to hold the special messages that will be displayed on the screen as a result of landing on special spaces
    private final String[] specialMessage1 = {
            "Oh no! Lord Farquaad sents his knights after you — move back one space.",
            "Not the gumdrop buttons! The Gingerbread Man lost a leg! — move back two spaces.",
            "Fiona shows you a shortcut — move up one space!",
            "Dragon swoops in and gives you a lift — fly forward three spaces!"
    };

    private final String[] specialMessage2 = {
            "Ribbit! A mischievous frog startles you — leap forward one space!",
            "Hisss! A sly snake slithers into your path — move forward one space!",
            "Squawk! A chatty parrot distracts you with gossip — move back one space!",
            "Caw! A nosy toucan pecks at your supplies — move forward one space!",
            "Ooh ooh aah aah! A playful monkey steals your hat — swing back one space to chase it!"
    };
    private final String[] specialMessage3 = {
            "Arrr! A fearsome pirate blocks your path and makes you move back two spaces!",
            "A friendly crab gives you a boost — move forward two spaces!",
            "A sneaky pirate jumps out and stops you — move back one space.",
            "BOOM! A nearby ship fires a cannonball across your bow — retreat back one space!",
            "A massive whale surfaces in your path — you're forced to move back one space to avoid it!"
    };

    public GameController(BoardGrid boardGrid, GameTrack track) {
        this.boardGrid = boardGrid;
        this.selectedTrack = track;

        // Based on the game mode chosen, set the special spaces indices
        String trackName = selectedTrack.getName();
        if (Objects.equals(trackName, "Track 1")) {
            this.specialIdx = specialIdx1;
            this.specialActions = specialActions1;
            this.messages = specialMessage1;
        }
        else if (Objects.equals(trackName, "Track 2")) {
            this.specialIdx = specialIdx2;
            this.specialActions = specialActions2;
            this.messages = specialMessage2;
        }
        else if (Objects.equals(trackName, "Track 3")) {
            this.specialIdx = specialIdx3;
            this.specialActions = specialActions3;
            this.messages = specialMessage3;
        }

        // Create the track display pane
        trackDisplayPane = new Pane();

        // Create themed player token instead of the yellow circle
        playerToken = createThemedPlayerToken();

        // Add the player token to the track display pane
        trackDisplayPane.getChildren().add(playerToken);
    }

    /**
     * Creates a themed player token based on the selected track
     * @return A Node representing the themed player token
     */
    private Node createThemedPlayerToken() {
        String trackName = selectedTrack.getName();

        // Create a StackPane to hold our token
        StackPane token = new StackPane();
        token.setPrefSize(40, 40);

        // For visual appeal if the image fails to load
        Circle fallbackCircle = new Circle(20);
        Text symbolText = new Text();
        symbolText.setFont(Font.font("Arial", 24));

        if (Objects.equals(trackName, "Track 1")) {
            // Shrek theme
            fallbackCircle.setFill(Color.web("#7c9f45", 0.8)); // Shrek green
            symbolText.setText("🧌"); // Ogre emoji for Shrek
        } else if (Objects.equals(trackName, "Track 2")) {
            // Rainforest theme
            fallbackCircle.setFill(Color.web("#1a936f", 0.8)); // Jungle green
            symbolText.setText("🐒"); // Monkey emoji
        } else {
            // Pirate theme
            fallbackCircle.setFill(Color.web("#c99767", 0.8)); // Gold/treasure color
            symbolText.setText("🏴‍☠️"); // Pirate flag emoji
        }

        // Add stroke to make it stand out
        fallbackCircle.setStroke(Color.BLACK);
        fallbackCircle.setStrokeWidth(2);

        // Add shadow effect for depth
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.color(0, 0, 0, 0.3));
        token.setEffect(shadow);

        // Add fallback elements to the token
        token.getChildren().addAll(fallbackCircle, symbolText);

        return token;
    }

    /**
     * Calculate the grid board number from row and column coordinates
     */
    private int calculateGridNumber(int row, int col) {
        if (row % 2 == 0) {
            // Left to right rows
            return row * 8 + col + 1;
        } else {
            // Right to left rows
            return row * 8 + (8 - col);
        }
    }

    public void setRollDiceButton(Button rollDiceButton) {
        this.rollDiceButton = rollDiceButton;
    }

    public void setDiceResultText(Text diceResultText) {
        this.diceResultText = diceResultText;
    }

    public void initializePlayer() {
        // Initialize player on first space of the track
        currentTrackIndex = 0;
        placePlayerOnFirstSpace();

        // Initialize player
        int[] startPosition = selectedTrack.getTrackPositions().get(0);
        currentPlayer = new Player(1, startPosition, true);
    }

    private void placePlayerOnFirstSpace() {
        if (selectedTrack != null && !selectedTrack.getTrackPositions().isEmpty()) {
            int[] startPosition = selectedTrack.getTrackPositions().get(0);
            int row = startPosition[0];
            int col = startPosition[1];

            // Calculate the position based on the board coordinates rather than layout properties
            positionPlayerTokenAtBoardCoordinates(row, col);
        }
    }

    public void positionPlayerTokenAtBoardCoordinates(int row, int col) {
        // Get the board space
        BoardSpace space = boardGrid.getBoardSpace(row, col);
        if (space != null) {
            // Get the actual layout bounds
            double spaceX = space.getBoundsInParent().getMinX() + space.getParent().getLayoutX();
            double spaceY = space.getBoundsInParent().getMinY() + space.getParent().getLayoutY();

            // Set initial position based on current layout
            double centerX = spaceX + space.getWidth() / 2;
            double centerY = spaceY + space.getHeight() / 2;

            // Position the token at the center of the space
            playerToken.setLayoutX(centerX - playerToken.getBoundsInLocal().getWidth() / 2);
            playerToken.setLayoutY(centerY - playerToken.getBoundsInLocal().getHeight() / 2);

            space.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                double updatedX = space.getLocalToSceneTransform().getTx() + space.getWidth() / 2;
                double updatedY = space.getLocalToSceneTransform().getTy() + space.getHeight() / 2;

                playerToken.setLayoutX(updatedX - playerToken.getBoundsInLocal().getWidth() / 2);
                playerToken.setLayoutY(updatedY - playerToken.getBoundsInLocal().getHeight() / 2);
            });
        }
    }

    public void movePlayer(int steps) {
        // Disable roll button during animation
        animationInProgress = true;
        if (rollDiceButton != null) {
            rollDiceButton.setDisable(true);
        }

        System.out.println("Current track index before move: " + currentTrackIndex);

        // Get current track positions
        List<int[]> trackPositions = selectedTrack.getTrackPositions();

        // Calculate target position index
        int targetIndex = Math.min(currentTrackIndex + steps, trackPositions.size() - 1);
        System.out.println("Target index: " + targetIndex);

        // Determine if this move results in the player winning
        boolean isWinningMove = (targetIndex >= trackPositions.size() - 1);
        System.out.println("Is winning move: " + isWinningMove);

        // Get the timeline of the animation of player movement
        Timeline moveTimeline = animatePlayerMovement(currentTrackIndex, targetIndex, trackPositions, isWinningMove);

        // Check if landing on a special space
        int specialAction = 0;
        String specialMsg = null;

        // Debug output to check special square detection
        System.out.println("Checking if " + targetIndex + " is a special space");
        System.out.println("Special indices: " + java.util.Arrays.toString(specialIdx));

        for (int i = 0; i < specialIdx.length; i++) {
            if (specialIdx[i] == targetIndex) {
                specialAction = specialActions[i];
                specialMsg = messages[i];
                System.out.println("Found special space at index " + i + ": action=" + specialAction + ", message=" + specialMsg);
                break;
            }
        }

        // If space is special, need to trigger additional animation
        if (specialAction != 0) {
            int finalTargetIndex = targetIndex + specialAction;

            // Make sure finalTargetIndex stays within bounds
            finalTargetIndex = Math.max(0, Math.min(finalTargetIndex, trackPositions.size() - 1));

            System.out.println("Player hit a special space! Will move to: " + finalTargetIndex);
            System.out.println("Message: " + specialMsg);
            setSpecialMessage(specialMsg);

            // Send initial and final target index to arduino
            ArduinoConnector.sendTargetIndices(targetIndex, finalTargetIndex);

            // Store final values for use in the lambda
            final int finalFinalTargetIndex = finalTargetIndex;

            moveTimeline.setOnFinished(event -> {
                // Pause for a short delay before triggering second animation
                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(pauseEvent -> {
                    Timeline specialMoveTimeline = animatePlayerMovement(targetIndex, finalFinalTargetIndex, trackPositions, false);
                    specialMoveTimeline.setOnFinished(e -> {
                        animationInProgress = false;
                        if (rollDiceButton != null) {
                            rollDiceButton.setDisable(false);
                        }
                    });
                    specialMoveTimeline.play();
                });
                pause.play();
            });
        }
        else {
            // Send indices to arduino: since not a special space, initial and final target index will be the same
            ArduinoConnector.sendTargetIndices(targetIndex, targetIndex);

            // Now just re-enable after first move finishes and set the special message to null (should still be null if reaches this statement)
            setSpecialMessage(specialMsg);
            moveTimeline.setOnFinished(event -> {
                animationInProgress = false;
                if (rollDiceButton != null) {
                    rollDiceButton.setDisable(false);
                }
            });
        }

        // Play the first animation
        moveTimeline.play();
    }

    // Add overloaded method to handle button parameter
    public void movePlayer(int steps, Button button) {
        this.rollDiceButton = button;
        movePlayer(steps);
    }

    private Timeline animatePlayerMovement(int startIndex, int endIndex, List<int[]> trackPositions, boolean isWinningMove) {
        // Create a timeline for animation
        Timeline timeline = new Timeline();

        // Duration for each hop
        double hopDuration = 0.7; // seconds
        double timePoint = 0;

        // Determine if moving forwards or backwards
        if (endIndex > startIndex) {
            // Move forward
            for (int i = startIndex + 1; i <= endIndex; i++) {
                animateStep(timeline, i, endIndex, trackPositions, hopDuration, timePoint, isWinningMove);
                timePoint += hopDuration;
            }
        }
        else {
            // Move backward
            for (int i = startIndex - 1; i >= endIndex; i--) {
                animateStep(timeline, i, endIndex, trackPositions, hopDuration, timePoint, isWinningMove);
                timePoint += hopDuration;
            }
        }

        return timeline;
    }

    private void animateStep(Timeline timeline, int index, int endIndex, List<int[]> trackPositions,
                             double hopDuration, double timePoint, boolean isWinningMove) {
        // Calculate positions
        int[] position = trackPositions.get(index);
        int row = position[0];
        int col = position[1];

        BoardSpace space = boardGrid.getBoardSpace(row, col);
        if (space != null) {
            // Get the parent of the player token
            Node tokenParent = playerToken.getParent();

            // Get the actual bounds of the space in scene coordinates
            Bounds spaceBounds = space.localToScene(space.getBoundsInLocal());
            Point2D spaceCenter = tokenParent.sceneToLocal(
                    spaceBounds.getMinX() + spaceBounds.getWidth()/2,
                    spaceBounds.getMinY() + spaceBounds.getHeight()/2
            );

            // Use these exact coordinates for positioning
            double centerX = spaceCenter.getX();
            double centerY = spaceCenter.getY();

            // Create final copies for event handler
            final double finalCenterX = centerX;
            final double finalCenterY = centerY;
            final int currentIndex = index;

            // Calculate token's center point
            double tokenWidth = playerToken.getBoundsInLocal().getWidth();
            double tokenHeight = playerToken.getBoundsInLocal().getHeight();

            // Add hop up animation
            KeyFrame hopUpFrame = new KeyFrame(
                    Duration.seconds(timePoint + hopDuration/3),
                    new KeyValue(playerToken.layoutXProperty(), centerX - tokenWidth/2),
                    new KeyValue(playerToken.layoutYProperty(), centerY - tokenHeight/2 - 20) // Hop up 20 pixels
            );

            // Add landing animation
            KeyFrame landFrame = new KeyFrame(
                    Duration.seconds(timePoint + hopDuration),
                    event -> {
                        if (currentIndex == endIndex) {
                            // Update the current index when this frame finishes
                            currentTrackIndex = currentIndex;
                            playerToken.setLayoutX(finalCenterX - tokenWidth/2);
                            playerToken.setLayoutY(finalCenterY - tokenHeight/2);

                            // Update player object location
                            if (currentPlayer != null) {
                                currentPlayer.setLocation(position);
                            }

                            // Handle winning move
                            if (isWinningMove) {
                                showWinnerScreen();
                                ArduinoConnector.sendWonGame();
                            } else {
                                // Re-enable roll button after animation completes
                                animationInProgress = false;
                                if (rollDiceButton != null) {
                                    rollDiceButton.setDisable(false);
                                }
                            }
                        }
                    },
                    new KeyValue(playerToken.layoutXProperty(), centerX - tokenWidth/2),
                    new KeyValue(playerToken.layoutYProperty(), centerY - tokenHeight/2)
            );

            timeline.getKeyFrames().addAll(hopUpFrame, landFrame);
        }
    }

    private void showWinnerScreen() {
        try {
            // Get the main stage from the track display pane's scene
            Stage mainStage = (Stage) trackDisplayPane.getScene().getWindow();

            // Load the WinnerScreen.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/WinnerScreen.fxml"));
            Parent root = loader.load();

            // Get the controller and set the player info
            WinnerScreenController controller = loader.getController();
            controller.setPlayerInfo(currentPlayer);

            // Create and show the scene
            Scene winnerScene = new Scene(root, 800, 600);
            mainStage.setScene(winnerScene);
        } catch (IOException e) {
            System.out.println("Error loading winner screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Pane getTrackDisplayPane() {
        return trackDisplayPane;
    }

    public boolean isAnimationInProgress() {
        return animationInProgress;
    }

    public GameTrack getSelectedTrack() {
        return selectedTrack;
    }

    public String getSpecialMessage() { return specialMessage; }

    public void setSpecialMessage(String specialMessage) { this.specialMessage = specialMessage; }
}