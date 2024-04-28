package engine.utils;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.StyleSet;

import java.util.EnumSet;

/**
 * Class to store all constant values
*/
public class Constants {
    public static final String[] mainMenuOptions = new String[] {"Play", "Settings", "About", "Exit"};

    // App logo generated using https://patorjk.com/software/taag/#p=display&f=Big&t=TEMPLATE
    public static final String[] appLogo = """
  _______ ______ __  __ _____  _            _______ ______
 |__   __|  ____|  \\/  |  __ \\| |        /\\|__   __|  ____|
    | |  | |__  | \\  / | |__) | |       /  \\  | |  | |__
    | |  |  __| | |\\/| |  ___/| |      / /\\ \\ | |  |  __|
    | |  | |____| |  | | |    | |____ / ____ \\| |  | |____
    |_|  |______|_|  |_|_|    |______/_/    \\_\\_|  |______|
""".split("\n");
    // Applying the blinkStyle to a text will make it blink
    public static final StyleSet<StyleSet.Set> blinkStyle = (new StyleSet.Set()).setModifiers(EnumSet.of(SGR.BLINK));
    public static final TextColor appForeground = new TextColor.RGB(200, 200, 255);
    public static final TextColor appBackground = new TextColor.RGB(0, 0, 0);
    public static final String creatorText = "Game made by *Your Name*";
    public static final String aboutText = """
Welcome to the template for lanterna.
This is an about page.
Here you should explain how the app works.""";
    public static int GameFPS = 60;
    public static final String appDataDir = "data/";
    public static final String configPath = appDataDir+"config/exampleConfiguration.txt";
    public static String soundsDir = appDataDir+"sounds/";
}
