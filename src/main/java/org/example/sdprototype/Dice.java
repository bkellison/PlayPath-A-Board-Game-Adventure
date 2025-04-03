package org.example.sdprototype;

import java.util.Random;

public class Dice {
    private int sides;
    private Random random;

    /*
    Constructor for Dice class with default 6 sides
     */
    public Dice() {
        this(6);
    }

    /*
    Constructor for Dice class with custom number of sides
     */
    public Dice(int sides) {
        this.sides = sides;
        this.random = new Random();
    }

    /*
    Rolls the dice and returns a random value between 1 and the number of sides
     */
    public int roll() {
        return random.nextInt(sides) + 1;
    }
}