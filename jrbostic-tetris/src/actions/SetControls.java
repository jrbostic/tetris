/*
 * Tetris - TCSS305 - Autumn 2013
 */

package actions;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import view.GamePanel;

/**
 * Action class for changing key bindings.
 * 
 * @author Jesse Bostic
 * @version Autumn 2013
 */
@SuppressWarnings("serial")
public class SetControls extends AbstractAction implements KeyListener {
    
    /**
     * Array of control names.
     */
    private static final String[] CONTROLS = {"left", "right", "down", 
                                              "instadrop", "rotate", "pause"};
    
    /**
     * Height and width dimensions of icon.
     */
    private static final int ICON_DIMENSIONS = 40;
    
    /**
     * The layout for binding panel.
     */
    private static final GridLayout PANEL_LAYOUT = new GridLayout(CONTROLS.length, 2);
    
    /**
     * The frame in which current game is set.
     */
    private final JFrame myFrame;
    
    /**
     * The panel in which game is being played.
     */
    private final GamePanel myGamePanel;
    
    /**
     * Map of controls. <names, keycodes>
     */
    private final Map<String, Integer> myKeyMap;
    
    /**
     * Map of text fields. <names, fields>
     */
    private final Map<String, JTextField> myFieldMap;
    
    /**
     * Constructor for control setting action.
     * 
     * @param the_game_panel the panel in which game is located
     * @param the_frame the frame in which game is located
     */
    public SetControls(final GamePanel the_game_panel, final JFrame the_frame) {
        super();
        super.putValue(NAME, "Set Control Keys");
        super.putValue(MNEMONIC_KEY, (int) 's');
        myFrame = the_frame;
        myGamePanel = the_game_panel;
        myKeyMap = the_game_panel.getControlKeys();
        myFieldMap = new HashMap<String, JTextField>();
    }
    
    /**
     * Pauses game, if necessary, and pulls up key binding pane.
     * 
     * {@inheritDoc}
     * 
     * @param the_event the menu item selected (or action triggered)
     */
    @Override
    public void actionPerformed(final ActionEvent the_event) {
        if (!myGamePanel.isPaused()) {
            myGamePanel.pause();
        }
        Image image = new ImageIcon("images/tetris.png").getImage();  
        image = image.getScaledInstance(ICON_DIMENSIONS, ICON_DIMENSIONS, 
                                        java.awt.Image.SCALE_SMOOTH);
        JOptionPane.showMessageDialog(myFrame, getPanel(), "Set Controls", 
                                      JOptionPane.OK_OPTION, new ImageIcon(image));
        for (KeyListener l : myGamePanel.getKeyListeners()) {
            myGamePanel.removeKeyListener(l);
        }
        myGamePanel.pause();
    }
    
    /**
     * Creates panel to display key bindings to user.
     * 
     * @return key binding panel
     */
    private JPanel getPanel() {
        final JPanel panel = new JPanel(PANEL_LAYOUT);
        for (int i = 0; i < CONTROLS.length; i++) {
            final String str = CONTROLS[i];
            panel.add(new JLabel(str.toUpperCase()));
            final JTextField textField = 
                    new JTextField(KeyEvent.getKeyText(myKeyMap.get(str)));
            textField.setEditable(false);
            textField.setFocusable(true);
            myFieldMap.put(str, textField);
            panel.add(textField);
            textField.addKeyListener(this);
        }
        return panel;
    }
    
    /**
     * Associates a field with key press and binds keycode to map.
     * 
     * {@inheritDoc}
     * 
     * @param the_event the key pressed
     */
    @Override
    public void keyPressed(final KeyEvent the_event) {
        
        final int keyCode = the_event.getKeyCode();
        for (String str : myFieldMap.keySet()) {
            final JTextField field = myFieldMap.get(str);
            if (field.hasFocus()) {
                myKeyMap.put(str, keyCode);
                field.setText(KeyEvent.getKeyText(keyCode));
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void keyReleased(final KeyEvent the_event) {   
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void keyTyped(final KeyEvent the_event) {
    }

}
