/*
 * Tetris - TCSS305 - Autumn 2013
 */

package view;

import java.awt.EventQueue;

/**
 * Class for superfluously initiating tetris game object.
 * 
 * @author Jesse Bostic
 * @version Autumn 2013
 */
public final class TetrisMain {
    
    /**
     * Constructor to ensure uninstantiability.
     */
    private TetrisMain() {
        //ensure uninstantiability
    }
    
    /**
     * Main method for kicking off program.
     * 
     * @param the_args standard accepted argument array for main
     */
    public static void main(final String[] the_args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TetrisGame();
            }
        });
    }
}
