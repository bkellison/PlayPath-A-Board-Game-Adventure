package org.example.sdprototype.GridBoard;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BoardSpace extends Region {
    private final int row, col, trackNumber;
    private Rectangle shape;
    private static final Color DEFAULT_COLOR = Color.LIGHTGRAY;
    private boolean isHighlighted = false;
    private Color highlightColor = null;

    public BoardSpace(int row, int col, int trackNumber, boolean isActive) {
        this.row = row;
        this.col = col;
        this.trackNumber = trackNumber;

        shape = new Rectangle(80, 80, DEFAULT_COLOR);
        shape.setStroke(Color.BLACK);
        shape.setStrokeWidth(1);

        getChildren().add(shape);

        widthProperty().addListener((obs, oldVal, newVal) -> shape.setWidth(newVal.doubleValue()));
        heightProperty().addListener((obs, oldVal, newVal) -> shape.setHeight(newVal.doubleValue()));
    }

    public void setDefaultColor() {
        shape.setFill(DEFAULT_COLOR);
        isHighlighted = false;
    }

    public void setHighlighted(boolean highlighted, Color color) {
        this.isHighlighted = highlighted;
        this.highlightColor = color;
        if (highlighted && color != null) {
            shape.setFill(color);
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
}