package org.example.sdprototype.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.sdprototype.GameLogic.Player;
import org.example.sdprototype.GridBoard.BoardGrid;
import org.example.sdprototype.GridBoard.BoardSpace;
import org.example.sdprototype.GridBoard.GameTrack;
import org.example.sdprototype.Communicator.ArduinoConnector;
import org.example.sdprototype.Utilities.SoundManager;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class GameController {
    private BoardGrid boardGrid;
    private GameTrack selectedTrack;
    private Player currentPlayer;
    private int currentTrackIndex = 0;
    private Node playerToken;
    private Pane trackDisplayPane;
    private boolean animationInProgress = false;
    private Button rollDiceButton;
    private Text diceResultText;
    private int[] specialIdx;
    private int[] specialActions;
    private String[] messages;
    private String specialMessage = null;

    private final int[] specialIdx1 = { 6, 8, 13, 20 };
    private final int[] specialIdx2 = { 1, 3, 6, 9, 13};
    private final int[] specialIdx3 = { 3, 6, 10, 13, 16 };

    private final int[] specialActions1 = { -1, 1, 3, -2 };
    private final int[] specialActions2 = {  1, 1, -1, 1, -1};
    private final int[] specialActions3 = { -2, 2, -1, -1, -1 };

    private final String[] specialMessage1 = {
            "Oh no! Lord Farquaad sents his knights after you — move back one space.",
            "Fiona shows you a shortcut — move up one space!",
            "Dragon swoops in and gives you a lift — fly forward three spaces!",
            "Not the gumdrop buttons! The Gingerbread Man lost a leg! — move back two spaces."
    };

    private final String[] specialMessage2 = {
            "Ribbit! A mischievous frog startles you — leap forward one space!",
            "Hisss! A sly snake slithers into your path — move forward one space!",
            "Squawk! A chatty parrot distracts you with gossip — move back one space!",
            "Caw! A nosy toucan pecks at your supplies — move forward one space!",
            "Ooh ooh aah aah! A playful monkey steals your hat — swing back one space to chase it!"
    };

    private final String[] specialMessage3 = {
            "Arrr! A fearsome pirate blocks your path and makes you move back two spaces!",
            "A friendly crab gives you a boost — move forward two spaces!",
            "A sneaky pirate jumps out and stops you — move back one space.",
            "BOOM! A nearby ship fires a cannonball across your bow — retreat back one space!",
            "A massive whale surfaces in your path — you're forced to move back one space to avoid it!"
    };

    public GameController(BoardGrid boardGrid, GameTrack track) {
        this.boardGrid = boardGrid;
        this.selectedTrack = track;

        String trackName = selectedTrack.getName();
        if (Objects.equals(trackName, "Track 1")) {
            this.specialIdx = specialIdx1;
            this.specialActions = specialActions1;
            this.messages = specialMessage1;
        }
        else if (Objects.equals(trackName, "Track 2")) {
            this.specialIdx = specialIdx2;
            this.specialActions = specialActions2;
            this.messages = specialMessage2;
        }
        else if (Objects.equals(trackName, "Track 3")) {
            this.specialIdx = specialIdx3;
            this.specialActions = specialActions3;
            this.messages = specialMessage3;
        }

        trackDisplayPane = new Pane();
        playerToken = createThemedPlayerToken();
        trackDisplayPane.getChildren().add(playerToken);
    }

    private Node createThemedPlayerToken() {
        String trackName = selectedTrack.getName();
        StackPane token = new StackPane();
        token.setPrefSize(40, 40);

        Circle fallbackCircle = new Circle(20);
        Text symbolText = new Text();
        symbolText.setFont(Font.font("Arial", 24));

        if (Objects.equals(trackName, "Track 1")) {
            fallbackCircle.setFill(Color.web("#7c9f45", 0.8));
            symbolText.setText("🧌");
        } else if (Objects.equals(trackName, "Track 2")) {
            fallbackCircle.setFill(Color.web("#1a936f", 0.8));
            symbolText.setText("🐒");
        } else {
            fallbackCircle.setFill(Color.web("#c99767", 0.8));
            symbolText.setText("🏴‍☠️");
        }

        fallbackCircle.setStroke(Color.BLACK);
        fallbackCircle.setStrokeWidth(2);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.color(0, 0, 0, 0.3));
        token.setEffect(shadow);

        token.getChildren().addAll(fallbackCircle, symbolText);
        return token;
    }

    public void setRollDiceButton(Button rollDiceButton) {
        this.rollDiceButton = rollDiceButton;
    }

    public void setDiceResultText(Text diceResultText) {
        this.diceResultText = diceResultText;
    }

    public void initializePlayer() {
        currentTrackIndex = 0;
        placePlayerOnFirstSpace();
        int[] startPosition = selectedTrack.getTrackPositions().get(0);
        currentPlayer = new Player(1, startPosition, true);
    }

    private void placePlayerOnFirstSpace() {
        if (selectedTrack != null && !selectedTrack.getTrackPositions().isEmpty()) {
            int[] startPosition = selectedTrack.getTrackPositions().get(0);
            positionPlayerTokenAtBoardCoordinates(startPosition[0], startPosition[1]);
        }
    }

    public void positionPlayerTokenAtBoardCoordinates(int row, int col) {
        BoardSpace space = boardGrid.getBoardSpace(row, col);
        if (space != null) {
            double spaceX = space.getBoundsInParent().getMinX() + space.getParent().getLayoutX();
            double spaceY = space.getBoundsInParent().getMinY() + space.getParent().getLayoutY();

            double centerX = spaceX + space.getWidth() / 2;
            double centerY = spaceY + space.getHeight() / 2;

            playerToken.setLayoutX(centerX - playerToken.getBoundsInLocal().getWidth() / 2);
            playerToken.setLayoutY(centerY - playerToken.getBoundsInLocal().getHeight() / 2);

            space.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
                double updatedX = space.getLocalToSceneTransform().getTx() + space.getWidth() / 2;
                double updatedY = space.getLocalToSceneTransform().getTy() + space.getHeight() / 2;

                playerToken.setLayoutX(updatedX - playerToken.getBoundsInLocal().getWidth() / 2);
                playerToken.setLayoutY(updatedY - playerToken.getBoundsInLocal().getHeight() / 2);
            });
        }
    }

    public void movePlayer(int steps) {
        animationInProgress = true;
        if (rollDiceButton != null) {
            rollDiceButton.setDisable(true);
        }

        List<int[]> trackPositions = selectedTrack.getTrackPositions();
        int targetIndex = Math.min(currentTrackIndex + steps, trackPositions.size() - 1);
        boolean isWinningMove = (targetIndex >= trackPositions.size() - 1);
        Timeline moveTimeline = animatePlayerMovement(currentTrackIndex, targetIndex, trackPositions, isWinningMove);

        int specialAction = 0;
        String specialMsg = null;

        for (int i = 0; i < specialIdx.length; i++) {
            if (specialIdx[i] == targetIndex) {
                specialAction = specialActions[i];
                specialMsg = messages[i];
                break;
            }
        }

        if (specialAction != 0) {
            int finalTargetIndex = targetIndex + specialAction;
            finalTargetIndex = Math.max(0, Math.min(finalTargetIndex, trackPositions.size() - 1));
            setSpecialMessage(specialMsg);
            ArduinoConnector.sendTargetIndices(targetIndex, finalTargetIndex);

            final int finalFinalTargetIndex = finalTargetIndex;
            final int finalSpecialAction = specialAction;

            moveTimeline.setOnFinished(event -> {
                if (finalSpecialAction > 0) {
                    SoundManager.playSound("/org/example/sdprototype/sounds/SuccessSound.wav");
                } else {
                    SoundManager.playSound("/org/example/sdprototype/sounds/FailSound.wav");
                }

                PauseTransition pause = new PauseTransition(Duration.seconds(1.5));
                pause.setOnFinished(pauseEvent -> {
                    Timeline specialMoveTimeline = animatePlayerMovement(targetIndex, finalFinalTargetIndex, trackPositions, false);
                    specialMoveTimeline.setOnFinished(e -> {
                        animationInProgress = false;
                        if (rollDiceButton != null) {
                            rollDiceButton.setDisable(false);
                        }
                    });
                    specialMoveTimeline.play();
                });
                pause.play();
            });
        }
        else {
            ArduinoConnector.sendTargetIndices(targetIndex, targetIndex);
            setSpecialMessage(specialMsg);
            moveTimeline.setOnFinished(event -> {
                animationInProgress = false;
                if (rollDiceButton != null) {
                    rollDiceButton.setDisable(false);
                }
            });
        }

        moveTimeline.play();
    }

    public void movePlayer(int steps, Button button) {
        this.rollDiceButton = button;
        movePlayer(steps);
    }

    private Timeline animatePlayerMovement(int startIndex, int endIndex, List<int[]> trackPositions, boolean isWinningMove) {
        Timeline timeline = new Timeline();
        double hopDuration = 0.7;
        double timePoint = 0;

        if (endIndex > startIndex) {
            for (int i = startIndex + 1; i <= endIndex; i++) {
                animateStep(timeline, i, endIndex, trackPositions, hopDuration, timePoint, isWinningMove);
                timePoint += hopDuration;
            }
        }
        else {
            for (int i = startIndex - 1; i >= endIndex; i--) {
                animateStep(timeline, i, endIndex, trackPositions, hopDuration, timePoint, isWinningMove);
                timePoint += hopDuration;
            }
        }

        return timeline;
    }

    private void animateStep(Timeline timeline, int index, int endIndex, List<int[]> trackPositions,
                             double hopDuration, double timePoint, boolean isWinningMove) {
        int[] position = trackPositions.get(index);
        int row = position[0];
        int col = position[1];

        BoardSpace space = boardGrid.getBoardSpace(row, col);
        if (space != null) {
            Node tokenParent = playerToken.getParent();
            Bounds spaceBounds = space.localToScene(space.getBoundsInLocal());
            Point2D spaceCenter = tokenParent.sceneToLocal(
                    spaceBounds.getMinX() + spaceBounds.getWidth()/2,
                    spaceBounds.getMinY() + spaceBounds.getHeight()/2
            );

            double centerX = spaceCenter.getX();
            double centerY = spaceCenter.getY();
            final double finalCenterX = centerX;
            final double finalCenterY = centerY;
            final int currentIndex = index;
            double tokenWidth = playerToken.getBoundsInLocal().getWidth();
            double tokenHeight = playerToken.getBoundsInLocal().getHeight();

            KeyFrame hopUpFrame = new KeyFrame(
                    Duration.seconds(timePoint + hopDuration/3),
                    event -> {
                        SoundManager.playSound("/org/example/sdprototype/sounds/JumpSound.wav");
                    },
                    new KeyValue(playerToken.layoutXProperty(), centerX - tokenWidth/2),
                    new KeyValue(playerToken.layoutYProperty(), centerY - tokenHeight/2 - 20)
            );

            KeyFrame landFrame = new KeyFrame(
                    Duration.seconds(timePoint + hopDuration),
                    event -> {
                        if (currentIndex == endIndex) {
                            currentTrackIndex = currentIndex;
                            playerToken.setLayoutX(finalCenterX - tokenWidth/2);
                            playerToken.setLayoutY(finalCenterY - tokenHeight/2);

                            if (currentPlayer != null) {
                                currentPlayer.setLocation(position);
                            }

                            if (isWinningMove) {
                                showWinnerScreen();
                                ArduinoConnector.sendWonGame();
                            } else {
                                animationInProgress = false;
                                if (rollDiceButton != null) {
                                    rollDiceButton.setDisable(false);
                                }
                            }
                        }
                    },
                    new KeyValue(playerToken.layoutXProperty(), centerX - tokenWidth/2),
                    new KeyValue(playerToken.layoutYProperty(), centerY - tokenHeight/2)
            );

            timeline.getKeyFrames().addAll(hopUpFrame, landFrame);
        }
    }

    private void showWinnerScreen() {
        try {
            Stage mainStage = (Stage) trackDisplayPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/WinnerScreen.fxml"));
            Parent root = loader.load();
            WinnerScreenController controller = loader.getController();
            controller.setPlayerInfo(currentPlayer);

            // Create scene with the new dimensions
            Scene winnerScene = new Scene(root, 1000, 750);
            mainStage.setScene(winnerScene);

            // Set minimum size and center the window
            mainStage.setMinWidth(900);
            mainStage.setMinHeight(700);
            mainStage.centerOnScreen();
        } catch (IOException e) {
            System.out.println("Error loading winner screen: " + e.getMessage());
        }
    }

    public Pane getTrackDisplayPane() {
        return trackDisplayPane;
    }

    public boolean isAnimationInProgress() {
        return animationInProgress;
    }

    public String getSpecialMessage() {
        return specialMessage;
    }

    public void setSpecialMessage(String specialMessage) {
        this.specialMessage = specialMessage;
    }
}