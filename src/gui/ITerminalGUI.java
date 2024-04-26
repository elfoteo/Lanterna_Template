package gui;

import java.io.IOException;

/**
 * This interface represents a terminal GUI.
 * This code should not be modified unless you know what you are doing.
 */
public interface ITerminalGUI {

    /**
     * Method called when the terminal is resized.
     */
    void onResize();

    /**
     * Method to draw the GUI.
     * @throws IOException if an I/O error occurs
     */
    void draw() throws IOException;

    /**
     * Method to show the GUI.
     * Called only one time when the GUI opens.
     * @throws IOException if an I/O error occurs
     */
    void show() throws IOException;

    /**
     * Method called when the GUI is closed.
     */
    void onClose();

    /**
     * Method to open a new GUI.
     * @param terminalGUI the new terminal GUI to open
     * @throws IOException if an I/O error occurs
     */
    void openGUI(ITerminalGUI terminalGUI) throws IOException;
}
