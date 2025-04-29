package org.example.sdprototype.GridBoard;
import javafx.geometry.Point2D;
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
    private ImageView imageView;    // Will hold images for non-track spaces
    private boolean isSpecial = false;  // Will be changed to true if space is a special space

    public BoardSpace(int row, int col, int trackNumber, boolean isActive) {
        this.row = row;
        this.col = col;
        this.trackNumber = trackNumber;

        shape = new Rectangle(80, 80);

        // For all tracks, make the spaces fully transparent
        if (trackNumber == 1 || trackNumber == 2 || trackNumber == 3) {
            shape.setFill(Color.TRANSPARENT);
            shape.setStroke(Color.TRANSPARENT);
            shape.setOpacity(0); // Set opacity to 0 to make it completely invisible
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
        // If an image is not already set, set it
        if (imageView == null) {

            if (!isSpecial) {
                // Clear any previous children, unless it is a special space (in this case, we still want the background square)
                this.getChildren().clear();
            }

            if (!Objects.equals(imagePath, "Remove")) {
                Image image = new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
                imageView = new ImageView(image);
                imageView.setFitWidth(this.getWidth());
                imageView.setFitHeight(this.getHeight());
                imageView.setPreserveRatio(false);

                // Bind the image size to the BoardSpace size
                widthProperty().addListener((obs, oldVal, newVal) -> imageView.setFitWidth(newVal.doubleValue()));
                heightProperty().addListener((obs, oldVal, newVal) -> imageView.setFitHeight(newVal.doubleValue()));

                // Add the ImageView
                this.getChildren().add(imageView);

                // Remove background color if you set one before
                this.setBackground(Background.EMPTY);
            }

        }
    }

    public void setDefaultColor() {
        // Set default color regardless of track number
        shape.setFill(DEFAULT_COLOR);
        isHighlighted = false;
    }

    public void setHighlighted(boolean highlighted, Color color) {
        this.isHighlighted = highlighted;
        this.highlightColor = color;

        // Use the highlight color if highlighted, otherwise use default
        if (highlighted && color != null) {
            // Special case for Track 1 which should be transparent
            if (color.equals(Color.TRANSPARENT)) {
                shape.setFill(Color.TRANSPARENT);
                shape.setStroke(Color.TRANSPARENT);
                shape.setOpacity(0); // Set opacity to 0 to make it completely invisible
            } else {
                shape.setFill(color);
                shape.setStroke(Color.BLACK);
                shape.setOpacity(1);
            }
        } else {
            setDefaultColor();
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getTrackNumber() {
        return trackNumber;
    }

    public Point2D getTopMiddle() {
        return new Point2D(getLayoutX() + getWidth() / 2, getLayoutY());
    }

    public Point2D getBottomMiddle() {
        return new Point2D(getLayoutX() + getWidth() / 2, getLayoutY() + getHeight());
    }

    public Point2D getLeftMiddle() {
        return new Point2D(getLayoutX(), getLayoutY() + getHeight() / 2);
    }

    public Point2D getRightMiddle() {
        return new Point2D(getLayoutX() + getWidth(), getLayoutY() + getHeight() / 2);
    }

    public boolean isSpecial() { return isSpecial; }

    public void setSpecial(boolean special) { this.isSpecial = special; }
}