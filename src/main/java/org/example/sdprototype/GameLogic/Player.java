package org.example.sdprototype.GameLogic;

public class Player {
    private int playerID;
    private int[] location;
    private boolean turn;

    /*
    Constructor for Player class
     */
    public Player(int playerID, int[] location, boolean turn) {
        this.playerID = playerID;
        this.location = location;
        this.turn = turn;
    }

    /*
    Getter and setter for player ID
     */
    public int getPlayerID() {return playerID;}
    public void setPlayerID(int playerID) {this.playerID = playerID;}

    /*
    Getter and setter for player's location on game board
     */
    public int[] getLocation() {return location;}
    public void setLocation(int[] location) {this.location = location;}

    /*
    Getter and setter for if it is the player's turn
     */
    public boolean getTurn() {return turn;}
    public void setTurn(boolean turn) {this.turn = turn;}
}
