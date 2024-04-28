package gui.impl;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.*;
import engine.UIManager;
import gui.AbstractTerminalGUI;
import gui.ITerminalGUI;
import engine.utils.Constants;
import engine.utils.Utils;

import java.io.IOException;

/**
 * GUI implementation for a "Settings" screen.
 */
public class SettingsGUI extends AbstractTerminalGUI implements ITerminalGUI {
    private static final String GUI_TITLE = "Settings";
    private final MenuPopupWindow window;
    private final TextBox nameTextBox;
    private final TextBox surnameTextBox;

    public SettingsGUI(UIManager uiManager) {
        super(uiManager.getTerminal());
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

        window = new MenuPopupWindow(uiManager.mainPanel);
        window.setTheme(uiManager.getWindowTheme());
        Panel windowBody = new Panel();
        windowBody.addComponent(new Label(GUI_TITLE));
        windowBody.addComponent(new EmptySpace(new TerminalSize(1, 1))); // Add some empty space

        // Call the createTextBox function defined below
        nameTextBox = createTextBox("Name:", name, windowBody);
        surnameTextBox = createTextBox("Surname:", surname, windowBody);

        // Create the buttons for closing the settings gui
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        addButton(buttonsPanel, "Save", this::saveAndClose, TextColor.ANSI.GREEN);
        addButton(buttonsPanel, "Exit", this::onClose, TextColor.ANSI.RED);

        windowBody.addComponent(buttonsPanel);

        window.setComponent(windowBody);
        uiManager.setHints(window);
    }

    /**
     * Creates a text box with a label and adds them to the specified container panel.
     *
     * @param labelText      The label text for the text box.
     * @param initialContent The initial content (placeholder text) of the text box.
     * @param container      The panel where the label and text box will be added.
     * @return The created TextBox object, which can be used to access properties such as text.
     * @throws IllegalArgumentException If the label, initial content, or container is null.
     */
    private TextBox createTextBox(String labelText, String initialContent, Panel container) {
        if (labelText == null || initialContent == null || container == null) {
            throw new IllegalArgumentException("Label, initial content, and container must not be null.");
        }

        // Create a horizontal panel to contain the label and text box
        Panel panel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        // Create label and textbox objects
        Label label = new Label(labelText);
        TextBox textBox = new TextBox(initialContent);

        // Add label and panel to the gui
        panel.addComponent(label);
        panel.addComponent(textBox);

        // Add the panel containing the label and text box to the specified container panel
        container.addComponent(panel);
        return textBox;
    }

    /**
     * Creates a button with the specified label and action and adds it to the given panel.
     *
     * @param panel The panel where the button will be added.
     * @param label The text to be displayed on the button.
     * @param action The action to be performed when the button is clicked.
     * @param color The color theme for the button.
     * @throws IllegalArgumentException If the panel or label is null.
     */
    private void addButton(Panel panel, String label, Runnable action, TextColor color) {
        if (panel == null || label == null) {
            throw new IllegalArgumentException("Panel and label must not be null.");
        }

        // Create a button with the specified label and action
        Button button = new Button(label, action);
        // Set the color theme for the button
        button.setTheme(new SimpleTheme(color, Constants.appBackground, SGR.BOLD));
        // Add the button to the panel
        panel.addComponent(button);
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
