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

    @FXML
    private Text diceResultText;

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
            System.out.println("Rolled value: " + diceRoll);
            diceResultText.setText("Roll Dice: " + diceRoll);

            // Move player based on dice roll
            gameController.movePlayer(diceRoll, rollDiceButton);

            // Get the message to be displayed if the user has landed on a special space:
            String message = gameController.getSpecialMessage();
            if (message != null) {
                System.out.println("UI controller has received message: " + message);
            }
            else {
                System.out.println("UI controller has received NO special message");
            }
        }
    }

    public Button getRollDiceButton() {
        return rollDiceButton;
    }
}