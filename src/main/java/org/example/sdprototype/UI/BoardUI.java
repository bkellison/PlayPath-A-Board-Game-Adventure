package org.example.sdprototype.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.example.sdprototype.Controllers.GameController;
import org.example.sdprototype.GridBoard.GameTrack;

public class BoardUI {
    private GameController gameController;
    private GameTrack selectedTrack;
    private Button rollDiceButton;

    public BoardUI(GameController gameController, GameTrack track) {
        this.gameController = gameController;
        this.selectedTrack = track;
    }

    public VBox createControlPanel() {
        VBox controlPanel = new VBox(20);
        controlPanel.setPadding(new Insets(20));
        controlPanel.setAlignment(Pos.TOP_CENTER);
        controlPanel.setPrefWidth(200);
        controlPanel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 1;");

        // Add roll dice button
        rollDiceButton = new Button("Roll Dice");
        rollDiceButton.setPrefWidth(160);
        rollDiceButton.setPrefHeight(40);
        rollDiceButton.setStyle("-fx-font-size: 14px;");

        rollDiceButton.setOnAction(e -> {
            if (!gameController.isAnimationInProgress()) {
                // Simulate rolling dice (1-6)
                int diceRoll = (int)(Math.random() * 6) + 1;

                // Display dice result
                Text diceResultText = new Text("Rolled: " + diceRoll);
                diceResultText.setFont(Font.font("Arial", 14));

                // Update or add the dice result text to the control panel
                if (controlPanel.getChildren().size() > 3) {
                    controlPanel.getChildren().set(3, diceResultText);
                } else {
                    controlPanel.getChildren().add(diceResultText);
                }

                // Move player based on dice roll
                gameController.movePlayer(diceRoll, rollDiceButton);
            }
        });

        // Add track info
        Text trackInfoLabel = new Text("Current Track: " + selectedTrack.getName());
        trackInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Add player info
        Text playerInfoLabel = new Text("Player 1");
        playerInfoLabel.setFont(Font.font("Arial", 14));

        controlPanel.getChildren().addAll(trackInfoLabel, playerInfoLabel, rollDiceButton);

        return controlPanel;
    }

    public Button getRollDiceButton() {
        return rollDiceButton;
    }
}