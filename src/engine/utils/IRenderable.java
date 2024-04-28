package engine.utils;

import com.googlecode.lanterna.graphics.TextGraphics;

/**
 * Simple interface to ensure that the .draw method is implemented
 * This code should not be changed unless you know what you are doing
 */
public interface IRenderable {
    /**
     * Draw method
     * @param textGraphics The surface where the object will be rendered
     */
    void draw(TextGraphics textGraphics);
}
