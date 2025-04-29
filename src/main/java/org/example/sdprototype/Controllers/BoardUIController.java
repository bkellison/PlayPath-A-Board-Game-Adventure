package org.example.sdprototype.Controllers;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.util.Duration;
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
    private Label messageLabel;

    private GameController gameController;
    private GameTrack selectedTrack;

    public void initialize() {
        // Initialize any required properties
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
            trackInfoLabel.setText("Ocean Odyssey");
        }
        else {
            trackInfoLabel.setText("Pirate's Gold Rush");
        }
    }

    @FXML
    public void handleRollDice(ActionEvent event) {
        if (gameController != null && !gameController.isAnimationInProgress()) {
            // Simulate rolling dice (1-6)
            int diceRoll = (int)(Math.random() * 6) + 1;

            // Display dice result
            System.out.println("Rolled value: " + diceRoll);
            diceResultText.setText("Roll Dice: " + diceRoll);

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
                });
                pause.play();
            } else {
                // No special message, update immediately
                System.out.println("UI controller has received NO special message");
                messageLabel.setText("");
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

    public Label getMessageLabel() { return messageLabel; }
    public void setMessageLabel(Label messageLabel) { this.messageLabel = messageLabel; }

}