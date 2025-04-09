package org.example.sdprototype.Controllers;
import org.example.sdprototype.GridBoard.GameBoard;
import org.example.sdprototype.GridBoard.GameTrack;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.sdprototype.Application.MainApplication;
import org.example.sdprototype.GameLogic.Player;
import org.example.sdprototype.GridBoard.BoardSpace;
import org.example.sdprototype.GridBoard.BoardGrid;
import org.example.sdprototype.UI.TrackSelectionScreen;
import org.example.sdprototype.UI.WelcomeScreen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;

import java.util.List;

public class GameController {
    private BoardGrid boardGrid;
    private GameTrack selectedTrack;
    private Player currentPlayer;
    private int currentTrackIndex = 0; // Track the current position in the track
    private Circle playerToken;
    private Pane trackDisplayPane;
    private boolean animationInProgress = false;

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

    public void movePlayer(int steps, Button rollDiceButton) {
        // Disable roll button during animation
        animationInProgress = true;
        rollDiceButton.setDisable(true);

        System.out.println("Current track index before move: " + currentTrackIndex); // Add logging

        // Get current track positions
        List<int[]> trackPositions = selectedTrack.getTrackPositions();

        // Calculate target position index
        int targetIndex = Math.min(currentTrackIndex + steps, trackPositions.size() - 1);

        // Check if player has reached or passed the end
        if (targetIndex >= trackPositions.size() - 1) {
            // Final hop animation to the last space
            animatePlayerMovement(currentTrackIndex, trackPositions.size() - 1, trackPositions, true, rollDiceButton);
        } else {
            // Normal movement animation
            animatePlayerMovement(currentTrackIndex, targetIndex, trackPositions, false, rollDiceButton);
        }
    }
    private void animatePlayerMovement(int startIndex, int endIndex, List<int[]> trackPositions,
                                       boolean isWinningMove, Button rollDiceButton) {
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
                javafx.scene.Node tokenParent = playerToken.getParent();

                // Get the actual bounds of the space in scene coordinates
                javafx.geometry.Bounds spaceBounds = space.localToScene(space.getBoundsInLocal());
                javafx.geometry.Point2D spaceCenter = tokenParent.sceneToLocal(
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
                                    rollDiceButton.setDisable(false);
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
        // Get the main stage from the roll button's scene
        Stage mainStage = (Stage) trackDisplayPane.getScene().getWindow();

        // Create the winner screen
        BorderPane winnerLayout = new BorderPane();
        winnerLayout.setStyle("-fx-background-color: #f8f9fa;");

        VBox contentBox = new VBox(30);
        contentBox.setAlignment(Pos.CENTER);

        Text congratsText = new Text("Congratulations!");
        congratsText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        congratsText.setFill(Color.GREEN);

        Text winnerText = new Text("Player 1 has won the game!");
        winnerText.setFont(Font.font("Arial", 24));

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setPrefWidth(200);
        playAgainButton.setPrefHeight(50);
        playAgainButton.setFont(Font.font("Arial", 16));
        playAgainButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setPrefWidth(200);
        mainMenuButton.setPrefHeight(50);
        mainMenuButton.setFont(Font.font("Arial", 16));

        contentBox.getChildren().addAll(congratsText, winnerText, playAgainButton, mainMenuButton);
        winnerLayout.setCenter(contentBox);

        Scene winnerScene = new Scene(winnerLayout, 800, 600);
        mainStage.setScene(winnerScene);

        // Set button actions
        playAgainButton.setOnAction(e -> {
            // Reset to track selection
            try {
                TrackSelectionScreen trackScreen = new TrackSelectionScreen(mainStage);
                mainStage.setScene(trackScreen.getScene());
            } catch (Exception ex) {
                System.out.println("Could not return to track selection. Please update the imports.");
                GameBoard newGame = new GameBoard();
                newGame.setTrack(selectedTrack);
                newGame.init(mainStage);
            }
        });

        mainMenuButton.setOnAction(e -> {
            // Return to welcome screen
            try {
                WelcomeScreen welcomeScreen = new WelcomeScreen(mainStage);
                mainStage.setScene(welcomeScreen.getScene());
            } catch (Exception ex) {
                System.out.println("Could not return to welcome screen. Please update the imports.");
                try {
                    new MainApplication().start(new Stage());
                    mainStage.close();
                } catch (Exception ex2) {
                    System.out.println("Could not restart application.");
                }
            }
        });
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