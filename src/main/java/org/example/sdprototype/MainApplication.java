package org.example.sdprototype;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The main application class for the board game.
 * This class initializes the game and shows the welcome screen.
 */
public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Board Game");

        // Create and show the welcome screen
        WelcomeScreen welcomeScreen = new WelcomeScreen(primaryStage);
        primaryStage.setScene(welcomeScreen.getScene());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}