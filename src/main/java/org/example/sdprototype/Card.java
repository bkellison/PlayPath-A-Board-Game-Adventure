package org.example.sdprototype;

public class Card {
    public enum CardType { MOVE_FORWARD, LOSE_TURN, QUIZ }

    private int cardID;
    private CardType type;
    private String description;
    private int moveSpaces; // For MOVE_FORWARD type

    /*
    Constructor for Card class
     */
    public Card(int cardID, CardType type, String description, int moveSpaces) {
        this.cardID = cardID;
        this.type = type;
        this.description = description;
        this.moveSpaces = moveSpaces;
        }

    /*
    Getter and setter for card ID
     */
    public int getCardID() { return cardID; }
    public void setCardID(int cardID) { this.cardID = cardID; }

    /*
    Getter and setter for card type
     */
    public CardType getType() { return type; }
    public void setType(CardType type) { this.type = type; }

    /*
    Getter and setter for card description
     */
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    /*
    Getter and setter for move amount
     */
    public int getMoveAmount() { return moveSpaces; }
    public void setMoveAmount(int moveSpaces) { this.moveSpaces = moveSpaces; }

    /*
    Effect method to process the card's action
     */
    public void applyEffect(HelloController controller) {
        switch (type) {
            case MOVE_FORWARD:
                controller.moveForward(moveSpaces);
                break;
            case LOSE_TURN:
                controller.setSkipTurn(true);
                break;
            case QUIZ:
                controller.showQuiz();
                break;
        }
    }
}
