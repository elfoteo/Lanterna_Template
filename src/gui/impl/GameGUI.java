package gui.impl;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.input.KeyStroke;
import engine.ExamplePlayer;
import engine.UIManager;
import gui.AbstractTerminalGUI;
import gui.ITerminalGUI;
import utils.InputHandler;
import utils.Utils;

import java.io.IOException;

/**
 * GUI implementation for the main game screen.
 */
public class GameGUI extends AbstractTerminalGUI implements ITerminalGUI {
    private boolean gameRunning = true;
    private ExamplePlayer examplePlayer;
    /**
     * Constructor for the GameGUI.
     *
     * @param uiManager The UIManager giving access to the terminal and screen.
     */
    public GameGUI(UIManager uiManager) {
        super(uiManager.getTerminal());
        this.screen = uiManager.getScreen();
        this.textGraphics = uiManager.getTextGraphics();
        this.uiManager = uiManager;
    }

    @Override
    public void show() throws IOException {
        super.show();

        screen.clear();
        Utils.hideCursor(0, 0, textGraphics);

        int centerX = screen.getTerminalSize().getColumns()/2;
        int centerY = screen.getTerminalSize().getRows()/2;
        examplePlayer = new ExamplePlayer(centerX, centerY);

        // InputHandler is a class to handle input asynchronously
        InputHandler inputHandler = new InputHandler(screen);
        inputHandler.startThread();

        while (gameRunning){
            // Draw everything on screen
            draw();
            // Get the key press from the user if any
            KeyStroke choice = inputHandler.handleInput();
            if (choice != null){
                // If the user has pressed any key
                switch (choice.getKeyType()) {
                    // Handle arrow movement
                    case ArrowUp -> examplePlayer.move(0, -1);
                    case ArrowDown -> examplePlayer.move(0, 1);
                    case ArrowLeft -> examplePlayer.move(-1, 0);
                    case ArrowRight -> examplePlayer.move(1, 0);
                    case EOF, Escape -> gameRunning = false;
                }
            }

            // Don't burn the CPU
            Utils.waitFor(10);
        }
        // Stop the input handler thread
        inputHandler.stopThread();
        // Call the close method for the text gui
        onClose();
    }

    @Override
    public void onClose() {
        super.onClose();
        screen.clear();
    }

    @Override
    public void draw() throws IOException {
        super.draw();
        textGraphics.setForegroundColor(uiManager.getThemeForeground());
        textGraphics.setBackgroundColor(uiManager.getThemeBackground());
        drawText("""
                This is an example for a game gui.
                Here you should put the logic for your game.
                Use your arrows keys to move the example player.
                The player position is:\s""" + examplePlayer.getX()+", "+ examplePlayer.getY());

        // If there is a player instance, then draw the player
        if (examplePlayer != null){
            examplePlayer.draw(textGraphics);
        }

        textGraphics.disableModifiers(SGR.BOLD);
        textGraphics.setForegroundColor(uiManager.getThemeForeground());
        textGraphics.setBackgroundColor(uiManager.getThemeBackground());
        textGraphics.putString(0, getTerminalHeight()-1, "Press \"Escape\" to exit");
        screen.refresh();
    }

    /**
     * Draws the text with normal colors.
     *
     * @param completeText The complete text to be drawn.
     */
    private void drawText(String completeText){
        int offsetY = 1;
        for (String line : completeText.split("\n")){
            textGraphics.putString(0, offsetY, line);
            offsetY++;
        }
        Utils.hideCursor(0, 0, textGraphics);
    }
}
