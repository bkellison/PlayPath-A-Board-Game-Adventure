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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
    private Circle playerToken;
    private Pane trackDisplayPane;
    private boolean animationInProgress = false;
    private Button rollDiceButton;
    private Text diceResultText;
    private int[] specialIdx;
    private int[] specialActions;
    private String[] messages;
    private String specialMessage = null;

    // Arrays that hold the indices of the special spaces for each theme
    // These indices correspond to the positional indices in the track path, not the grid numbers
    private int[] specialIdx1;
    private final int[] specialIdx2 = {  };
    private final int[] specialIdx3 = {  };

    // Arrays to hold results of special spaces (ex. -1 space)
    private int[] specialActions1;
    private final int[] specialActions2 = {  };
    private final int[] specialActions3 = {  };

    // Arrays to hold the special messages that will be displayed on the screen as a result of landing on special spaces
    private String[] specialMessage1;
    private final String[] specialMessage2 = {  };
    private final String[] specialMessage3 = {  };

    // Original special messages for Track 1
    private final String[] originalSpecialMessages1 = {
            "Oh no! Lord Farquaad sents his knights after you — move back one space.",
            "Fiona shows you a shortcut — move up one space!",
            "Dragon swoops in and gives you a lift — fly forward three spaces!",
            "Not the gumdrop buttons! The Gingerbread Man lost a leg! — move back two spaces."
    };

    public GameController(BoardGrid boardGrid, GameTrack track) {
        this.boardGrid = boardGrid;
        this.selectedTrack = track;

        // Initialize the special squares for Track 1
        initializeSpecialSquares();

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

        // Create player token
        playerToken = new Circle(15, Color.YELLOW);
        playerToken.setStroke(Color.BLACK);
        playerToken.setStrokeWidth(2);

        // Add the player token to the track display pane
        trackDisplayPane.getChildren().add(playerToken);
    }

    /**
     * Initialize special squares by mapping grid board numbers to track indices
     */
    private void initializeSpecialSquares() {
        // Get the track positions to map grid numbers to track indices
        List<int[]> trackPositions = selectedTrack.getTrackPositions();

        // Create arrays to hold information for special squares
        specialIdx1 = new int[4];
        specialActions1 = new int[4];
        specialMessage1 = new String[4];

        // Find track indices corresponding to grid board numbers 3, 9, 13, and 38
        int count = 0;
        for (int i = 0; i < trackPositions.size(); i++) {
            int[] position = trackPositions.get(i);
            int row = position[0];
            int col = position[1];

            // Calculate the grid board number from row and col
            int gridNumber = calculateGridNumber(row, col);

            // Set the special square information based on the grid number
            if (gridNumber == 3) {
                specialIdx1[0] = i;
                specialActions1[0] = -1; // Move back 1 space
                specialMessage1[0] = originalSpecialMessages1[0]; // Lord Farquaad message
                count++;
            } else if (gridNumber == 9) {
                specialIdx1[1] = i;
                specialActions1[1] = -1; // Move back 1 space
                specialMessage1[1] = originalSpecialMessages1[3]; // Gingerbread Man message
                count++;
            } else if (gridNumber == 19) {
                specialIdx1[2] = i;
                specialActions1[2] = 1; // Move forward 1 space
                specialMessage1[2] = originalSpecialMessages1[1]; // Fiona message
                count++;
            } else if (gridNumber == 38) {
                specialIdx1[3] = i;
                specialActions1[3] = 3; // Move forward 3 spaces
                specialMessage1[3] = originalSpecialMessages1[2]; // Dragon message
                count++;
            }
            // If we've found all special squares, break the loop
            if (count == 4) {
                break;
            }
        }

        // Log the special square mapping for debugging
        System.out.println("Special squares initialized:");
        for (int i = 0; i < specialIdx1.length; i++) {
            System.out.println("Track index " + specialIdx1[i] + " (grid number " +
                    (i == 0 ? 3 : i == 1 ? 9 : i == 2 ? 13 : 38) +
                    ") will move " + (specialActions1[i] > 0 ? "forward " : "back ") +
                    Math.abs(specialActions1[i]) + " space(s)");
        }
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

            playerToken.setCenterX(centerX);
            playerToken.setCenterY(centerY);

            space.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                double updatedX = space.getLocalToSceneTransform().getTx() + space.getWidth() / 2;
                double updatedY = space.getLocalToSceneTransform().getTy() + space.getHeight() / 2;

                playerToken.setCenterX(updatedX);
                playerToken.setCenterY(updatedY);
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

            // Add hop up animation
            KeyFrame hopUpFrame = new KeyFrame(
                    Duration.seconds(timePoint + hopDuration/3),
                    new KeyValue(playerToken.centerXProperty(), centerX),
                    new KeyValue(playerToken.centerYProperty(), centerY - 20) // Hop up 20 pixels
            );

            // Add landing animation
            KeyFrame landFrame = new KeyFrame(
                    Duration.seconds(timePoint + hopDuration),
                    event -> {
                        if (currentIndex == endIndex) {
                            // Update the current index when this frame finishes
                            currentTrackIndex = currentIndex;
                            playerToken.setCenterX(finalCenterX);
                            playerToken.setCenterY(finalCenterY);

                            // Update player object location
                            if (currentPlayer != null) {
                                currentPlayer.setLocation(position);
                            }

                            // Handle winning move
                            if (isWinningMove) {
                                showWinnerScreen();
                            } else {
                                // Re-enable roll button after animation completes
                                animationInProgress = false;
                                if (rollDiceButton != null) {
                                    rollDiceButton.setDisable(false);
                                }
                            }
                        }
                    },
                    new KeyValue(playerToken.centerXProperty(), centerX),
                    new KeyValue(playerToken.centerYProperty(), centerY)
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