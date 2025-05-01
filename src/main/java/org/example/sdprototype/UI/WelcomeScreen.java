package org.example.sdprototype.UI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class WelcomeScreen {
    private Stage primaryStage;
    private Scene scene;

    public WelcomeScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createWelcomeScreen();
    }

    private void createWelcomeScreen() {
        // Create title label
        Label titleLabel = new Label("Board Game Adventure");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));

        // Create welcome message
        Label welcomeLabel = new Label("Welcome to the Game!");
        welcomeLabel.setFont(Font.font("Arial", 20));

        // Create instruction
        Label instructionLabel = new Label("Press SPACE to continue");
        instructionLabel.setFont(Font.font("Arial", 16));

        // Create layout
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, welcomeLabel, instructionLabel);
        root.setStyle("-fx-background-color: #f0f8ff;");

        // Create scene
        scene = new Scene(root, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}