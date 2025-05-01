package org.example.sdprototype.GameLogic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.control.Label;
import org.example.sdprototype.Utilities.SoundManager;

import java.util.Random;

public class DiceAnimator {
    private final StackPane dicePane;
    private final Text resultText;
    private ImageView diceImageView;
    private final Random random;
    private Image[] diceImages;
    private int finalResult;
    private Runnable onRollComplete;

    public DiceAnimator(StackPane dicePane, Text resultText) {
        this.dicePane = dicePane;
        this.resultText = resultText;
        this.random = new Random();
        this.diceImages = new Image[6]; // Initialize the array
        this.diceImageView = new ImageView(); // Initialize the image view

        try {
            // Load dice images (assuming you have dice images from 1-6 in resources folder)
            for (int i = 0; i < 6; i++) {
                diceImages[i] = new Image(getClass().getResourceAsStream("/org/example/sdprototype/images/dice/dice" + (i + 1) + ".png"));
            }

            // Configure the ImageView
            diceImageView.setFitWidth(100);
            diceImageView.setFitHeight(100);
            diceImageView.setPreserveRatio(true);

            // Clear the dice pane and add the image view
            dicePane.getChildren().clear();
            dicePane.getChildren().add(diceImageView);
        } catch (Exception e) {
            System.err.println("Error loading dice images: " + e.getMessage());
            // Fallback to emoji dice
            Label diceFallback = new Label("🎲");
            diceFallback.setStyle("-fx-font-size: 80px;");
            dicePane.getChildren().clear();
            dicePane.getChildren().add(diceFallback);
        }
    }

    public void rollDice(Runnable onComplete) {
        this.onRollComplete = onComplete;

        // Generate the final result (1-6)
        finalResult = random.nextInt(6) + 1;

        // Update text to show rolling
        resultText.setText("Rolling...");

        // Play dice roll sound
        SoundManager.playSound("/org/example/sdprototype/sounds/dice" + (random.nextInt(2) + 1) + ".wav");

        // Create animation timeline
        Timeline timeline = new Timeline();

        // Add frames for dice animation (10 frames, changing rapidly)
        for (int i = 0; i < 10; i++) {
            final int frameIndex = i;
            KeyFrame frame = new KeyFrame(
                    Duration.millis(100 * i),
                    event -> {
                        // During animation, show random dice faces
                        int randomFace = random.nextInt(6);
                        try {
                            diceImageView.setImage(diceImages[randomFace]);
                        } catch (Exception e) {
                            System.err.println("Error updating dice image: " + e.getMessage());
                        }

                        // On last frame, show actual result
                        if (frameIndex == 9) {
                            showFinalResult();
                        }
                    }
            );
            timeline.getKeyFrames().add(frame);
        }

        // Start the animation
        timeline.play();
    }

    private void showFinalResult() {
        try {
            // Show the final dice face
            diceImageView.setImage(diceImages[finalResult - 1]);
        } catch (Exception e) {
            System.err.println("Error showing final result: " + e.getMessage());
        }

        // Update the result text
        resultText.setText("Rolled: " + finalResult);

        // Call the completion handler
        if (onRollComplete != null) {
            onRollComplete.run();
        }
    }

    public int getFinalResult() {
        return finalResult;
    }
}