package gui.impl;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import engine.UIManager;
import gui.AbstractTerminalGUI;
import gui.ITerminalGUI;
import engine.utils.Constants;
import engine.utils.Utils;

import java.io.IOException;

/**
 * GUI implementation for an "About" screen.
 */
public class AboutGUI extends AbstractTerminalGUI implements ITerminalGUI {
    private final long startTime = System.currentTimeMillis();
    private final boolean[] rgbEnabled = new boolean[]{true};
    private static final String GUI_TITLE = "About";
    private static final int SMALL_SLEEP = 30;
    private static final int LONG_SLEEP = 200;

    /**
     * Constructor for the AboutGUI.
     *
     * @param uiManager The UIManager giving access to the terminal and screen.
     */
    public AboutGUI(UIManager uiManager) {
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
                    } else if (choice.getKeyType() == KeyType.Tab) {
                        // Toggle RGB mode on Tab key press
                        rgbEnabled[0] = !rgbEnabled[0];
                    }
                }
            }
        }).start();

        while (running[0]) {
            // Draw the GUI
            draw();
            // RGB needs a higher frame-rate; a static text doesn't need lots of updates
            if (rgbEnabled[0]){
                Utils.waitFor(SMALL_SLEEP);
            } else {
                Utils.waitFor(LONG_SLEEP);
            }
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
        String completeText = getTitle() + Constants.aboutText;

        if (rgbEnabled[0]){
            drawRainbow(completeText);
        } else {
            textGraphics.setForegroundColor(uiManager.getThemeForeground());
            textGraphics.setBackgroundColor(uiManager.getThemeBackground());
            Utils.drawText(textGraphics, completeText, 0);
        }

        textGraphics.disableModifiers(SGR.BOLD);
        textGraphics.setForegroundColor(uiManager.getThemeForeground());
        textGraphics.setBackgroundColor(uiManager.getThemeBackground());
        textGraphics.putString(0, getTerminalHeight()-1, "Press \"Tab\" to toggle RGB");
        screen.refresh();
    }

    /**
     * Draws the text with a rainbow effect.
     *
     * @param completeText The complete text to be drawn.
     */
    private void drawRainbow(String completeText){
        int offsetY = 0;
        int offsetX = 0;
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        textGraphics.enableModifiers(SGR.BOLD);

        for (int i = 0; i < completeText.length(); i++) {
            int[] rgb = Utils.getRainbow(elapsedTime, i);

            textGraphics.setForegroundColor(new TextColor.RGB(rgb[0], rgb[1], rgb[2]));

            textGraphics.putString(offsetX, offsetY, String.valueOf(completeText.charAt(i)));
            Utils.hideCursor(0, 0, textGraphics);
            offsetX++;
            if (completeText.charAt(i) == '\n'){
                offsetY++;
                offsetX = 0;
            }
        }
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
