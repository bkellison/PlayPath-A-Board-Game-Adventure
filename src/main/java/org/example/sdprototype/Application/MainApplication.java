package org.example.sdprototype.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.sdprototype.Controllers.WelcomeScreenController;


public class MainApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Board Game");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/WelcomeScreen.fxml"));
        Parent root = loader.load();

        // Get the controller and set the stage
        WelcomeScreenController controller = loader.getController();

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        controller.setStage(primaryStage);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}