package org.example.sdprototype.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class GameSelectionScreenController {
    @FXML
    private Button savedGame1Button;

    @FXML
    private Button savedGame2Button;

    @FXML
    private Button savedGame3Button;

    @FXML
    private Button createGameButton;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void handleSavedGameButton(ActionEvent event) {
        System.out.println("Loading saved game: " + ((Button)event.getSource()).getText());
        // add more when working on save files
    }

    @FXML
    public void handleCreateGameButton(ActionEvent event) {
        showTrackSelectionScreen();
    }

    private void showTrackSelectionScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrackSelectionScreen.fxml"));
            Parent root = loader.load();

            TrackSelectionScreenController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}