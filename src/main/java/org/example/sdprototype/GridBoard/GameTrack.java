package org.example.sdprototype.GridBoard;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

public class GameTrack {
    private final List<Integer> spaceIds = new ArrayList<>();
    private final String name;
    private final Color trackColor;

    public GameTrack(String name, int[] spaces, Color trackColor) {
        this.name = name;
        this.trackColor = trackColor;

        for (int space : spaces) {
            spaceIds.add(space);
        }
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return trackColor;
    }

    // Convert spaceIds to row/col positions
    public List<int[]> getTrackPositions() {
        List<int[]> positions = new ArrayList<>();
        for (int spaceId : spaceIds) {
            int id = spaceId - 1;
            int row = id / 8;
            int col;

            if (row % 2 == 0) {
                // Left to right rows
                col = id % 8;
            } else {
                // Right to left rows
                col = 7 - (id % 8);
            }

            positions.add(new int[]{row, col});
        }
        return positions;
    }

    public static GameTrack createTrack1() {
        int[] spaces = {33, 32, 17, 16, 1, 2, 3, 14, 19, 30, 35, 36, 37, 38, 27, 22, 11, 6, 7, 8, 9, 24, 25, 40};
        return new GameTrack("Track 1", spaces, Color.BLUE);
    }

    public static GameTrack createTrack2() {
        int[] spaces = {33, 34, 31, 18, 15, 14, 13, 20, 29, 28, 27, 22, 11, 10, 9, 8};
        return new GameTrack("Track 2", spaces, Color.RED);
    }

    public static GameTrack createTrack3() {
        int[] spaces = {8, 9, 24, 23, 22, 11, 12, 13, 14, 15, 18, 31, 30, 29, 36, 37, 38, 39, 40};
        return new GameTrack("Track 3", spaces, Color.GREEN);
    }
}