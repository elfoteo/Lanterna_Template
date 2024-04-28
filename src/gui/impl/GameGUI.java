package gui.impl;

import com.googlecode.lanterna.input.KeyStroke;
import engine.UIManager;
import engine.World;
import gui.AbstractTerminalGUI;
import gui.ITerminalGUI;
import utils.Constants;
import utils.InputHandler;
import utils.Utils;

import java.io.IOException;

/**
 * GUI implementation for the main game screen.
 */
public class GameGUI extends AbstractTerminalGUI implements ITerminalGUI {
    private World exampleWorld; // Should be renamed to "world"
    private final int FPS = 60;
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

        int screenWidth = screen.getTerminalSize().getColumns();
        int screenHeight = screen.getTerminalSize().getRows();
        exampleWorld = new World(screenWidth, screenHeight); // Initialize the world passing the necessary parameters

        // InputHandler is a class to handle input asynchronously
        InputHandler inputHandler = new InputHandler(screen);
        inputHandler.startThread();

        // Main game loop
        while (exampleWorld.isRunning()){
            // Draw everything on screen
            draw();
            // Get the key press from the user and pass it to the world.
            // If you want you can handle the keypress here, is a personal preference
            KeyStroke keyPress = inputHandler.handleInput();
            exampleWorld.update(keyPress);

            // This determines the FPS of the game, avoiding CPU burn
            // 1000milliseconds divided by the fixed game frame-rate
            Utils.waitFor(1000 / Constants.GameFPS);
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

        // Draw the world (including the player)
        exampleWorld.draw(textGraphics);
        // Refresh the screen to see the actual changes
        screen.refresh();
    }
}
