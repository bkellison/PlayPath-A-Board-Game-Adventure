package org.example.sdprototype.GridBoard;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.sdprototype.Controllers.GameController;
import org.example.sdprototype.UI.BoardUI;

public class GameBoard {
    private BoardGrid boardGrid;
    private BoardUI boardUI;
    private GameController gameController;
    private Stage mainStage;
    private GameTrack selectedTrack;

    public GameBoard() {
        // Default constructor
    }

    public void setTrack(GameTrack track) {
        this.selectedTrack = track;
    }

    public void init(Stage primaryStage) {
        this.mainStage = primaryStage;

        // Create the main game layout
        BorderPane mainLayout = new BorderPane();

        // Initialize
        boardGrid = new BoardGrid();
        GridPane grid = boardGrid.createGameBoard();
        gameController = new GameController(boardGrid, selectedTrack);
        boardUI = new BoardUI(gameController, selectedTrack);

        // Stack the track display pane over the board grid
        StackPane gameAreaStack = new StackPane();
        gameAreaStack.getChildren().addAll(grid, gameController.getTrackDisplayPane());

        // Add components to main layout
        mainLayout.setCenter(gameAreaStack);
        mainLayout.setRight(boardUI.createControlPanel());
        boardGrid.highlightTrack(selectedTrack);

        // Initialize player on first space
        gameController.initializePlayer();

        // Create scene and show
        Scene scene = new Scene(mainLayout, 900, 650);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Board Game");
        primaryStage.show();
    }

}