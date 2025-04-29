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
        }
    }

    private void highlightSelectedButton(Button selected, Button... others) {
        selected.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-border-color: black; -fx-border-width: 2;");

        for (Button other : others) {
            other.setStyle("-fx-padding: 10; -fx-font-size: 14px;");
        }
    }
}