package gui;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import engine.utils.TerminalResizeEventHandler;
import engine.UIManager;
import engine.utils.Utils;

import java.io.IOException;

/**
 * An abstract implementation of the ITerminalGUI interface providing common functionality and terminal resizing.
 * This code should not be modified unless you know what you are doing.
 */
public class AbstractTerminalGUI implements ITerminalGUI {

    protected Screen screen;
    protected TextGraphics textGraphics;
    protected UIManager uiManager;
    private TerminalResizeEventHandler terminalResizeEventHandler;
    protected boolean resizePaused = false;

    /**
     * Constructor for AbstractTerminalGUI.
     *
     * @param terminal The terminal instance to associate with this GUI.
     */
    public AbstractTerminalGUI(Terminal terminal) {
        setup(terminal);
    }

    /**
     * Initialize the GUI setup with the given terminal.
     *
     * @param terminal The terminal instance to associate with this GUI.
     */
    private void setup(Terminal terminal) {
        try {
            terminalResizeEventHandler = new TerminalResizeEventHandler(terminal.getTerminalSize());
            terminal.addResizeListener(terminalResizeEventHandler);
            // If an exception is thrown, the terminal will not be resizable,
            // as .subscribe will never be called. Consider logging the exception.
            // Register the onResize event
            subscribe();
        } catch (Exception ignored) {
            // Handle exception
        }
    }

    /**
     * Resize method called in a separate thread to handle terminal resizing.
     */
    private void resize() {
        new Thread(() -> {
            try {
                if (!resizePaused) {
                    // Clear the screen
                    screen.clear();
                    // Resize screen
                    screen.doResizeIfNecessary();

                    try {
                        onResize();
                    } catch (Exception ignore) {
                        // Handle exception
                    }

                    try {
                        // Redraw everything
                        draw();
                    } catch (Exception ignore) {
                        // Handle exception
                    }
                }
            } catch (Exception ex) {
                Utils.Debug(Utils.exceptionToString(ex));
            }
        }).start();
    }

    @Override
    public void onResize() {
        // Gets called when the window gets resized
    }

    @Override
    public void draw() throws IOException {
        // Fill the background with the theme background color
        fillBackground();
    }

    /**
     * Fills the background with the theme background color.
     */
    protected void fillBackground() {
        // Set's the background color to the theme background and fills it
        textGraphics.setBackgroundColor(uiManager.getThemeBackground());
        textGraphics.fill(' ');
    }

    @Override
    public void onClose() {
        resizePaused = true;
        unsubscribe();
        terminalResizeEventHandler = null;
    }

    @Override
    public void openGUI(ITerminalGUI gui) throws IOException {
        // While the new GUI is open, we need to stop the current resize event handler
        resizePaused = true;
        // Open the new GUI
        gui.show();
        // Then add it back
        resizePaused = false;
    }

    /**
     * Unsubscribe from resize events.
     */
    private void unsubscribe() {
        terminalResizeEventHandler.unsubscribe(this::resize);
    }

    /**
     * Subscribe to resize events.
     */
    private void subscribe() {
        terminalResizeEventHandler.subscribe(this::resize);
    }

    @Override
    public void show() throws IOException {
        // Implementation specific to each concrete class
    }

    /**
     * Get the height of the terminal.
     *
     * @return The height of the terminal.
     */
    public int getTerminalHeight() {
        return terminalResizeEventHandler.getLastKnownSize().getRows();
    }

    /**
     * Get the width of the terminal.
     *
     * @return The width of the terminal.
     */
    public int getTerminalWidth() {
        return terminalResizeEventHandler.getLastKnownSize().getColumns();
    }
}
