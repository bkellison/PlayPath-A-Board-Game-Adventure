package org.example.sdprototype.GridBoard;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;

public class BoardSpace extends Region {
    private final int row, col, trackNumber;
    private Rectangle shape;
    private static final Color DEFAULT_COLOR = Color.LIGHTGRAY;
    private boolean isHighlighted = false;
    private Color highlightColor = null;
    private ImageView imageView;

    public BoardSpace(int row, int col, int trackNumber, boolean isActive) {
        this.row = row;
        this.col = col;
        this.trackNumber = trackNumber;

        shape = new Rectangle(80, 80);

        if (trackNumber == 1 || trackNumber == 2 || trackNumber == 3) {
            shape.setFill(Color.TRANSPARENT);
            shape.setStroke(Color.TRANSPARENT);
            shape.setOpacity(0);
        } else {
            shape.setFill(DEFAULT_COLOR);
            shape.setStroke(Color.BLACK);
            shape.setStrokeWidth(1);
        }

        getChildren().add(shape);

        widthProperty().addListener((obs, oldVal, newVal) -> shape.setWidth(newVal.doubleValue()));
        heightProperty().addListener((obs, oldVal, newVal) -> shape.setHeight(newVal.doubleValue()));
    }

    public void addImageToSpace(String imagePath, boolean isSpecial) {
        if (imageView == null) {
            if (!isSpecial) {
                this.getChildren().clear();
            }

            if (!Objects.equals(imagePath, "Remove")) {
                Image image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
                imageView = new ImageView(image);
                imageView.setFitWidth(this.getWidth());
                imageView.setFitHeight(this.getHeight());
                imageView.setPreserveRatio(false);

                widthProperty().addListener((obs, oldVal, newVal) -> imageView.setFitWidth(newVal.doubleValue()));
                heightProperty().addListener((obs, oldVal, newVal) -> imageView.setFitHeight(newVal.doubleValue()));
                this.getChildren().add(imageView);
                this.setBackground(Background.EMPTY);
            }
        }
    }

    public void setDefaultColor() {
        shape.setFill(DEFAULT_COLOR);
        isHighlighted = false;
    }

    public void setHighlighted(boolean highlighted, Color color) {
        this.isHighlighted = highlighted;
        this.highlightColor = color;

        if (highlighted && color != null) {
            if (color.equals(Color.TRANSPARENT)) {
                shape.setFill(Color.TRANSPARENT);
                shape.setStroke(Color.TRANSPARENT);
                shape.setOpacity(0);
            } else {
                shape.setFill(color);
                shape.setStroke(Color.BLACK);
                shape.setOpacity(1);
            }
        } else {
            setDefaultColor();
        }
    }
}