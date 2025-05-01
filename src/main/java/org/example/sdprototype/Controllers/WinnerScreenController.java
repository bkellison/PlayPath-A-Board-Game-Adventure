package org.example.sdprototype.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.sdprototype.Application.MainApplication;
import org.example.sdprototype.Communicator.ArduinoConnector;
import org.example.sdprototype.GameLogic.Player;

import java.io.IOException;

public class WinnerScreenController {

    @FXML
    private Text congratsText;

    @FXML
    private Text winnerText;

    @FXML
    private Button playAgainButton;

    @FXML
    private Button mainMenuButton;

    private Player player;

    public void setPlayerInfo(Player player) {
        this.player = player;
        // Update the winner text with the player ID
        if (player != null) {
            winnerText.setText("Player " + player.getPlayerID() + " has won the game!");
        }
    }

    @FXML
    public void handlePlayAgain(ActionEvent event) {
        try {
            // Reset to track selection
            Stage stage = (Stage) playAgainButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TrackSelectionScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);

            // Get the controller and set the stage
            TrackSelectionScreenController controller = loader.getController();
            controller.setStage(stage);

            // Tell arduino we are playing a new game
            ArduinoConnector.sendNewGame();
        } catch (IOException ex) {
            System.out.println("Could not load track selection screen: " + ex.getMessage());
        }
    }

    @FXML
    public void handleMainMenu(ActionEvent event) {
        try {
            // Return to welcome screen
            Stage stage = (Stage) mainMenuButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/WelcomeScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);

            // Get the controller and set the stage
            WelcomeScreenController controller = loader.getController();
            controller.setStage(stage);
        } catch (IOException ex) {
            System.out.println("Could not load welcome screen: " + ex.getMessage());
            try {
                new MainApplication().start(new Stage());
                ((Stage) mainMenuButton.getScene().getWindow()).close();
            } catch (Exception ex2) {
                System.out.println("Could not restart application.");
            }
        }
    }
}