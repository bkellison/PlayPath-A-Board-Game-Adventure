package org.example.sdprototype;

public class Card {
    private int cardID;
    private String type;
    private String description;

    /*
    Constructor for Card class
     */
    public Card(int cardID, String type, String description) {
        this.cardID = cardID;
        this.type = type;
        this.description = description;
    }

    /*
    Getter and setter for card ID
     */
    public int getCardID() { return cardID; }
    public void setCardID(int cardID) { this.cardID = cardID; }

    /*
    Getter and setter for card type
     */
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    /*
    Getter and setter for card description
     */
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
