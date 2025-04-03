package org.example.sdprototype.UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sdprototype.GridBoard.GameBoard;
import org.example.sdprototype.GridBoard.GameTrack;

public class TrackSelectionScreen {
    private Stage primaryStage;
    private Scene scene;
    private GameTrack selectedTrack = null;

    public TrackSelectionScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
        createTrackSelectionScreen();
    }

    private void createTrackSelectionScreen() {
        // Create title
        Label titleLabel = new Label("Select a Track");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Create track buttons
        Button track1Button = createTrackButton("Track 1", Color.BLUE);
        Button track2Button = createTrackButton("Track 2", Color.RED);
        Button track3Button = createTrackButton("Track 3", Color.GREEN);

        // Create start game button
        Button startGameButton = new Button("Start Game");
        startGameButton.setPrefWidth(200);
        startGameButton.setPrefHeight(50);
        startGameButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        startGameButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        startGameButton.setDisable(true); // Initially disabled until track is selected

        startGameButton.setOnAction(e -> {
            if (selectedTrack != null) {
                // Start the game with the selected track
                GameBoard gameBoard = new GameBoard();
                gameBoard.setTrack(selectedTrack);
                gameBoard.init(primaryStage);
            }
        });

        // Track information
        VBox trackInfoBox = createTrackInfoSection();

        // Create layout for track buttons
        VBox trackButtonsBox = new VBox(15);
        trackButtonsBox.setAlignment(Pos.CENTER);
        trackButtonsBox.getChildren().addAll(track1Button, track2Button, track3Button);

        // Create layout for all buttons
        VBox allButtonsBox = new VBox(30);
        allButtonsBox.setAlignment(Pos.CENTER);
        allButtonsBox.getChildren().addAll(trackButtonsBox, startGameButton);

        // Main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setTop(titleLabel);
        root.setAlignment(titleLabel, Pos.CENTER);
        root.setCenter(allButtonsBox);
        root.setRight(trackInfoBox);
        root.setStyle("-fx-background-color: #f5f5f5;");

        // Create scene
        scene = new Scene(root, 800, 600);

        // Track 1 button action
        track1Button.setOnAction(e -> {
            selectedTrack = GameTrack.createTrack1();
            highlightSelectedButton(track1Button, track2Button, track3Button);
            startGameButton.setDisable(false);
        });

        // Track 2 button action
        track2Button.setOnAction(e -> {
            selectedTrack = GameTrack.createTrack2();
            highlightSelectedButton(track2Button, track1Button, track3Button);
            startGameButton.setDisable(false);
        });

        // Track 3 button action
        track3Button.setOnAction(e -> {
            selectedTrack = GameTrack.createTrack3();
            highlightSelectedButton(track3Button, track1Button, track2Button);
            startGameButton.setDisable(false);
        });
    }

    private Button createTrackButton(String name, Color color) {
        Button button = new Button();
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setStyle("-fx-padding: 10; -fx-font-size: 14px;");

        // Create an HBox to hold circle indicator and text
        HBox content = new HBox(10);
        content.setAlignment(Pos.CENTER_LEFT);

        // Create color indicator
        Circle indicator = new Circle(8, color);

        // Add indicator and text to content
        Label buttonText = new Label(name);
        content.getChildren().addAll(indicator, buttonText);

        button.setGraphic(content);
        return button;
    }

    private void highlightSelectedButton(Button selected, Button... others) {
        selected.setStyle("-fx-padding: 10; -fx-font-size: 14px; -fx-border-color: black; -fx-border-width: 2;");

        for (Button other : others) {
            other.setStyle("-fx-padding: 10; -fx-font-size: 14px;");
        }
    }

    private VBox createTrackInfoSection() {
        VBox trackInfoBox = new VBox(10);
        trackInfoBox.setPadding(new Insets(20));
        trackInfoBox.setStyle("-fx-background-color: #e0e0e0; -fx-border-color: #cccccc; -fx-border-width: 1;");
        trackInfoBox.setPrefWidth(250);

        Label infoTitleLabel = new Label("Track Information");
        infoTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Text trackInfoText = new Text(
                "Track 1: Classic path - A straightforward track that follows the outside of the board. Best for beginners.\n\n" +
                        "Track 2: Medium difficulty - This track cuts across the board in a zigzag pattern. Offers a moderate challenge.\n\n" +
                        "Track 3: Expert level - This complex track has many intersections and is the most challenging option."
        );
        trackInfoText.setWrappingWidth(210);

        trackInfoBox.getChildren().addAll(infoTitleLabel, trackInfoText);
        return trackInfoBox;
    }

    public Scene getScene() {
        return scene;
    }
}