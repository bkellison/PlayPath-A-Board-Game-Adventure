package org.example.sdprototype.Controllers;

import org.example.sdprototype.GameLogic.Card;

import java.util.Random;

public class CardController {
    private Random random;

    public CardController() {
        random = new Random();
    }

    public Card drawCard() {
        int cardTypeIndex = random.nextInt(Card.CardType.values().length);
        Card.CardType type = Card.CardType.values()[cardTypeIndex];

        switch (type) {
            case MOVE_FORWARD:
                return new Card(1, type, "Move forward 2 spaces!", 2);
            case LOSE_TURN:
                return new Card(2, type, "Oops! Lose a turn.", 0);
            case QUIZ:
                return new Card(3, type, "Answer a question!", 0);
            default:
                return null;
        }
    }
}
