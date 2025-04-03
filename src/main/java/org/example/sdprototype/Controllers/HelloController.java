package org.example.sdprototype.Controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.example.sdprototype.GameLogic.Card;

import java.util.Random;

public class HelloController {

    // Field that stores the coordinates of the game board
    // 26 spaces, so 26 coordinates
    private static final int[][] COORDINATES = {
            {123, 720},
            {123, 590},
            {123, 460},
            {123, 320},
            {123, 200},
            {160, 90},
            {270, 80},
            {380, 100},
            {385, 240},
            {385, 370},
            {385, 500},
            {385, 640},
            {470, 730},
            {600, 725},
            {645, 600},
            {645, 475},
            {645, 345},
            {645, 215},
            {665, 100},
            {775, 75},
            {885, 95},
            {907, 200},
            {907, 320},
            {907, 450},
            {907, 580},
            {907, 710}
    };

    @FXML
    private ImageView characterImage;
    @FXML
    private AnchorPane background;

    private int currentPosition;
    //-----
    private boolean skipTurn;
    private Random random;
    private CardController cardController;
    //-----
    public HelloController() {
        currentPosition = 0;
        //------
        skipTurn = false;
        random = new Random();
        cardController = new CardController();
        //------
    }

    @FXML
    public void initialize() {
        // Event handling: any key press or mouse click will move the character
        //characterImage.setOnMouseClicked(this::moveCharacter);
        background.setOnMouseClicked(this::moveCharacter);

        // Add key event listener once the scene is set
        // This ensures we don't call getScene() before the scene is assigned
        Scene scene = characterImage.getScene();
        if (scene != null) {
            scene.setOnKeyPressed(this::moveCharacter);
        }
    }

    /* Getters for characterImage, background, and currentPosition, */
    public ImageView getCharacterImage() { return characterImage; }
    public AnchorPane getBackground() { return background; }
    public int getCurrentPosition() { return currentPosition; }

    /* Setters for characterImage, background, and currentPosition, */
    public void setCharacterImage(ImageView characterImage) { this.characterImage = characterImage; }
    public void setBackground(AnchorPane background) { this.background = background; }
    public void setCurrentPosition(int currentPosition) { this.currentPosition = currentPosition; }

    // Method to move the character to the next position on a mouse event
    public void moveCharacter(MouseEvent event) {
        // Check if there are more positions to move to
        //------
        if (!skipTurn) {
            checkPosition();
        } else {
            skipTurn = false; // Reset skip turn after skipping
        }
        //-----
    }

    // Method to move the character to the next position on a key event
    public void moveCharacter(KeyEvent event) {
        // Check if there are more positions to move to
        //------
        if (!skipTurn) {
            checkPosition();
        } else {
            skipTurn = false;
        }
        //-----
    }

    public void checkPosition() {
        if (currentPosition < COORDINATES.length - 1) {
            // If yes, set our position to next space
            currentPosition++;
        }
        else {
            // If not, go back to the beginning
            currentPosition = 0;
        }

        // Now move the character accordingly
        characterImage.setLayoutX(COORDINATES[currentPosition][0]);
        characterImage.setLayoutY(COORDINATES[currentPosition][1]);

        //-----
        // Check if the new position requires drawing a card
        if (isSpecialSpace(currentPosition)) {
            Card card = cardController.drawCard();
            card.applyEffect(this);
        }
        //-----
    }

    //------
    private boolean isSpecialSpace(int position) {
        return position % 5 == 0; // Example: Special spaces every 5 tiles
    }

    public void setSkipTurn(boolean skip) {
        this.skipTurn = skip;
    }

    public void showQuiz() {}

    public void moveForward(int spaces) {
        currentPosition += spaces;
        if (currentPosition >= COORDINATES.length) {
            currentPosition = COORDINATES.length - 1; // Prevent out-of-bounds
        }
    }
    //------

}