package org.example.sdprototype.UI;


import javafx.scene.control.Button;

import org.example.sdprototype.Controllers.GameController;
import org.example.sdprototype.GridBoard.GameTrack;

public class BoardUI {
    private GameController gameController;
    private GameTrack selectedTrack;
    private Button rollDiceButton;

    public BoardUI(GameController gameController, GameTrack track) {
        this.gameController = gameController;
        this.selectedTrack = track;
    }
}