package org.example.sdprototype.GridBoard;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sdprototype.GameLogic.Player;
import org.example.sdprototype.UI.TrackSelectionScreen;
import org.example.sdprototype.UI.WelcomeScreen;

public class GameBoard extends Application {

    private BoardSpace[][] boardSpaces;
    private Pane trackDisplayPane;
    private GameTrack selectedTrack = null;
    private Player currentPlayer;
    private int[] playerPosition = {0, 0}; // Start position (row, col)
    private Circle playerToken;
    private Stage mainStage;

    @Override
    public void start(Stage primaryStage) {
        // For standalone testing
        this.mainStage = primaryStage;

        // Default to Track 1 if no track is selected when running directly
        if (selectedTrack == null) {
            selectedTrack = GameTrack.createTrack1();
        }

        init(primaryStage);
    }

    public void setTrack(GameTrack track) {
        this.selectedTrack = track;
    }

    public void init(Stage primaryStage) {
        this.mainStage = primaryStage;

        // Create the main game layout
        BorderPane mainLayout = new BorderPane();

        // Create game board
        GridPane boardGrid = createGameBoard();
        trackDisplayPane = new Pane();

        // Create player token
        playerToken = new Circle(15, Color.YELLOW);
        playerToken.setStroke(Color.BLACK);
        playerToken.setStrokeWidth(2);

        // Add the player token to the track display pane
        trackDisplayPane.getChildren().add(playerToken);

        // Initialize player position
        updatePlayerPosition(0, 0);

        // Initialize player
        currentPlayer = new Player(1, playerPosition, true);

        // Create control panel (for rolling dice, etc.)
        VBox controlPanel = createControlPanel();

        // Stack the track display pane over the board grid
        StackPane gameAreaStack = new StackPane();
        gameAreaStack.getChildren().addAll(boardGrid, trackDisplayPane);

        // Add components to main layout
        mainLayout.setCenter(gameAreaStack);
        mainLayout.setRight(controlPanel);

        // Apply track highlighting
        highlightSelectedTrack();

        // Create scene and show
        Scene scene = new Scene(mainLayout, 900, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Board Game");
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

    private VBox createControlPanel() {
        VBox controlPanel = new VBox(20);
        controlPanel.setPadding(new Insets(20));
        controlPanel.setAlignment(Pos.TOP_CENTER);
        controlPanel.setPrefWidth(200);
        controlPanel.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #cccccc; -fx-border-width: 0 0 0 1;");

        // Add roll dice button
        Button rollDiceButton = new Button("Roll Dice");
        rollDiceButton.setPrefWidth(160);
        rollDiceButton.setPrefHeight(40);
        rollDiceButton.setStyle("-fx-font-size: 14px;");

        rollDiceButton.setOnAction(e -> {
            // Simulate rolling dice (1-6)
            int diceRoll = (int)(Math.random() * 6) + 1;

            // Move player based on dice roll
            movePlayer(diceRoll);
        });

        // Add track info
        Text trackInfoLabel = new Text("Current Track: " + selectedTrack.getName());
        trackInfoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // Add player info
        Text playerInfoLabel = new Text("Player 1");
        playerInfoLabel.setFont(Font.font("Arial", 14));

        controlPanel.getChildren().addAll(trackInfoLabel, playerInfoLabel, rollDiceButton);

        return controlPanel;
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

    private void highlightSelectedTrack() {
        // Reset all spaces to default
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                boardSpaces[row][col].setDefaultColor();
            }
        }

        // Highlight the selected track
        if (selectedTrack != null) {
            for (int[] position : selectedTrack.getTrackPositions()) {
                int row = position[0];
                int col = position[1];
                if (row >= 0 && row < 5 && col >= 0 && col < 8) {
                    boardSpaces[row][col].setHighlighted(true, selectedTrack.getColor());
                }
            }
        }
    }

    private void movePlayer(int steps) {
        // Get current track positions
        java.util.List<int[]> trackPositions = selectedTrack.getTrackPositions();

        // Find current position index in the track
        int currentIndex = -1;
        for (int i = 0; i < trackPositions.size(); i++) {
            int[] pos = trackPositions.get(i);
            if (pos[0] == playerPosition[0] && pos[1] == playerPosition[1]) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex != -1) {
            // Calculate new position index
            int newIndex = currentIndex + steps;

            // Check if player has reached or passed the end
            if (newIndex >= trackPositions.size()) {
                showWinnerScreen();
                return;
            }

            // Update player position
            int[] newPosition = trackPositions.get(newIndex);
            updatePlayerPosition(newPosition[0], newPosition[1]);
        }
    }

    private void updatePlayerPosition(int row, int col) {
        playerPosition[0] = row;
        playerPosition[1] = col;

        // Update player token position
        if (row < 5 && col < 8 && boardSpaces[row][col] != null) {
            double spaceWidth = boardSpaces[row][col].getWidth();
            double spaceHeight = boardSpaces[row][col].getHeight();

            double tokenX = boardSpaces[row][col].getLayoutX() + spaceWidth / 2;
            double tokenY = boardSpaces[row][col].getLayoutY() + spaceHeight / 2;

            playerToken.setCenterX(tokenX);
            playerToken.setCenterY(tokenY);
        }
    }

    private void showWinnerScreen() {
        // Create the winner screen
        BorderPane winnerLayout = new BorderPane();
        winnerLayout.setStyle("-fx-background-color: #f8f9fa;");

        VBox contentBox = new VBox(30);
        contentBox.setAlignment(Pos.CENTER);

        Text congratsText = new Text("Congratulations!");
        congratsText.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        congratsText.setFill(Color.GREEN);

        Text winnerText = new Text("Player 1 has won the game!");
        winnerText.setFont(Font.font("Arial", 24));

        Button playAgainButton = new Button("Play Again");
        playAgainButton.setPrefWidth(200);
        playAgainButton.setPrefHeight(50);
        playAgainButton.setFont(Font.font("Arial", 16));
        playAgainButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setPrefWidth(200);
        mainMenuButton.setPrefHeight(50);
        mainMenuButton.setFont(Font.font("Arial", 16));

        contentBox.getChildren().addAll(congratsText, winnerText, playAgainButton, mainMenuButton);
        winnerLayout.setCenter(contentBox);

        Scene winnerScene = new Scene(winnerLayout, 800, 600);
        mainStage.setScene(winnerScene);

        // Set button actions
        playAgainButton.setOnAction(e -> {
            TrackSelectionScreen trackScreen = new TrackSelectionScreen(mainStage);
            mainStage.setScene(trackScreen.getScene());
        });

        mainMenuButton.setOnAction(e -> {
            WelcomeScreen welcomeScreen = new WelcomeScreen(mainStage);
            mainStage.setScene(welcomeScreen.getScene());
        });
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
}