package org.example.sdprototype.GridBoard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.sdprototype.Controllers.GameController;
import org.example.sdprototype.Controllers.BoardUIController;

import java.io.IOException;

public class GameBoard {
    private BoardGrid boardGrid;
    private BoardUIController boardUIController;
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

        try {
            // Create the main game layout
            BorderPane mainLayout = new BorderPane();

            // Initialize
            boardGrid = new BoardGrid();
            GridPane grid = boardGrid.createGameBoard();
            gameController = new GameController(boardGrid, selectedTrack);


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BoardUI.fxml"));
            Parent boardUIRoot = loader.load();
            Object controller = loader.getController();

            if (controller instanceof BoardUIController) {
                boardUIController = (BoardUIController) controller;
                boardUIController.setGameController(gameController);
                boardUIController.setSelectedTrack(selectedTrack);

                // Connect the controller with the game controller
                gameController.setRollDiceButton(boardUIController.getRollDiceButton());
            } else {
                throw new ClassCastException("Expected BoardUIController but got " +
                        (controller != null ? controller.getClass().getName() : "null"));
            }

            // Stack the track display pane over the board grid
            StackPane gameAreaStack = new StackPane();
            gameAreaStack.getChildren().addAll(grid, gameController.getTrackDisplayPane());

            // Add components to main layout
            mainLayout.setCenter(gameAreaStack);
            mainLayout.setRight(boardUIRoot);
            boardGrid.highlightTrack(selectedTrack);

            // Initialize player on first space
            gameController.initializePlayer();

            // Create scene and show
            Scene scene = new Scene(mainLayout, 900, 650);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Board Game");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}