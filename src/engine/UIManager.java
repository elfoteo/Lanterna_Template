package engine;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import gui.impl.MainMenuGUI;
import utils.Constants;

import java.io.IOException;

public class UIManager {
    // Store the terminal variable as final because it doesn't need to be modified
    private final Terminal terminal;
    // Screen the object where everything will be drawn on
    private final Screen screen;
    // TextGraphics contains all the methods necessary to draw on the screen
    private final TextGraphics textGraphics;

    public UIManager(Terminal terminal) throws IOException {
        this.terminal = terminal;
        // Start a screen, the screen class can be used to draw more easly objects on the screen
        screen = new TerminalScreen(terminal);
        // Create a textGraphics connected to the screen
        textGraphics = screen.newTextGraphics();
        // Now that all variables are assigned we init the screen
        screen.startScreen();
        // Clear the screen
        screen.clear();
    }

    // To apply a color to the textGraphics, can later be changed to better fit your theme
    // So you can change colors in one places, and it will change everywhere
    public void applyThemeColors(TextGraphics textGraphics){
        textGraphics.setForegroundColor(Constants.appForeground);
        textGraphics.setBackgroundColor(Constants.appBackground);
    }

    // Method to show the main gui
    public void showMainScreen() throws IOException {
        MainMenuGUI mainMenuGUI = new MainMenuGUI(this);
        mainMenuGUI.show();
    }

    // Getter for terminal to allow other classes to access the terminal instance
    public Terminal getTerminal() {
        return terminal;
    }

    // Getter for screen to allow other classes to access the screen instance
    public Screen getScreen() {
        return screen;
    }

    // Getter for the textGraphics to allow other classes to access the textGraphics instance
    public TextGraphics getTextGraphics() {
        return textGraphics;
    }

    public TextColor getThemeForeground() {
        return Constants.appForeground;
    }

    public TextColor getThemeBackground() {
        return Constants.appBackground;
    }
}
