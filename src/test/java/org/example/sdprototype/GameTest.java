package org.example.sdprototype;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game testGame;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        // Create a test game with ID 1, game mode 3, playerID 1, and cpus turned off
        testGame = new Game(1, 3, 1, false);
    }

    @Test
    void testGameGetters() {
        assertEquals(1, testGame.getGameID(), "Game ID should be 1, but was " + testGame.getGameID());
        assertEquals(3, testGame.getGameMode(), "Game mode should be 3, but was " + testGame.getGameMode());
        assertEquals(1, testGame.getPlayerID(), "Player ID should be 1, but was " + testGame.getPlayerID());
        assertFalse(testGame.getCpus());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1, 1, true",
            "1, 2, 2, false",
            "1, 3, 3, true",
            "2, 1, 4, false",
            "2, 2, 1, true",
            "2, 3, 2, false",
            "3, 1, 3, true",
            "3, 2, 4, false",
            "3, 3, 1, true"
    })
    void testGameSetters(int gameID, int gameMode, int playerID, boolean cpus) {
        // Set each field of testGame
        testGame.setGameID(gameID);
        testGame.setGameMode(gameMode);
        testGame.setPlayerID(playerID);
        testGame.setCpus(cpus);

        assertEquals(gameID, testGame.getGameID(), "Game ID should be " + gameID + ", but was " + testGame.getGameID());
        assertEquals(gameMode, testGame.getGameMode(), "Game mode should be " + gameMode + ", but was " + testGame.getGameMode());
        assertEquals(playerID, testGame.getPlayerID(), "Player ID should be " + playerID + ", but was " + testGame.getPlayerID());
        assertEquals(cpus, testGame.getCpus(), "Cpus should be " + cpus + ", but was " + testGame.getCpus());
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        testGame = null;
    }
}
