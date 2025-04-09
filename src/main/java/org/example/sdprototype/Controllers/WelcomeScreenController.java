package org.example.sdprototype.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeScreenController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;

        // Add key event handler for the scene
        Scene scene = stage.getScene();
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE) {
                showGameSelectionScreen();
            }
        });
    }

    private void showGameSelectionScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GameSelectionScreen.fxml"));
            Parent root = loader.load();

            GameSelectionScreenController controller = loader.getController();
            controller.setStage(stage);

            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}