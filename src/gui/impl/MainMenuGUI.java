package gui.impl;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import engine.UIManager;
import gui.AbstractTerminalGUI;
import gui.ITerminalGUI;
import engine.utils.Constants;
import engine.utils.Utils;

import java.io.IOException;

/**
 * Represents the main menu GUI.
 */
public class MainMenuGUI extends AbstractTerminalGUI implements ITerminalGUI {

    private final Terminal terminal;
    private int selectedIndex = 0;

    /**
     * Constructor for the MainMenuGUI.
     *
     * @param uiManager The UIManager giving access to the terminal and screen.
     */
    public MainMenuGUI(UIManager uiManager) {
        super(uiManager.getTerminal());
        this.uiManager = uiManager;
        this.terminal = uiManager.getTerminal();
        this.screen = uiManager.getScreen();
        this.textGraphics = uiManager.getTextGraphics();
    }

    @Override
    public void show() throws IOException {
        // Call super method to register all the necessary stuff
        super.show();

        boolean running = true;

        while (running) {
            draw();

            KeyStroke choice = screen.readInput();
            if (choice.getKeyType() == KeyType.EOF) {
                break;
            }

            if (choice.getKeyType() == KeyType.ArrowDown) {
                selectedIndex++;
                if (selectedIndex > Constants.mainMenuOptions.length - 1) {
                    selectedIndex = 0;
                }
            } else if (choice.getKeyType() == KeyType.ArrowUp) {
                selectedIndex--;
                if (selectedIndex < 0) {
                    selectedIndex = Constants.mainMenuOptions.length - 1;
                }
            } else if (choice.getKeyType() == KeyType.Enter) {
                switch (Constants.mainMenuOptions[selectedIndex]) {
                    case "Play":
                        GameGUI game = new GameGUI(uiManager);
                        openGUI(game);
                        break;
                    case "Settings":
                        openGUI(new SettingsGUI(uiManager));
                        break;
                    case "About":
                        AboutGUI myAboutGui = new AboutGUI(uiManager);
                        openGUI(myAboutGui);
                        break;
                    case "Exit":
                        running = false;
                        break;
                }
            }
        }
        // GUI got closed
        onClose();
    }

    @Override
    public void onClose() {
        // Call super method
        super.onClose();
    }

    @Override
    public void draw() throws IOException {
        super.draw();

        // screen.doResizeIfNecessary() returns size if the screen has been resized, null if not
        // if the screen has been resized clear the screen
        if (screen.doResizeIfNecessary() != null) {
            screen.clear();
        }
        uiManager.applyThemeColors(textGraphics);
        // Add logo
        int x = Utils.getMaxStringLength(Constants.appLogo);
        int y = 1;
        for (String logoLine : Constants.appLogo) {
            textGraphics.putString(getTerminalWidth() / 2 - x / 2, y, logoLine);
            y++;
        }
        // Add creator text
        int xOffset = 0;

        for (String segment : Constants.creatorText.split("\\*")) {
            // Display the non-star segment without any modifiers
            uiManager.applyThemeColors(textGraphics);
            textGraphics.putString(getTerminalWidth() - Constants.creatorText.length() - 1 + xOffset,
                    terminal.getTerminalSize().getRows() - 1, segment);
            xOffset += segment.length();

            // Apply the blinking style to the next star
            textGraphics.setStyleFrom(Constants.blinkStyle);
            uiManager.applyThemeColors(textGraphics);
            // Display the star with the blinking style
            textGraphics.putString(getTerminalWidth() - Constants.creatorText.length() - 1 + xOffset,
                    terminal.getTerminalSize().getRows() - 1, "*");

            // Move the xOffset to the next position after the star
            textGraphics.clearModifiers();
            xOffset++;
            screen.refresh();
        }

        // Hide cursor
        Utils.hideCursor(0, 0, textGraphics);
        // Clear any modifiers after the loop
        textGraphics.clearModifiers();
        screen.refresh();
        uiManager.applyThemeColors(textGraphics);

        x = Utils.getMaxStringLength(Constants.mainMenuOptions) + 2;
        y = Constants.appLogo.length + 2;
        int counter = 0;
        for (String menuLine : Constants.mainMenuOptions) {
            if (selectedIndex == counter) {
                textGraphics.putString(getTerminalWidth() / 2 - x / 2, y, "o " + menuLine);
            } else {
                textGraphics.putString(getTerminalWidth() / 2 - x / 2, y, "- " + menuLine);
            }
            y++;
            counter++;
        }
        screen.refresh();
    }
}
