package org.example.sdprototype.Utilities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This is a utility class to generate dice images.
 * You would run this as a standalone program to create the dice images,
 * then place them in your resources folder at:
 * /src/main/resources/org/example/sdprototype/images/dice/
 */
public class DiceImageCreator {

    public static void main(String[] args) {
        // Create output directory
        File outputDir = new File("src/main/resources/org/example/sdprototype/images/dice");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        // Generate 6 dice images
        for (int i = 1; i <= 6; i++) {
            generateDiceImage(i, new File(outputDir, "dice" + i + ".png"));
        }

        System.out.println("Dice images generated successfully in: " + outputDir.getAbsolutePath());
    }

    private static void generateDiceImage(int number, File outputFile) {
        int width = 200;
        int height = 200;
        int dotSize = 30;

        // Create blank image with white background
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Set anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw dice background (rounded rectangle)
        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(0, 0, width, height, 30, 30);

        // Draw border
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(0, 0, width - 1, height - 1, 30, 30);

        // Draw dots based on dice number
        g2d.setColor(Color.BLACK);

        switch (number) {
            case 1:
                // Center dot
                drawDot(g2d, width / 2, height / 2, dotSize);
                break;

            case 2:
                // Top-left and bottom-right
                drawDot(g2d, width / 4, height / 4, dotSize);
                drawDot(g2d, 3 * width / 4, 3 * height / 4, dotSize);
                break;

            case 3:
                // Top-left, center, and bottom-right
                drawDot(g2d, width / 4, height / 4, dotSize);
                drawDot(g2d, width / 2, height / 2, dotSize);
                drawDot(g2d, 3 * width / 4, 3 * height / 4, dotSize);
                break;

            case 4:
                // Four corners
                drawDot(g2d, width / 4, height / 4, dotSize);
                drawDot(g2d, 3 * width / 4, height / 4, dotSize);
                drawDot(g2d, width / 4, 3 * height / 4, dotSize);
                drawDot(g2d, 3 * width / 4, 3 * height / 4, dotSize);
                break;

            case 5:
                // Four corners and center
                drawDot(g2d, width / 4, height / 4, dotSize);
                drawDot(g2d, 3 * width / 4, height / 4, dotSize);
                drawDot(g2d, width / 2, height / 2, dotSize);
                drawDot(g2d, width / 4, 3 * height / 4, dotSize);
                drawDot(g2d, 3 * width / 4, 3 * height / 4, dotSize);
                break;

            case 6:
                // Six dots (three on each side)
                drawDot(g2d, width / 4, height / 4, dotSize);
                drawDot(g2d, 3 * width / 4, height / 4, dotSize);
                drawDot(g2d, width / 4, height / 2, dotSize);
                drawDot(g2d, 3 * width / 4, height / 2, dotSize);
                drawDot(g2d, width / 4, 3 * height / 4, dotSize);
                drawDot(g2d, 3 * width / 4, 3 * height / 4, dotSize);
                break;
        }

        g2d.dispose();

        // Save the image
        try {
            ImageIO.write(image, "PNG", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void drawDot(Graphics2D g2d, int x, int y, int size) {
        g2d.fillOval(x - size/2, y - size/2, size, size);
    }
}