package org.example.sdprototype.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.sdprototype.Controllers.WelcomeScreenController;
import org.example.sdprototype.Utilities.SoundManager;

public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting Application");
        primaryStage.setTitle("PlayPath: A Board Game Adventure");

        // Preload sounds
        preloadGameSounds();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WelcomeScreen.fxml"));
        Parent root = loader.load();

        // Get the controller and set the stage
        WelcomeScreenController controller = loader.getController();

        // Create the scene with larger initial dimensions
        Scene scene = new Scene(root, 1000, 750);
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(900);  // Set minimum width
        primaryStage.setMinHeight(700); // Set minimum height

        controller.setStage(primaryStage);

        // Center the window on screen
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void preloadGameSounds() {
        try {
            // Preload all sounds used in the game
            SoundManager.preloadSounds(
                    "/org/example/sdprototype/sounds/dice1.wav",
                    "/org/example/sdprototype/sounds/dice2.wav",
                    "/org/example/sdprototype/sounds/JumpSound.wav",
                    "/org/example/sdprototype/sounds/SuccessSound.wav",
                    "/org/example/sdprototype/sounds/FailSound.wav"
            );
            System.out.println("Game sounds preloaded successfully");
        } catch (Exception e) {
            System.err.println("Error preloading sounds: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        SoundManager.cleanup();
    }

    public static void main(String[] args) {
        launch(args);
    }
}