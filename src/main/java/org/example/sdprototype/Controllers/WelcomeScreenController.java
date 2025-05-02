package org.example.sdprototype.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeScreenController {
    private Stage stage;

    @FXML
    public void initialize() {
        System.out.println("WelcomeScreenController initialized");
    }

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
            // Load TrackSelectionScreen.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrackSelectionScreen.fxml"));
            Parent root = loader.load();

            // Get the controller and set the stage
            TrackSelectionScreenController controller = loader.getController();
            controller.setStage(stage);

            // Create a larger scene with the new dimensions
            Scene scene = new Scene(root, 1000, 750);
            stage.setScene(scene);
            stage.setTitle("Track Selection - PlayPath");

            // Center the window on screen after resizing
            stage.centerOnScreen();
        } catch (IOException e) {
            System.err.println("Error loading track selection screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}