package org.example.sdprototype.ClassTests;

import org.example.sdprototype.GameLogic.Dice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiceCardTest {
    private Dice dice;

    @BeforeEach
    void setUp() {
        dice = new Dice(6); // Default 6-sided dice
    }

    @Test
    void testDiceRollWithinRange() {
        for (int i = 0; i < 100; i++) { // Test multiple rolls
            int roll = dice.roll();
            assertTrue(roll >= 1 && roll <= 6, "Dice roll out of range: " + roll);
        }
    }
}
