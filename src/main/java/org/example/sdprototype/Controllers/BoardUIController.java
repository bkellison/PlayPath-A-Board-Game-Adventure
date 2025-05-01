package org.example.sdprototype.Controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.sdprototype.GameLogic.DiceAnimator;
import org.example.sdprototype.GridBoard.GameTrack;

import java.util.Objects;

public class BoardUIController {
    @FXML
    public Button rollDiceButton;

    @FXML
    private Text trackInfoLabel;

    @FXML
    private Text playerInfoLabel;

    @FXML
    private Text diceResultText;

    @FXML
    private StackPane dicePane;

    @FXML
    private Label messageLabel;

    private GameController gameController;
    private GameTrack selectedTrack;
    private DiceAnimator diceAnimator;

    @FXML
    public void initialize() {
        System.out.println("Initializing BoardUIController...");

        // Debug information to check if the FXML components are properly loaded
        System.out.println("dicePane: " + (dicePane != null ? "found" : "null"));
        System.out.println("diceResultText: " + (diceResultText != null ? "found" : "null"));
        System.out.println("rollDiceButton: " + (rollDiceButton != null ? "found" : "null"));

        initializeDiceAnimator();
    }

    private void initializeDiceAnimator() {
        // Initialize the dice animator when the FXML is loaded
        if (dicePane != null && diceResultText != null) {
            try {
                diceAnimator = new DiceAnimator(dicePane, diceResultText);
                System.out.println("Dice animator created successfully");
            } catch (Exception e) {
                System.err.println("Error creating dice animator: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Cannot initialize dice animator: dicePane or diceResultText is null");
        }
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public void setSelectedTrack(GameTrack track) {
        this.selectedTrack = track;

        // Display track name depending on the mode selected
        String trackName = selectedTrack.getName();
        if (Objects.equals(trackName, "Track 1")) {
            trackInfoLabel.setText("Shrek's Swamp Trek");
        }
        else if (Objects.equals(trackName, "Track 2")) {
            trackInfoLabel.setText("Rainforest Rumble");
        }
        else {
            trackInfoLabel.setText("Pirate's Gold Rush");
        }

        // Initialize dice animator if it wasn't initialized in the FXML initialize method
        if (diceAnimator == null) {
            initializeDiceAnimator();
        }
    }

    @FXML
    public void handleRollDice(ActionEvent event) {
        System.out.println("Roll dice button clicked");

        if (diceAnimator == null) {
            System.err.println("Dice animator is null, attempting to reinitialize");
            initializeDiceAnimator();

            if (diceAnimator == null) {
                System.err.println("Failed to initialize dice animator, cannot proceed");
                messageLabel.setText("Error: Dice system not initialized");
                return;
            }
        }

        if (gameController != null && !gameController.isAnimationInProgress()) {
            // Disable roll button during dice animation
            rollDiceButton.setDisable(true);

            try {
                // Start dice roll animation
                diceAnimator.rollDice(() -> {
                    // This is called when the dice animation completes
                    int diceRoll = diceAnimator.getFinalResult();
                    System.out.println("Dice roll result: " + diceRoll);

                    // Move player based on dice roll
                    gameController.movePlayer(diceRoll, rollDiceButton);

                    // Get the message to be displayed if the user has landed on a special space
                    String message = gameController.getSpecialMessage();

                    if (message != null) {
                        // If there is a special message, delay showing it
                        PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                        pause.setOnFinished(pauseEvent -> {
                            System.out.println("UI controller has received message: " + message);
                            messageLabel.setText(message);
                            // Remove duplicate sound here since we're playing the appropriate sound in GameController
                        });
                        pause.play();
                    } else {
                        // No special message, update immediately
                        System.out.println("UI controller has received NO special message");
                        messageLabel.setText("");
                    }
                });
            } catch (Exception e) {
                System.err.println("Error during dice roll: " + e.getMessage());
                e.printStackTrace();
                rollDiceButton.setDisable(false);
                messageLabel.setText("Error rolling dice");
            }
        }
    }

    public Button getRollDiceButton() {
        return rollDiceButton;
    }

    public Text getTrackInfoLabel() { return trackInfoLabel; }
    public void setTrackInfoLabel(Text trackInfoLabel) { this.trackInfoLabel = trackInfoLabel; }

    public Text getPlayerInfoLabel() { return playerInfoLabel; }
    public void setPlayerInfoLabel(Text playerInfoLabel) { this.playerInfoLabel = playerInfoLabel; }

    public Text getDiceResultText() { return diceResultText; }
    public void setDiceResultText(Text diceResultText) { this.diceResultText = diceResultText; }

    public StackPane getDicePane() { return dicePane; }
    public void setDicePane(StackPane dicePane) { this.dicePane = dicePane; }

    public Label getMessageLabel() { return messageLabel; }
    public void setMessageLabel(Label messageLabel) { this.messageLabel = messageLabel; }

    public void manuallyInitializeDiceAnimator() {
        if (dicePane != null && diceResultText != null) {
            System.out.println("Manually initializing dice animator...");
            try {
                diceAnimator = new DiceAnimator(dicePane, diceResultText);
                System.out.println("Dice animator created successfully through manual initialization");
            } catch (Exception e) {
                System.err.println("Error creating dice animator: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("Cannot manually initialize dice animator: components still null");

            // Print debug info about the state of UI components
            System.out.println("Current controller state:");
            System.out.println("dicePane: " + dicePane);
            System.out.println("diceResultText: " + diceResultText);
            System.out.println("rollDiceButton: " + rollDiceButton);
            System.out.println("messageLabel: " + messageLabel);
        }
    }
}