package org.example.sdprototype.GameLogic;

public class Game {
    private int gameID;
    private int gameMode;
    private int playerID;
    private boolean cpus;

    /*
    Constructor for game class
     */
    public Game(int gameID, int gameMode, int playerID, boolean cpus) {
        this.gameID = gameID;

        // Since there are only 3 game modes, input can only be 1-3
        if(gameMode > 0 && gameMode < 4) {
            this.gameMode = gameMode;
        }
        else {
            throw new IllegalArgumentException("Invalid game mode. Please select a game mode between 1 and 3.");
        }

        this.playerID = playerID;
        this.cpus = cpus;
    }

    /*
    Getter and setter for game ID
     */
    public int getGameID() {return gameID;}
    public void setGameID(int gameID) {this.gameID = gameID;}

    /*
    Getter and setter for game mode
     */
    public int getGameMode() {return gameMode;}
    public void setGameMode(int gameMode) {
        // Since there are only 3 game modes, input can only be 1-3
        if(gameMode > 0 && gameMode < 4) {
            this.gameMode = gameMode;
        }
        else {
            throw new IllegalArgumentException("Invalid game mode. Please select a game mode between 1 and 3.");
        }
    }

    /*
    Getter and setter for player ID
     */
    public int getPlayerID() {return playerID;}
    public void setPlayerID(int playerID) {this.playerID = playerID;}

    /*
    Getter and setter for CPUs
     */
    public boolean getCpus() {return cpus;}
    public void setCpus(boolean cpus) {this.cpus = cpus;}

}
