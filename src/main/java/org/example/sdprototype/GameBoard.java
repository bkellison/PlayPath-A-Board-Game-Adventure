package org.example.sdprototype;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameBoard extends Application {

    private BoardSpace[][] boardSpaces;
    private Pane trackDisplayPane;
    private GameTrack selectedTrack = null;

    @Override
    public void start(Stage primaryStage) {
        BorderPane mainLayout = new BorderPane();

        // Create game board and tracks
        GridPane boardGrid = createGameBoard();
        trackDisplayPane = new Pane();

        // Create selection panel
        TrackSelectionPanel selectionPanel = new TrackSelectionPanel(this::selectTrack);

        // Stack the track display pane over the board grid
        StackPane gameAreaStack = new StackPane();
        gameAreaStack.getChildren().addAll(boardGrid, trackDisplayPane);

        // Add components to main layout
        mainLayout.setCenter(gameAreaStack);
        mainLayout.setRight(selectionPanel);

        // Create scene and show
        Scene scene = new Scene(mainLayout, 900, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Track Selection");
        primaryStage.show();
    }

    private GridPane createGameBoard() {
        GridPane boardGrid = new GridPane();
        boardGrid.setPadding(new Insets(20));
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);

        // Create board spaces
        boardSpaces = new BoardSpace[5][8];
        boolean[][] trackPattern = createTrackPattern();

        // Configure grid constraints
        for (int i = 0; i < 5; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / 5);
            boardGrid.getRowConstraints().add(rowConstraints);
        }

        for (int i = 0; i < 8; i++) {
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / 8);
            boardGrid.getColumnConstraints().add(colConstraints);
        }

        // Create board spaces based on the track pattern
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                int trackNumber = getTrackForSpace(row, col);
                BoardSpace space = new BoardSpace(row, col, trackNumber, trackPattern[row][col]);
                space.setPrefSize(80, 80);
                boardSpaces[row][col] = space;

                // Add space number for reference
                int spaceNumber = getSpaceNumber(row, col);
                Text spaceNumberText = new Text(String.valueOf(spaceNumber));
                spaceNumberText.setStyle("-fx-font-size: 12px;");

                StackPane spaceWithNumber = new StackPane(space, spaceNumberText);
                boardGrid.add(spaceWithNumber, col, row);
            }
        }

        return boardGrid;
    }

    private boolean[][] createTrackPattern() {
        boolean[][] track = new boolean[5][8];

        // First row (left to right)
        for (int col = 0; col < 8; col++) {
            track[0][col] = true;
        }

        // Second row (right to left)
        for (int col = 7; col >= 0; col--) {
            track[1][col] = true;
        }

        // Third row (left to right)
        for (int col = 0; col < 8; col++) {
            track[2][col] = true;
        }

        // Fourth row (right to left)
        for (int col = 7; col >= 0; col--) {
            track[3][col] = true;
        }

        // Fifth row (left to right)
        for (int col = 0; col < 8; col++) {
            track[4][col] = true;
        }

        return track;
    }

    private int getSpaceNumber(int row, int col) {
        if (row % 2 == 0) {
            // Left to right rows
            return row * 8 + col + 1;
        } else {
            // Right to left rows
            return row * 8 + (8 - col);
        }
    }

    public void selectTrack(GameTrack track) {
        trackDisplayPane.getChildren().clear();

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                boardSpaces[row][col].setDefaultColor();
            }
        }

        selectedTrack = track;

        for (int[] position : track.getTrackPositions()) {
            int row = position[0];
            int col = position[1];
            if (row >= 0 && row < 5 && col >= 0 && col < 8) {
                boardSpaces[row][col].setHighlighted(true, track.getColor());
            }
        }

        System.out.println("Selected: " + track.getName());
    }

    private int getTrackForSpace(int row, int col) {
        if (row == 0 || row == 2 || row == 4) {
            return 1; // Assign these rows to Track 1
        } else if (row == 1) {
            return 2; // Assign row 1 to Track 2
        } else if (row == 3) {
            return 3; // Assign row 3 to Track 3
        }
        return 0; // Default: No track
    }

    public static void main(String[] args) {
        launch(args);
    }
}