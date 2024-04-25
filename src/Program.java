import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import engine.UIManager;
import utils.Utils;

import java.io.IOException;

public class Program {
    public static void main(String[] args) throws IOException {
        Terminal terminal;
        try {
            // Create factory, needed to set preferences for the swing window
            DefaultTerminalFactory factory = new DefaultTerminalFactory();
            factory.setTerminalEmulatorTitle("You App Title");
            // Create terminal
            terminal = factory.createTerminal();
            terminal.enterPrivateMode();

            // Let's keep the program class small
            // To do that we create another class to handle all the UI
            UIManager uiManager = new UIManager(terminal);
            uiManager.showMainScreen();
        }
        catch (Exception ex){
            String crashReport = Utils.exceptionToString(ex);

            // Show the crash report in a message box
            Utils.Debug(crashReport);
            return; // Immediately stop the program
        }

        terminal.exitPrivateMode();
        terminal.close();
    }
}