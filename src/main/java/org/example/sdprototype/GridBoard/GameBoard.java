package org.example.sdprototype.GridBoard;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
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

    // Arrays holding images for each theme - all commented out except for board1
    private final String[] theme1 = {
            // "/org/example/sdprototype/images/Theme1/Donkey.png",
            null,
            null,
            null,
            null,
            // "/org/example/sdprototype/images/Theme1/BewareOfOgreSign.png",
            null,
            null,
            null,
            null,
            null,
            null,
            // "/org/example/sdprototype/images/Theme1/SignPost.png",
            null,
            // "/org/example/sdprototype/images/Theme1/DulocSign.png",
            null,
    };

    private final String[] theme2 = {
            // "/org/example/sdprototype/images/Theme2/--.png",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
    };

    private final String[] theme3 = {
            // "/org/example/sdprototype/images/Theme3/--.png",
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
    };

    // Hash map to map special spaces to their images for each theme, to be set in constructor
    private final Map<String, String> specialSpacesTheme1 = new HashMap<>();
    private final Map<String, String> specialSpacesTheme2 = new HashMap<>();
    private final Map<String, String> specialSpacesTheme3 = new HashMap<>();

    // Arrays for holding special squares for each theme
    private final String[] specialSpaceCoordsTheme1 = {
            "1,0",
            "3,2",
            "4,0",
            "4,5",
            "0,7",
            "4,7",
    };

    private final String[] specialSpaceCoordsTheme2 = {};
    private final String[] specialSpaceCoordsTheme3 = {};

    // Arrays for holding image paths corresponding to special squares - all commented out
    private final String[] specialImagesTheme1 = {
            // "/org/example/sdprototype/images/Theme1/LordFarquaad.png",
            null,
            // "/org/example/sdprototype/images/Theme1/Fiona.png",
            null,
            // "/org/example/sdprototype/images/Theme1/DulocTower.png",
            null,
            // "/org/example/sdprototype/images/Theme1/Dragon.png",
            null,
            // "/org/example/sdprototype/images/Theme1/GingerbreadMan.png",
            null,
            // "/org/example/sdprototype/images/Theme1/Shrek.png",
            null,
    };

    private final String[] specialImagesTheme2 = {};
    private final String[] specialImagesTheme3 = {};

    public GameBoard() {
        // Default constructor
        // Populate hash maps for special squares
        // Theme 1:
        for (int i = 0; i < specialSpaceCoordsTheme1.length; i++) {
            specialSpacesTheme1.put(specialSpaceCoordsTheme1[i], specialImagesTheme1[i]);
        }

        // Theme 2:
        for (int i = 0; i < specialSpaceCoordsTheme2.length; i++) {
            specialSpacesTheme2.put(specialSpaceCoordsTheme2[i], specialImagesTheme2[i]);
        }

        // Theme 3:
        for (int i = 0; i < specialSpaceCoordsTheme3.length; i++) {
            specialSpacesTheme3.put(specialSpaceCoordsTheme3[i], specialImagesTheme3[i]);
        }
    }

    public void setTrack(GameTrack track) {
        this.selectedTrack = track;
    }

    public void init(Stage primaryStage) {
        this.mainStage = primaryStage;

        try {
            // Create the main game layout
            BorderPane mainLayout = new BorderPane();
            // Make the BorderPane resize with the stage
            mainLayout.prefWidthProperty().bind(primaryStage.widthProperty());
            mainLayout.prefHeightProperty().bind(primaryStage.heightProperty());

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

            // Load theme images and get path to the background image
            String backgroundImagePath = loadImages();
            Image backgroundImage = new Image(String.valueOf(getClass().getResource(backgroundImagePath)));
            ImageView backgroundImageView = new ImageView(backgroundImage);

            // Make the image view resize with the container
            backgroundImageView.setPreserveRatio(true);
            backgroundImageView.fitWidthProperty().bind(mainLayout.widthProperty());
            backgroundImageView.fitHeightProperty().bind(mainLayout.heightProperty());

            // Add background image to the stack pane
            gameAreaStack.getChildren().add(backgroundImageView);

            // Add grid and track display above background
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

    // Helper function: Load images into non-track spaces and return background image path
    private String loadImages() {
        String[] imagesArray = new String[]{};
        String backgroundImagePath = "";
        Map<String, String> specialSpaces = new HashMap<>();
        String trackName = selectedTrack.getName();

        System.out.println("Selected: " + trackName);
        if (Objects.equals(trackName, "Track 1")) {
            System.out.print("Loading theme 1 images");
            imagesArray = theme1;
            // Keep only the board1 background image path
            backgroundImagePath = "/org/example/sdprototype/images/Theme1/board1.png";
            specialSpaces = specialSpacesTheme1;
        }
        else if (Objects.equals(trackName, "Track 2")) {
            System.out.println("Loading theme 2 images");
            imagesArray = theme2;
            // Comment out other background images
            backgroundImagePath = "/org/example/sdprototype/images/Theme2/Board2.png";
            specialSpaces = specialSpacesTheme2;
        }
        else if (Objects.equals(trackName, "Track 3")) {
            System.out.println("Loading theme 3 images");
            imagesArray = theme3;
            backgroundImagePath = "/org/example/sdprototype/images/Theme3/Board3.png";
            specialSpaces = specialSpacesTheme3;
        }

        // Get all track positions
        List<int[]> trackPositions = selectedTrack.getTrackPositions();

        // Convert this to a set for easier checking if a space is part of the active track
        Set<String> trackPositionSet = new HashSet<>();
        for (int[] pos : trackPositions) {
            trackPositionSet.add(pos[0] + "," + pos[1]); // Store "row,col" as a String
        }

        int imageIndex = 0;

        // Loop through each space on the board
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 8; col++) {
                String key = row + "," + col;

                // If the current space is NOT a track space, load an image
                if (!trackPositionSet.contains(key)) {
                    // Get image path, wrapping around using % if run out of images
                    String imagePath = imagesArray[imageIndex % imagesArray.length];

                    // Get board space
                    BoardSpace space = boardGrid.getBoardSpace(row, col);

                    if (imagePath != null) {
                        // Load image into that space
                        if (space != null) {
                            space.addImageToSpace(imagePath, false);
                        } else {
                            System.out.println("Space at (" + row + "," + col + ") is null!");
                        }
                    }
                    else {
                        space.addImageToSpace("Remove", false);
                    }

                    imageIndex++;

                }
                /* Comment out special space image loading
                else if (specialSpaces.containsKey(key)) {
                    // This means this space is a special space, and therefore needs to load in an image
                    BoardSpace space = boardGrid.getBoardSpace(row, col);
                    space.addImageToSpace(specialSpaces.get(key), true);

                    // Additionally, set this space's isSpecial field to true
                    space.setSpecial(true);
                }
                */
            }
        }

        // Return the background image path
        return backgroundImagePath;
    }
}