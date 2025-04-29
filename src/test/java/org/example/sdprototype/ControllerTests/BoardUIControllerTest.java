package org.example.sdprototype.ControllerTests;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import org.example.sdprototype.Controllers.BoardUIController;
import org.example.sdprototype.Controllers.GameController;
import org.example.sdprototype.GridBoard.GameTrack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;

class BoardUIControllerTest {

    private BoardUIController controller;
    private GameController mockGameController;
    private GameTrack mockTrack;

    @BeforeAll
    static void initToolkit() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.startup(latch::countDown);
        latch.await();
    }

    @BeforeEach
    void setUp() {
        controller = new BoardUIController();

        // Create real JavaFX components
        controller.rollDiceButton = new Button();
        controller.setTrackInfoLabel(mock(Text.class));
        controller.setPlayerInfoLabel(mock(Text.class));
        controller.setDiceResultText(mock(Text.class));
        controller.setMessageLabel(mock(Label.class));

        // Mock dependencies
        mockGameController = mock(GameController.class);
        mockTrack = mock(GameTrack.class);

        controller.setGameController(mockGameController);
    }

    @Test
    void testSetSelectedTrack_Track1() {
        when(mockTrack.getName()).thenReturn("Track 1");
        controller.setSelectedTrack(mockTrack);

        verify(controller.getTrackInfoLabel()).setText("Shrek's Swamp Trek");
    }

    @Test
    void testSetSelectedTrack_Track2() {
        when(mockTrack.getName()).thenReturn("Track 2");
        controller.setSelectedTrack(mockTrack);

        verify(controller.getTrackInfoLabel()).setText("Ocean Odyssey");
    }

    @Test
    void testSetSelectedTrack_Other() {
        when(mockTrack.getName()).thenReturn("Some Other Track");
        controller.setSelectedTrack(mockTrack);

        verify(controller.getTrackInfoLabel()).setText("Pirate's Gold Rush");
    }

    @Test
    void testHandleRollDice_NoSpecialMessage() {
        when(mockGameController.isAnimationInProgress()).thenReturn(false);
        when(mockGameController.getSpecialMessage()).thenReturn(null);

        controller.handleRollDice(new ActionEvent());

        verify(controller.getDiceResultText()).setText(startsWith("Roll Dice:"));
        verify(mockGameController).movePlayer(anyInt(), eq(controller.rollDiceButton));
        verify(controller.getMessageLabel()).setText("");
    }

    @Test
    void testHandleRollDice_WithSpecialMessage() {
        when(mockGameController.isAnimationInProgress()).thenReturn(false);
        when(mockGameController.getSpecialMessage()).thenReturn("Special event!");

        controller.handleRollDice(new ActionEvent());

        verify(controller.getDiceResultText()).setText(startsWith("Roll Dice:"));
        verify(mockGameController).movePlayer(anyInt(), eq(controller.rollDiceButton));
        // Cannot easily verify delayed message update due to PauseTransition timing.
    }
}
