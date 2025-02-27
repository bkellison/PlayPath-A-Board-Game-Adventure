package org.example.sdprototype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiceCardTest {
    private Dice dice;
    private Card card;

    @BeforeEach
    void setUp() {
        dice = new Dice(6); // Default 6-sided dice
        card = new Card(1, "Action", "Move forward 2 spaces");
    }

    @Test
    void testDiceRollWithinRange() {
        for (int i = 0; i < 100; i++) { // Test multiple rolls
            int roll = dice.roll();
            assertTrue(roll >= 1 && roll <= 6, "Dice roll out of range: " + roll);
        }
    }

    @Test
    void testDiceDefaultSides() {
        Dice defaultDice = new Dice();
        assertEquals(6, defaultDice.getSides(), "Default dice should have 6 sides");
    }

    @Test
    void testDiceCustomSides() {
        Dice customDice = new Dice(10);
        assertEquals(10, customDice.getSides(), "Custom dice should have specified number of sides");
    }

    @Test
    void testCardGetters() {
        assertEquals(1, card.getCardID(), "Card ID should match");
        assertEquals("Action", card.getType(), "Card type should match");
        assertEquals("Move forward 2 spaces", card.getDescription(), "Card description should match");
    }

    @Test
    void testCardSetters() {
        card.setCardID(2);
        card.setType("Penalty");
        card.setDescription("Lose a turn");

        assertEquals(2, card.getCardID(), "Card ID should update");
        assertEquals("Penalty", card.getType(), "Card type should update");
        assertEquals("Lose a turn", card.getDescription(), "Card description should update");
    }
}