package engine;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

/**
 * Example of a player class implementation.
 * For this example, we will have a player that can be drawn on the screen and can be moved around
 */
public class ExamplePlayer {
    // Position X
    private int posX;
    // Position Y
    private int posY;
    // Let's add a player color just for fun
    private final TextColor myColor = new TextColor.RGB(140, 100, 255);

    /**
     * Constructor for MyPlayer class
     *
     * @param x The starting x position
     * @param y The starting y position
     */
    public ExamplePlayer(int x, int y){
        posX = x;
        posY = y;
    }

    public void move(int amountX, int amountY){
        posX += amountX;
        posY += amountY;
    }

    public void draw(TextGraphics textGraphics){
        // Set the foreground color to myColor
        textGraphics.setForegroundColor(myColor); // This is optional
        // Draw the actual player on the screen
        textGraphics.putString(posX, posY, "*");
    }

    public int getX() {
        return posX;
    }

    public int getY() {
        return posY;
    }
}
