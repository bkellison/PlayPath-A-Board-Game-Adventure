package org.example.sdprototype.ClassTests;

import org.example.sdprototype.GameLogic.Card;
import org.example.sdprototype.GameLogic.Dice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DiceCardTest {
    private Dice dice;
    private Card card;

    @BeforeEach
    void setUp() {
        dice = new Dice(6); // Default 6-sided dice
        card = new Card(1, Card.CardType.MOVE_FORWARD, "Move forward 2 spaces", 2);
    }

    @Test
    void testDiceRollWithinRange() {
        for (int i = 0; i < 100; i++) { // Test multiple rolls
            int roll = dice.roll();
            assertTrue(roll >= 1 && roll <= 6, "Dice roll out of range: " + roll);
        }
    }

    @Test
    void testCardGetters() {
        assertEquals(1, card.getCardID(), "Card ID should match");
        assertEquals(Card.CardType.MOVE_FORWARD, card.getType(), "Card type should match");
        assertEquals("Move forward 2 spaces", card.getDescription(), "Card description should match");
        assertEquals(2, card.getMoveSpaces(), "Card move spaces should match");
    }

    @Test
    void testCardSetters() {
        card.setCardID(2);
        card.setType(Card.CardType.LOSE_TURN);
        card.setDescription("Lose a turn");

        assertEquals(2, card.getCardID(), "Card ID should update");
        assertEquals(Card.CardType.LOSE_TURN, card.getType(), "Card type should update");
        assertEquals("Lose a turn", card.getDescription(), "Card description should update");
    }
}
