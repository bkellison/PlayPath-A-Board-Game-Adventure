package org.example.sdprototype;

public class Game {
    private int gameID;
    private int gameMode;
    private int playerID;
    private boolean cpus;
    /* ADD ONCE DICE CLASS COMPLETE
    private Dice dice;
     */

    /*
    Constructor for game class
     */
    public Game(int gameID, int gameMode, int playerID, boolean cpus) {
        this.gameID = gameID;
        this.gameMode = gameMode;
        this.playerID = playerID;
        this.cpus = cpus;
    }

    /*
    Getter and setter for game ID
     */
    public int getGameID() {return gameID;}
    public void setGameID(int gameID) {this.gameID = gameID;}
}
