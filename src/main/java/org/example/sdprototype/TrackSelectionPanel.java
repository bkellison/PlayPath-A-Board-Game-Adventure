package org.example.sdprototype;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.function.Consumer;

public class TrackSelectionPanel extends VBox {

    private final Consumer<GameTrack> trackSelectionHandler;

    public TrackSelectionPanel(Consumer<GameTrack> trackSelectionHandler) {
        super(20);
        this.trackSelectionHandler = trackSelectionHandler;

        // Configure panel layout
        setPadding(new Insets(20));
        setPrefWidth(250);
        setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 1;");

        setupComponents();
    }

    private void setupComponents() {
        // Title
        Label titleLabel = new Label("Select a Track");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setPadding(new Insets(0, 0, 10, 0));

        // Create track selection buttons
        VBox trackButtons = new VBox(15);
        trackButtons.setAlignment(Pos.CENTER_LEFT);

        // Track 1 button with blue indicator
        Button track1Btn = createTrackButton("Track 1", Color.BLUE);
        track1Btn.setOnAction(e -> trackSelectionHandler.accept(GameTrack.createTrack1()));

        // Track 2 button with red indicator
        Button track2Btn = createTrackButton("Track 2", Color.RED);
        track2Btn.setOnAction(e -> trackSelectionHandler.accept(GameTrack.createTrack2()));

        // Track 3 button with green indicator
        Button track3Btn = createTrackButton("Track 3", Color.GREEN);
        track3Btn.setOnAction(e -> trackSelectionHandler.accept(GameTrack.createTrack3()));

        // Start game button
        Button startGameBtn = new Button("Start Game");
        startGameBtn.setPrefWidth(200);
        startGameBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10;");
        startGameBtn.setOnAction(e -> {
            System.out.println("Start Game button clicked");
        });

        // Track information section
        VBox trackInfoBox = createTrackInfoSection();

        // Add all components to the panel
        trackButtons.getChildren().addAll(track1Btn, track2Btn, track3Btn);
        getChildren().addAll(titleLabel, trackButtons, startGameBtn, trackInfoBox);
    }

    private Button createTrackButton(String name, Color color) {
        Button button = new Button();
        button.setPrefWidth(200);
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

    private VBox createTrackInfoSection() {
        VBox trackInfoBox = new VBox(10);
        trackInfoBox.setPadding(new Insets(20, 0, 0, 0));
        trackInfoBox.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1 0 0 0; -fx-padding: 15 0 0 0;");

        Label infoTitleLabel = new Label("Track Information");
        infoTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Text trackInfoText = new Text(
                "Track 1: Classic path \n\n" +
                        "Track 2: Meduim \n\n" +
                        "Track 3: Expert"
        );
        trackInfoText.setWrappingWidth(210);

        trackInfoBox.getChildren().addAll(infoTitleLabel, trackInfoText);
        return trackInfoBox;
    }
}