package gui.impl;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import engine.UIManager;
import gui.AbstractTerminalGUI;
import gui.ITerminalGUI;
import utils.Constants;
import utils.Utils;

import java.io.IOException;

/**
 * GUI implementation for a "Settings" screen.
 */
public class SettingsGUI extends AbstractTerminalGUI implements ITerminalGUI {
    private static final String GUI_TITLE = "Settings";

    /**
     * Constructor for the AboutGUI.
     *
     * @param uiManager The UIManager giving access to the terminal and screen.
     */
    public SettingsGUI(UIManager uiManager) {
        super(uiManager.getTerminal());
        this.screen = uiManager.getScreen();
        this.textGraphics = uiManager.getTextGraphics();
        this.uiManager = uiManager;
    }

    @Override
    public void show() throws IOException {
        super.show();
        boolean[] running = new boolean[]{true};

        screen.clear();
        Utils.hideCursor(0, 0, textGraphics);

        // Background thread for handling user input during the display
        new Thread(() -> {
            while (running[0]) {
                KeyStroke choice = null;
                try {
                    choice = screen.readInput();
                } catch (IOException ignored) {}

                if (choice != null) {
                    // Terminate the loop on EOF or Escape key press
                    if (choice.getKeyType() == KeyType.EOF || choice.getKeyType() == KeyType.Escape) {
                        running[0] = false;
                    }
                }
            }
        }).start();

        while (running[0]) {
            // Draw the GUI
            draw();
            // Wait some time to avoid CPU Burn
            Utils.waitFor(200);
        }
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
        String completeText = getTitle() + "Example for a settings gui, you should put your actual settings here if any.\nExamples: Volume of music, Input preferences, ...";

        textGraphics.setForegroundColor(uiManager.getThemeForeground());
        textGraphics.setBackgroundColor(uiManager.getThemeBackground());
        drawText(completeText);

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
        int offsetY = 0;
        for (String line : completeText.split("\n")){
            textGraphics.putString(0, offsetY, line);
            offsetY++;
        }
        Utils.hideCursor(0, 0, textGraphics);
    }

    /**
     * Generates a centered title string.
     *
     * @return The formatted title string.
     */
    private String getTitle(){
        return " ".repeat(getTerminalWidth()/2 - GUI_TITLE.length()/2) + GUI_TITLE + "\n\n";
    }
}
