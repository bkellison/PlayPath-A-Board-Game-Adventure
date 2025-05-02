package org.example.sdprototype.GridBoard;

import javafx.geometry.Insets;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BoardGrid {
    private BoardSpace[][] boardSpaces;

    public BoardGrid() {
        // Initialize board spaces
        boardSpaces = new BoardSpace[5][8];
    }

    public GridPane createGameBoard() {
        GridPane boardGrid = new GridPane();
        boardGrid.setPadding(new Insets(20));
        boardGrid.setHgap(5);
        boardGrid.setVgap(5);

        // Make grid transparent to allow background image to show through
        boardGrid.setStyle("-fx-background-color: transparent;");

        // Create board spaces
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

                StackPane spaceWithNumber = new StackPane(space);
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

    public void highlightTrack(GameTrack track) {
        // Reset all spaces to default
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                boardSpaces[row][col].setDefaultColor();
            }
        }

        if (track != null) {
            for (int[] position : track.getTrackPositions()) {
                int row = position[0];
                int col = position[1];
                if (row >= 0 && row < 5 && col >= 0 && col < 8) {
                    // For Track 1, use completely transparent highlighting
                    if (track.getName().equals("Track 1")) {
                        // Make Track 1 completely invisible
                        boardSpaces[row][col].setOpacity(0);
                        boardSpaces[row][col].setHighlighted(true, Color.TRANSPARENT);
                    } else {
                        boardSpaces[row][col].setHighlighted(true, track.getColor());
                    }
                }
            }
        }
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

    public BoardSpace[][] getBoardSpaces() {
        return boardSpaces;
    }

    public BoardSpace getBoardSpace(int row, int col) {
        if (row >= 0 && row < boardSpaces.length && col >= 0 && col < boardSpaces[0].length) {
            return boardSpaces[row][col];
        }
        return null;
    }
}