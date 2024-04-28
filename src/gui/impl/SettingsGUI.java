package gui.impl;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
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
    private final MenuPopupWindow window;
    // UI Components
    private final TextBox nameTextBox; // This needs to be accessed later, so we save it globally
    private final TextBox surnameTextBox; // This needs to be accessed later, so we save it globally
    /**
     * Constructor for the SettingsGUI.
     *
     * @param uiManager The UIManager giving access to the terminal and screen.
     */
    public SettingsGUI(UIManager uiManager) {
        super(uiManager.getTerminal());
        // Set instance variables
        this.screen = uiManager.getScreen();
        this.textGraphics = uiManager.getTextGraphics();
        this.uiManager = uiManager;

        // Set local variables
        String name = "";
        String surname = "";

        if (uiManager.config.name != null){
            name = uiManager.config.name;
        }
        if (uiManager.config.surname != null){
            surname = uiManager.config.surname;
        }


        /* =============== Window =============== */
        window = new MenuPopupWindow(uiManager.mainPanel);
        window.setTheme(uiManager.getWindowTheme());
        Panel windowBody = new Panel();
        windowBody.addComponent(new Label(GUI_TITLE));
        windowBody.addComponent(new EmptySpace(new TerminalSize(1, 1))); // Add some empty space

        /* =============== Name =============== */
        Panel namePanel = new Panel(new LinearLayout(Direction.HORIZONTAL)); // Create a container with a horizontal layout

        Label nameLabel = new Label("Name:"); // Create a label
        // Under the label, create a textBox for the user to set their name
        // The TextBox initial content is the name found in the configuration
        nameTextBox = new TextBox(name);

        // Add the objects to the nameOptions panel
        namePanel.addComponent(nameLabel);
        namePanel.addComponent(nameTextBox);

        windowBody.addComponent(namePanel); // Add the nameOptions container to the window body
        /* =============== Surname =============== */
        Panel surnamePanel = new Panel(new LinearLayout(Direction.HORIZONTAL)); // Create a container with a horizontal layout

        Label surnameLabel = new Label("Surname:"); // Create a label
        // Under the label, create a textBox for the user to set their name
        // The TextBox initial content is the surname found in the configuration
        surnameTextBox = new TextBox(surname);

        // Add the objects to the nameOptions panel
        surnamePanel.addComponent(surnameLabel);
        surnamePanel.addComponent(surnameTextBox);

        windowBody.addComponent(surnamePanel); // Add the nameOptions container to the window body
        /* =============== Buttons =============== */
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        // Save button
        // this::onClose means call the class method onClose, same thing of onClose() but inline
        Button saveButton = new Button("Save", this::saveAndClose); // Create a button that when clicked closes the current menu
        // Make the button green (optional)
        saveButton.setTheme(new SimpleTheme(TextColor.ANSI.GREEN, Constants.appBackground, SGR.BOLD));
        // Exit button
        Button exitButton = new Button("Exit", this::onClose); // Create a button that when clicked closes the current menu
        // Make the button red (optional)
        exitButton.setTheme(new SimpleTheme(TextColor.ANSI.RED, Constants.appBackground, SGR.BOLD));

        buttonsPanel.addComponent(saveButton); // Add the button to the window body
        buttonsPanel.addComponent(exitButton); // Add the button to the window body

        windowBody.addComponent(buttonsPanel); // Add the button panel to the window body

        window.setComponent(windowBody);
        uiManager.setHints(window);
    }

    private void saveAndClose(){
        uiManager.config.name = nameTextBox.getText();
        uiManager.config.surname = surnameTextBox.getText();
        try{
            uiManager.configAPI.save();
        }
        catch (Exception ignore){
            Utils.Debug("Failed to save settings to disk");
        }
        onClose(); // Close
    }

    @Override
    public void show() throws IOException {
        super.show();
        uiManager.gui.addWindowAndWait(window);
    }

    @Override
    public void onClose() {
        super.onClose();
        // Close the window
        window.close();
        // Clear the screen
        screen.clear();
    }
}
