package org.example.sdprototype;
import org.example.sdprototype.GameLogic.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player testPlayer;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        testPlayer = new Player(1, new int[]{0, 1}, false);
    }

    @Test
    void testPlayerGetters() {
        assertEquals(1, testPlayer.getPlayerID(), "Player ID should be 1, but was " + testPlayer.getPlayerID());
        assertArrayEquals(new int[]{0,1}, testPlayer.getLocation(), "Location should be {0, 1}, but was " + Arrays.toString(testPlayer.getLocation()));
        assertFalse(testPlayer.getTurn(), "Turn should be false");
    }

    @ParameterizedTest
    @CsvSource({
            "2, 1, 1, true",
            "3, 2, 5, false",
            "50, 6, 0, true",
            "32, 3, 9, false",
            "8, 3, 6, true"
    })
    void testPlayerSetters(int id, int loc1, int loc2, boolean turn) {
        // Convert 2 location integers into an array
        int[] location = {loc1, loc2};

        testPlayer.setPlayerID(id);
        testPlayer.setLocation(location);
        testPlayer.setTurn(turn);

        assertEquals(id, testPlayer.getPlayerID(), "Player ID should be " + id + ", but was " + testPlayer.getPlayerID());
        assertArrayEquals(location, testPlayer.getLocation(), "Location should be " + Arrays.toString(location) + ", but was " + Arrays.toString(testPlayer.getLocation()));
        assertEquals(turn, testPlayer.getTurn(), "Turn should be " + turn + ", but was " + testPlayer.getTurn());
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        testPlayer = null;
    }
}