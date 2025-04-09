package org.example.sdprototype.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.example.sdprototype.GridBoard.GameTrack;

public class BoardUIController {
    @FXML
    public Button rollDiceButton;

    @FXML
    private Text trackInfoLabel;

    @FXML
    private Text playerInfoLabel;

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
        trackInfoLabel.setText("Current Track: " + track.getName());
    }

    @FXML
    public void handleRollDice(ActionEvent event) {
        if (gameController != null && !gameController.isAnimationInProgress()) {
            // Simulate rolling dice (1-6)
            int diceRoll = (int)(Math.random() * 6) + 1;

            // Display dice result
            Text diceResultText = new Text("Rolled: " + diceRoll);

            // Move player based on dice roll
            gameController.movePlayer(diceRoll, rollDiceButton);
        }
    }

    public Button getRollDiceButton() {
        return rollDiceButton;
    }
}