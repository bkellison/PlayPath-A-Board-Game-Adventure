package org.example.sdprototype.GameLogic;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.Random;

public class DiceAnimator {
    private final StackPane dicePane;
    private final Text resultText;
    private final ImageView diceImageView;
    private final Random random;
    private final Image[] diceImages;
    private int finalResult;
    private Runnable onRollComplete;

    public DiceAnimator(StackPane dicePane, Text resultText) {
        this.dicePane = dicePane;
        this.resultText = resultText;
        this.random = new Random();

        // Load dice images (assuming you have dice images from 1-6 in resources folder)
        this.diceImages = new Image[6];
        for (int i = 0; i < 6; i++) {
            diceImages[i] = new Image(getClass().getResourceAsStream("/org/example/sdprototype/images/dice/dice" + (i + 1) + ".png"));
        }

        // Create and configure the ImageView
        diceImageView = new ImageView();
        diceImageView.setFitWidth(100);
        diceImageView.setFitHeight(100);
        diceImageView.setPreserveRatio(true);

        // Clear the dice pane and add the image view
        dicePane.getChildren().clear();
        dicePane.getChildren().add(diceImageView);
    }

    public void rollDice(Runnable onComplete) {
        this.onRollComplete = onComplete;

        // Generate the final result (1-6)
        finalResult = random.nextInt(6) + 1;

        // Update text to show rolling
        resultText.setText("Rolling...");

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
                        diceImageView.setImage(diceImages[randomFace]);

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
        // Show the final dice face
        diceImageView.setImage(diceImages[finalResult - 1]);

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