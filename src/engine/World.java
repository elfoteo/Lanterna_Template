package engine;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import engine.utils.IRenderable;
import engine.utils.Utils;

/**
 * The Word class implements the logic for the world in your game
 */
public class World implements IRenderable {
    private boolean running; // Variable to determine if the world is still running or not
    private final ExamplePlayer myPlayer; // Player class example
    private boolean isHintTextDisplayed = true;

    // Constructor
    public World(int screenWidth, int screenHeight){
        // Find the center of the screen
        int centerX = screenWidth/2;
        int centerY = screenHeight/2;
        // Create a player object positioned at the center of the screen
        myPlayer = new ExamplePlayer(centerX, centerY);
        running = true;
    }

    /**
     * Draws the world on a given TextGraphics
     */
    @Override
    public void draw(TextGraphics textGraphics) {
        // Hide the cursor
        Utils.hideCursor(0, 0, textGraphics);
        // Draw the player
        myPlayer.draw(textGraphics);
        // Draw the hint text if necessary
        if (isHintTextDisplayed){
            Utils.drawText(textGraphics, "[Hint] Try to move around using the arrow keys");
        }
    }

    /**
     * This method should contain all the processing of your game, not graphical updates!
     * Examples:
     *  - Moving players, enemies, goals
     *  - Manipulating arrays, lists, variables
     *  - Manipulating the world in general
     * @param keyPress the last keypress if any
     */
    public void update(KeyStroke keyPress) {
        /*
        You could do a check and throw an exception if the world is no longer running, and this method is called
        if (!running){
            throw new IllegalStateException("The world is no longer running, and this method cannot be called.");
        }
        */

        // Use a switch statement to handle different key types
        if (keyPress != null){
            switch (keyPress.getKeyType()) {
                case ArrowUp:
                    // Move the player up
                    myPlayer.move(0, -1);
                    break;
                case ArrowDown:
                    // Move the player down
                    myPlayer.move(0, 1);
                    break;
                case ArrowLeft:
                    // Move the player left
                    myPlayer.move(-1, 0);
                    break;
                case ArrowRight:
                    // Move the player right
                    myPlayer.move(1, 0);
                    break;
                case EOF, Escape:
                    // Stop the game
                    running = false;
                    break;
            }
            isHintTextDisplayed = false;
        }
    }

    public boolean isRunning() {
        return running;
    }
}
