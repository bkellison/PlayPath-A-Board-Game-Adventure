package org.example.sdprototype;
import org.junit.jupiter.api.Test;

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

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        testPlayer = null;
    }
}