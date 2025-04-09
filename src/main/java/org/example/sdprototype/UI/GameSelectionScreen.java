package org.example.sdprototype.UI;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GameSelectionScreen {
    private Stage primaryStage;
    private Scene scene;

    public GameSelectionScreen(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Scene getScene() {
        return scene;
    }
}