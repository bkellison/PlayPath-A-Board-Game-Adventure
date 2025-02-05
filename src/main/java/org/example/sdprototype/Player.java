package org.example.sdprototype;

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
}
