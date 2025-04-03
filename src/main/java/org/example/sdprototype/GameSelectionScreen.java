package org.example.sdprototype;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class GameSelectionScreen {
    private Stage primaryStage;
    private Scene scene;

    public GameSelectionScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createGameSelectionScreen();
    }

    private void createGameSelectionScreen() {
        // Create title
        Label titleLabel = new Label("Game Selection");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Create saved game buttons (up to 3)
        Button savedGame1Button = createGameButton("Saved Game 1", false);
        Button savedGame2Button = createGameButton("Saved Game 2", false);
        Button savedGame3Button = createGameButton("Saved Game 3", false);

        // Create new game button
        Button createGameButton = createGameButton("Create New Game", true);

        // Create layout for buttons
        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(savedGame1Button, savedGame2Button,
                savedGame3Button, createGameButton);

        // Main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setTop(titleLabel);
        root.setAlignment(titleLabel, Pos.CENTER);
        root.setCenter(buttonBox);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Create scene
        scene = new Scene(root, 800, 600);
    }

    private Button createGameButton(String text, boolean isCreateGame) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(50);
        button.setFont(Font.font("Arial", 14));

        if (isCreateGame) {
            // Special styling for Create Game button
            button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

            // Action for Create Game button
            button.setOnAction(e -> {
                TrackSelectionScreen trackSelectionScreen = new TrackSelectionScreen(primaryStage);
                primaryStage.setScene(trackSelectionScreen.getScene());
            });
        } else {
            // Action for saved game buttons (placeholder for now)
            button.setOnAction(e -> {
                System.out.println("Loading saved game: " + text);
                // In the future, this would load the saved game
            });
        }

        return button;
    }

    public Scene getScene() {
        return scene;
    }
}