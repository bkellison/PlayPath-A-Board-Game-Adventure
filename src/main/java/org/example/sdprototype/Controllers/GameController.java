package org.example.sdprototype.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
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

import java.io.IOException;
import java.util.List;

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

    public GameController(BoardGrid boardGrid, GameTrack track) {
        this.boardGrid = boardGrid;
        this.selectedTrack = track;

        // Create the track display pane
        trackDisplayPane = new Pane();

        // Create player token
        playerToken = new Circle(15, Color.YELLOW);
        playerToken.setStroke(Color.BLACK);
        playerToken.setStrokeWidth(2);

        // Add the player token to the track display pane
        trackDisplayPane.getChildren().add(playerToken);
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

        // Check if player has reached or passed the end
        if (targetIndex >= trackPositions.size() - 1) {
            // Final hop animation to the last space
            animatePlayerMovement(currentTrackIndex, trackPositions.size() - 1, trackPositions, true);
        } else {
            // Normal movement animation
            animatePlayerMovement(currentTrackIndex, targetIndex, trackPositions, false);
        }
    }

    // Add overloaded method to handle button parameter
    public void movePlayer(int steps, Button button) {
        this.rollDiceButton = button;
        movePlayer(steps);
    }

    private void animatePlayerMovement(int startIndex, int endIndex, List<int[]> trackPositions, boolean isWinningMove) {
        // Create a timeline for animation
        Timeline timeline = new Timeline();

        // Duration for each hop
        double hopDuration = 0.7; // seconds

        double timePoint = 0;

        // For each step in the path
        for (int i = startIndex + 1; i <= endIndex; i++) {
            int index = i;

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

                // Increment the time point for the next hop
                timePoint += hopDuration;
            }
        }

        // Play the animation
        timeline.play();
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
}