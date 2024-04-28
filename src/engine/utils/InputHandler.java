package engine.utils;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

/**
 * The InputHandler class allows for asynchronous keystroke handling.
 * This code should not be modified unless you know what you are doing.
 */
public class InputHandler {
    private final Screen screen;
    private volatile KeyStroke lastChar;
    private Thread inputThread;
    private boolean[] handled = new boolean[] {false};

    public InputHandler(Screen screen) {
        this.screen = screen;
    }

    /**
     * Starts the input handling thread.
     * This method starts a new thread that continuously reads keystrokes from the screen
     * and updates the last keystroke received.
     */
    public void startThread() {
        inputThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    lastChar = screen.readInput();
                    handled[0] = false;
                } catch (IOException e) {

                }
            }
        });
        inputThread.start();
    }

    /**
     * Stops the input handling thread.
     * This method interrupts the input handling thread and waits for it to finish.
     */
    public void stopThread() {
        inputThread.interrupt(); // Interrupt the input thread
        try {
            inputThread.join(); // Wait for inputThread to finish
        } catch (InterruptedException e) {

        }
    }

    /**
     * Retrieves the last keystroke received and marks it as handled.
     * This method returns the last keystroke received since the last call to handleInput()
     * and marks it as handled to prevent duplicate processing.
     *
     * @return The last keystroke received, or null if no new keystroke is available.
     */
    public KeyStroke handleInput() {
        if (!handled[0]){
            handled[0] = true;
            return lastChar;
        }
        return null;
    }
}
