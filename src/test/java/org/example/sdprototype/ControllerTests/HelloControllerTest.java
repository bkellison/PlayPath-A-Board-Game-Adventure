package org.example.sdprototype.ControllerTests;

import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.example.sdprototype.Controllers.HelloController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class HelloControllerTest {

    private HelloController controller;

    @BeforeEach
    void setUp() {
        // Create an instance of the controller
        controller = new HelloController();

        // Mock JavaFX components
        ImageView mockCharacterImage = mock(ImageView.class);
        AnchorPane mockBackground = mock(AnchorPane.class);

        // Inject mocks into controller (simulating @FXML injection)
        controller.setCharacterImage(mockCharacterImage);
        controller.setBackground(mockBackground);
    }

    @Test
    void testInitialPosition() {
        // Check that the initial position is 0
        assertEquals(0, controller.getCurrentPosition(), "Initial position should be 0.");
    }

    @Test
    void testCheckPositionMovesForward() {
        controller.checkPosition();
        assertEquals(1, controller.getCurrentPosition(), "Position should increment to 1.");

        controller.checkPosition();
        assertEquals(2, controller.getCurrentPosition(), "Position should increment to 2.");
    }

    @Test
    void testCheckPositionWrapsAround() {
        // Move to the last position
        for (int i = 0; i < 26; i++) {
            controller.checkPosition();
        }
        assertEquals(0, controller.getCurrentPosition(), "Position should wrap around to 0.");
    }

    @Test
    void testMoveCharacterMouseEvent() {
        MouseEvent mockEvent = mock(MouseEvent.class);
        controller.moveCharacter(mockEvent);
        assertEquals(1, controller.getCurrentPosition(), "After mouse event, position should be 1.");
    }

    @Test
    void testMoveCharacterKeyEvent() {
        KeyEvent mockEvent = mock(KeyEvent.class);
        controller.moveCharacter(mockEvent);
        assertEquals(1, controller.getCurrentPosition(), "After key event, position should be 1.");
    }
}
