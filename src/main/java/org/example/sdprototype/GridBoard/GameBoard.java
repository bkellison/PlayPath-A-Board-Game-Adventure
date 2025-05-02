package org.example.sdprototype.GridBoard;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.sdprototype.Controllers.GameController;
import org.example.sdprototype.Controllers.BoardUIController;

import java.io.IOException;
import java.util.*;

public class GameBoard {
    private BoardGrid boardGrid;
    private BoardUIController boardUIController;
    private GameController gameController;
    private Stage mainStage;
    private GameTrack selectedTrack;

    public GameBoard() {
    }

    public void setTrack(GameTrack track) {
        this.selectedTrack = track;
    }

    public void init(Stage primaryStage) {
        this.mainStage = primaryStage;

        try {
            BorderPane mainLayout = new BorderPane();
            mainLayout.prefWidthProperty().bind(primaryStage.widthProperty());
            mainLayout.prefHeightProperty().bind(primaryStage.heightProperty());

            boardGrid = new BoardGrid();
            GridPane grid = boardGrid.createGameBoard();
            gameController = new GameController(boardGrid, selectedTrack);

            String fxmlPath = "/BoardUI.fxml";

            if (selectedTrack != null) {
                String trackName = selectedTrack.getName();
                if (Objects.equals(trackName, "Track 1")) {
                    fxmlPath = "/BoardUI_Shrek.fxml";
                } else if (Objects.equals(trackName, "Track 2")) {
                    fxmlPath = "/BoardUI_Rainforest.fxml";
                } else if (Objects.equals(trackName, "Track 3")) {
                    fxmlPath = "/BoardUI_Pirate.fxml";
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent boardUIRoot = loader.load();
            Object controller = loader.getController();

            if (controller instanceof BoardUIController) {
                boardUIController = (BoardUIController) controller;
                boardUIController.setGameController(gameController);
                boardUIController.setSelectedTrack(selectedTrack);

                gameController.setRollDiceButton(boardUIController.getRollDiceButton());
                gameController.setDiceResultText(boardUIController.getDiceResultText());

                boardUIController.manuallyInitializeDiceAnimator();
            } else {
                throw new ClassCastException("Expected BoardUIController but got " +
                        (controller != null ? controller.getClass().getName() : "null"));
            }

            StackPane gameAreaStack = new StackPane();

            // Load only the background image
            String backgroundImagePath = getBackgroundImagePath();
            Image backgroundImage = new Image(String.valueOf(getClass().getResource(backgroundImagePath)));
            ImageView backgroundImageView = new ImageView(backgroundImage);

            backgroundImageView.setPreserveRatio(true);
            backgroundImageView.fitWidthProperty().bind(mainLayout.widthProperty());
            backgroundImageView.fitHeightProperty().bind(mainLayout.heightProperty());

            gameAreaStack.getChildren().add(backgroundImageView);
            gameAreaStack.getChildren().addAll(grid, gameController.getTrackDisplayPane());

            mainLayout.setCenter(gameAreaStack);
            mainLayout.setRight(boardUIRoot);
            boardGrid.highlightTrack(selectedTrack);

            gameController.initializePlayer();

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

    // Simplified method that only returns the background image path
    private String getBackgroundImagePath() {
        String trackName = selectedTrack.getName();

        if (Objects.equals(trackName, "Track 1")) {
            System.out.println("Loading Shrek theme background");
            return "/org/example/sdprototype/images/Theme1/board1.png";
        }
        else if (Objects.equals(trackName, "Track 2")) {
            System.out.println("Loading Rainforest theme background");
            return "/org/example/sdprototype/images/Theme2/Board2.png";
        }
        else if (Objects.equals(trackName, "Track 3")) {
            System.out.println("Loading Pirate theme background");
            return "/org/example/sdprototype/images/Theme3/Board3.png";
        }

        // Default background if track name doesn't match
        return "/org/example/sdprototype/images/Theme1/board1.png";
    }
}