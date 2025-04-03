package org.example.sdprototype.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the FXML file and set scene
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Load the CSS file
        scene.getStylesheets().add(getClass().getResource("/org/example/sdprototype/styles.css").toExternalForm());

        // Modify scene settings and show
        stage.setTitle("Lost on a Trip");
        stage.setScene(scene);
        stage.setWidth(1100);  // Just hard coded for now
        stage.setHeight(900);  // Hard coded for now
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
