package engine;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;
import gui.impl.MainMenuGUI;
import utils.ConfigAPI;
import utils.Constants;
import utils.impl.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UIManager {
    // Store the terminal variable as final because it doesn't need to be modified
    private final Terminal terminal;
    // Screen the object where everything will be drawn on
    private final Screen screen;
    // TextGraphics contains all the methods necessary to draw on the screen
    private final TextGraphics textGraphics;
    // Gui with multiple window support
    public final MultiWindowTextGUI gui;
    // Panels can store more items, windows only one,
    // by putting a panel into a window you can put multiple items inside a window
    public final Panel mainPanel;
    // List of hints to tell lanterna how to handle windows, such as making it centered or removing borders
    private final List<Window.Hint> hints = new ArrayList<>();
    // Configuration example
    public Config config = new Config();
    public ConfigAPI configAPI;
    public UIManager(Terminal terminal) throws IOException {
        // Setup variables
        this.terminal = terminal;

        // Create a new ConfigAPI object
        // using the file defined in the Constants class
        configAPI = new ConfigAPI(Constants.configPath);

        configAPI.populateConfig(config); // Load the data of the file in to the config object

        // Now that the configuration is loaded, you can use config.name = "newName"
        // and it will be automatically saved when the app exits

        // For terminal gui:

        // Start a screen, the screen class can be used to draw more easly objects on the screen
        screen = new TerminalScreen(terminal);
        // Create a textGraphics connected to the screen
        textGraphics = screen.newTextGraphics();

        // For window (more advanced text gui elements such as buttons, checkboxes, and more):
        Panel guiBackground = new Panel();
        guiBackground.setTheme(getWindowTheme());
        gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), guiBackground);
        BasicWindow mainWindow = new BasicWindow();
        mainPanel = new Panel();
        mainWindow.setComponent(mainPanel);
        hints.add(Window.Hint.CENTERED);
        hints.add(Window.Hint.NO_POST_RENDERING);

        mainWindow.setHints(hints);

        gui.addWindow(mainWindow);
        // Now that all variables are assigned we init the screen
        screen.startScreen();
        // Clear the screen
        screen.clear();
    }

    // Set hints for a given window.
    // Hints are suggestions for lanterna on how to handle a window, like center it, remove borders, and more
    public void setHints(MenuPopupWindow window) {
        window.setHints(hints);
    }

    // To apply a color to the textGraphics, can later be changed to better fit your theme
    // So you can change colors in one places, and it will change everywhere
    public void applyThemeColors(TextGraphics textGraphics){
        textGraphics.setForegroundColor(Constants.appForeground);
        textGraphics.setBackgroundColor(Constants.appBackground);
    }

    // Returns a SimpleTheme object used by lanterna to style a window
    public SimpleTheme getWindowTheme(){
        return new SimpleTheme(Constants.appForeground, Constants.appBackground);
    }

    // Method to show the main gui
    public void showMainScreen() throws IOException, IllegalAccessException {
        // Create a new MainMenuGUI
        MainMenuGUI mainMenuGUI = new MainMenuGUI(this);
        // Display it
        mainMenuGUI.show();
        // The app now is being closed, so save the config

        // Take all fields set in the Config instance
        // and put them into the ConfigAPI class
        configAPI.populateFromConfig(config);
        // Save to disk the configuration
        configAPI.save();
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
