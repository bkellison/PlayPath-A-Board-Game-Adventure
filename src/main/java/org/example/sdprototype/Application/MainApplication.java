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
        primaryStage.setTitle("Board Game");

        // Preload sounds
        preloadGameSounds();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WelcomeScreen.fxml"));
        Parent root = loader.load();

        // Get the controller and set the stage
        WelcomeScreenController controller = loader.getController();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        controller.setStage(primaryStage);

        primaryStage.show();
    }

    private void preloadGameSounds() {
        try {
            // Preload all sounds used in the game
            SoundManager.preloadSounds(
                    "/org/example/sdprototype/sounds/dice1.wav",
                    "/org/example/sdprototype/sounds/dice2.wav",
                    "/org/example/sdprototype/sounds/JumpSound.wav"
            );
            System.out.println("Game sounds preloaded successfully");
        } catch (Exception e) {
            System.err.println("Error preloading sounds: " + e.getMessage());
            e.printStackTrace(); // Add this to see detailed error
        }
    }

    @Override
    public void stop() {
        // Clean up sound resources when application closes
        System.out.println("Cleaning up sound resources...");
        SoundManager.cleanup();
    }

    public static void main(String[] args) {
        launch(args);
    }
}