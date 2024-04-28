package engine.utils;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * Class of utilities
 */
public class Utils {
    /**
     * Returns the maximum string length of the array.
     * If the input array is or empty, returns 0.
     *
     * @param array An array of strings.
     * @return The maximum length among the strings in the array, or 0 if the array is null or empty.
     */
    public static int getMaxStringLength(String[] array) {
        if (array == null || array.length == 0) {
            return 0;
        }

        int maxLength = array[0].length();

        for (String logo : array) {
            int currentLength = logo.length();
            if (currentLength > maxLength) {
                maxLength = currentLength;
            }
        }

        return maxLength;
    }

    /**
     * Displays a debug message in a pop-up window.
     *
     * <p>This method shows a debug message in a pop-up using a JOptionPane.</p>
     *
     * @param message The debugging message to be displayed.
     */
    public static void Debug(Object message) {
        if (message == null){
            message = "null";
        }
        System.out.println("[DEBUG] "+message);
        JOptionPane.showMessageDialog(null, message.toString());
    }
    public static String exceptionToString(Exception ex){
        // Construct the crash report message with stack trace
        StringBuilder crashReport = new StringBuilder("Crash Report:\n\n");
        crashReport.append("An unexpected error occurred.\n");
        crashReport.append("Error details:\n");
        crashReport.append(ex);
        crashReport.append("\n\nStack Trace:\n");

        // Append each line of the stack trace to the crash report
        for (StackTraceElement element : ex.getStackTrace()) {
            crashReport.append(element.toString()).append("\n");
        }
        return crashReport.toString();
    }

    /**
     * Get RGB values for a shifting rainbow effect based on elapsed time and position.
     *
     * @param elapsedTime  The elapsed time in milliseconds.
     * @param i            The position index for adjusting the effect individually.
     * @return An array of RGB values representing the color.
     */
    public static int[] getRainbow(long elapsedTime, int i) {
        // How rainbow effect works.
        // We have a "hue" variable that is the angle as HSB works with an angle, saturation, lightness
        // Rotating the angle
        // and keeping constant the saturation and the brightness
        // we can achieve a simple rainbow effect

        // Calculate the hue based on elapsed time and position
        float hue = (float) ((elapsedTime / 5000.0 - i * 0.05) % 360.0);

        // Set saturation and brightness
        // final because they don't get changed
        final float saturation = 1F;
        final float brightness = 1F;

        // Create a Color object using HSB color model
        Color c = Color.getHSBColor(hue, saturation, brightness);

        // Extract RGB values from the Color object and return them
        return new int[]{c.getRed(), c.getGreen(), c.getBlue()};
    }

    public static void waitFor(int millis){
        try {
            Thread.sleep(millis);
        }catch (InterruptedException ignored) {

        }
    }

    /**
     * Draws the text with normal colors.
     *
     * @param completeText The complete text to be drawn.
     */
    public static void drawText(TextGraphics textGraphics, String completeText){
        drawText(textGraphics, completeText, 1);
    }

    /**
     * Draws the text with normal colors.
     *
     * @param completeText The complete text to be drawn.
     */
    public static void drawText(TextGraphics textGraphics, String completeText, int offsetY){
        for (String line : completeText.split("\n")){
            textGraphics.putString(0, offsetY, line);
            offsetY++;
        }
        Utils.hideCursor(0, 0, textGraphics);
    }

    /**
     * Hides the cursor at the specified position on the screen by using inverted colors.
     * The cursor is hidden by placing space a character at the cursor position with inverted colors.
     * This causes the cursor to invert the colors again, hiding it.
     *
     * @param cursorX       The x-coordinate of the cursor position.
     * @param cursorY       The y-coordinate of the cursor position.
     * @param textGraphics  The TextGraphics object used for rendering on the screen.
     */
    public static void hideCursor(int cursorX, int cursorY, TextGraphics textGraphics) {
        // Save current colors
        TextColor foreBefore = textGraphics.getForegroundColor();
        TextColor backBefore = textGraphics.getBackgroundColor();

        // Set background and foreground colors to be swapped
        textGraphics.setForegroundColor(backBefore);
        textGraphics.setBackgroundColor(foreBefore);

        // Place a space character to hide the cursor
        textGraphics.setCharacter(cursorX, cursorY, ' ');

        // Restore original colors
        textGraphics.setBackgroundColor(backBefore);
        textGraphics.setForegroundColor(foreBefore);
    }

    public static TextColor ColorToTextColor(Color color){
        return new TextColor.RGB(color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color TextColorToAwtColor(TextColor textColor){
        return new Color(textColor.getRed(), textColor.getGreen(), textColor.getBlue());
    }

    public static void drawRect(int x, int y, int width, int height, TextGraphics textGraphics) {
        // Draw top and bottom borders
        textGraphics.putString(x, y, "\u250C"+"\u2500".repeat(width-2)+"\u2510");
        textGraphics.putString(x, y + height - 1, "\u2514"+"\u2500".repeat(width-2)+"\u2518");

        // Draw left and right borders
        String symbol = "\u2502";
        for (int i = 1; i < height - 1; i++) {
            textGraphics.putString(x, y + i, symbol);
            textGraphics.putString(x + width - 1, y + i, symbol);
        }
    }

    public static String toCamelCase(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public static String getGameTimerText(long seconds, long minutes, long elapsedTime) {
        long remainingSeconds = seconds - TimeUnit.MINUTES.toSeconds(minutes);

        boolean showColon = (elapsedTime % 1000 < 500);

        String title;
        // String format pattern: "%02d %02d"
        // - %02d: Represents an integer with a minimum width of 2 digits.
        // - Space: Adds a space between the two numbers.
        if (showColon) {
            title = "Time: " + String.format("%02d:%02d", minutes, remainingSeconds);
        } else {
            title = "Time: " + String.format("%02d %02d", minutes, remainingSeconds);
        }
        return title;
    }

    public static void displaySidebarMessage(TextGraphics textGraphics, int line, String label, String content) {
        int width = textGraphics.getSize().getColumns();
        String message = String.format(label, content) + " ".repeat(width - (label.length() + content.length()));
        textGraphics.putString(0, line, message);
    }
}
