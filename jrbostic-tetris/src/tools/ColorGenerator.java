/*
 * Tetris - TCSS305 - Autumn 2013
 */

package tools;

import java.awt.Color;
import java.util.Random;

/**
 * A static color generator class.
 * 
 * @author Jesse Bostic
 * @version Autumn 2013
 */
public final class ColorGenerator {
    
    /**
     * Random object to generate int values for color codes.
     */
    private static final Random RAND = new Random();
    
    /**
     * The max value of red, green, blue, and alpha.
     */
    private static final int MAX_VAL = 256;
    
    /**
     * Constructor ensuring uninstantiablity.
     */
    private ColorGenerator() {
       //uninstantiable
    }
    
    /**
     * Generates and returns random color.
     * 
     * @return random color object
     */
    public static Color generateColor() {
        final int red = RAND.nextInt(MAX_VAL);
        final int green = RAND.nextInt(MAX_VAL);
        final int blue = RAND.nextInt(MAX_VAL);
        final int alpha = RAND.nextInt(MAX_VAL);
        return new Color(red, green, blue, alpha);
    }
}
