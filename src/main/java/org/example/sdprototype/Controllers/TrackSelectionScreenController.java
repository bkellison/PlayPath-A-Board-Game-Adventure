package org.example.sdprototype.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.sdprototype.GridBoard.GameBoard;
import org.example.sdprototype.GridBoard.GameTrack;
import org.example.sdprototype.Communicator.ArduinoConnector;

public class TrackSelectionScreenController {
    @FXML
    private Button track1Button;

    @FXML
    private Button track2Button;

    @FXML
    private Button track3Button;

    @FXML
    private Button startGameButton;

    private Stage stage;
    private GameTrack selectedTrack = null;

    @FXML
    public void initialize() {
        System.out.println("TrackSelectionScreenController initialized");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void handleTrack1Button(ActionEvent event) {
        selectedTrack = GameTrack.createTrack1();
        highlightSelectedButton(track1Button, track2Button, track3Button);
        startGameButton.setDisable(false);

        // Send game mode to arduino for LED light behavior
        ArduinoConnector.sendGameMode(1);
    }

    @FXML
    void handleTrack2Button(ActionEvent event) {
        selectedTrack = GameTrack.createTrack2();
        highlightSelectedButton(track2Button, track1Button, track3Button);
        startGameButton.setDisable(false);

        // Send game mode to arduino for LED light behavior
        ArduinoConnector.sendGameMode(2);
    }

    @FXML
    void handleTrack3Button(ActionEvent event) {
        selectedTrack = GameTrack.createTrack3();
        highlightSelectedButton(track3Button, track1Button, track2Button);
        startGameButton.setDisable(false);

        // Send game mode to arduino for LED light behavior
        ArduinoConnector.sendGameMode(3);
    }

    @FXML
    void handleStartGameButton(ActionEvent event) {
        if (selectedTrack != null) {
            // Start the game with the selected track
            GameBoard gameBoard = new GameBoard();
            gameBoard.setTrack(selectedTrack);
            gameBoard.init(stage);

            ArduinoConnector.sendStartGame();
        }
    }

    private void highlightSelectedButton(Button selected, Button... others) {
        // Determine which track is selected and use the appropriate highlighting style
        if (selected == track1Button) {
            // Shrek Theme - Enhanced selection style
            selected.setStyle("-fx-background-color: #3e5520; -fx-text-fill: #ffffff; " +
                    "-fx-border-color: #d9ed92; -fx-border-width: 3; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 8, 0, 0, 2);");
        }
        else if (selected == track2Button) {
            // Rainforest Theme - Enhanced selection style
            selected.setStyle("-fx-background-color: #114b5f; -fx-text-fill: #ffffff; " +
                    "-fx-border-color: #88d498; -fx-border-width: 3; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 8, 0, 0, 2);");
        }
        else if (selected == track3Button) {
            // Pirate Theme - Enhanced selection style
            selected.setStyle("-fx-background-color: #7d5a3c; -fx-text-fill: #ffffff; " +
                    "-fx-border-color: #e6c89c; -fx-border-width: 3; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; " +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.5), 8, 0, 0, 2);");
        }

        // Reset other buttons to their default styles
        for (Button other : others) {
            if (other == track1Button) {
                other.setStyle("-fx-background-color: #7c9f45; -fx-text-fill: #f2e8c9; " +
                        "-fx-border-color: #3e5520; -fx-border-width: 3; " +
                        "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 0);");
            }
            else if (other == track2Button) {
                other.setStyle("-fx-background-color: #1a936f; -fx-text-fill: #f3e9d2; " +
                        "-fx-border-color: #88d498; -fx-border-width: 3; " +
                        "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 0);");
            }
            else if (other == track3Button) {
                other.setStyle("-fx-background-color: #c99767; -fx-text-fill: #2c1603; " +
                        "-fx-border-color: #7d5a3c; -fx-border-width: 3; " +
                        "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 5, 0, 0, 0);");
            }
        }

        // Update Start Game button to match selected theme
        if (selectedTrack != null) {
            if (selected == track1Button) {
                // Shrek Theme - make button more vibrant
                startGameButton.setStyle("-fx-background-color: #3e5520; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 35; -fx-font-size: 18px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.7), 10, 0, 0, 3);");
                startGameButton.setText("To The Swamp!");
            }
            else if (selected == track2Button) {
                // Rainforest Theme - make button more vibrant
                startGameButton.setStyle("-fx-background-color: #114b5f; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 35; -fx-font-size: 18px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.7), 10, 0, 0, 3);");
                startGameButton.setText("Into The Jungle!");
            }
            else if (selected == track3Button) {
                // Pirate Theme - make button more vibrant
                startGameButton.setStyle("-fx-background-color: #7d5a3c; -fx-text-fill: white; " +
                        "-fx-font-weight: bold; -fx-background-radius: 35; -fx-font-size: 18px; " +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.7), 10, 0, 0, 3);");
                startGameButton.setText("Set Sail!");
            }
        }
    }
}