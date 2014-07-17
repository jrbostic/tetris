/*
 * Tetris - TCSS305 - Autumn 2013
 */

package view;

import actions.SetControls;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

/**
 * Class for setting up the Tetris game frame and menu functionality.
 * 
 * @author Jesse Bostic
 * @version Autumn 2013
 */
public class TetrisGame extends Observable {
    
    /**
     * Represents the desired block dimension for tetris board.
     */
    private static final int[] GAME_DIMENSIONS = {10, 20};
    
    /**
     * Represents the height and width of frame icon.
     */
    private static final int FRAME_ICON_DIMENSIONS = 40;
    
    /**
     * Represents minimum and starting dimension of the frame.
     */
    private static final Dimension FRAME_MIN_DIMENSION = new Dimension(415, 482);
    
    /**
     * Represents height and width of intro icon.
     */
    private static final int INTRO_ICON_DIMENSIONS = 30;
    
    /**
     * Represents dimension of text area at start of game.
     */
    private static final int[] INTRO_TEXTAREA_DIMENSION = {10, 18};
    
    /**
     * The frame in which the game is displayed.
     */
    private final JFrame myFrame;
    
    /**
     * The panel displaying game activity.
     */
    private final GamePanel myGamePanel;
    
    /**
     * The panel displaying stats for current game.
     */
    private final StatsPanel myStatsPanel;
    
    /**
     * No-arg constructor for a tetris game object.
     */
    public TetrisGame() {
        myFrame = new JFrame();
        myGamePanel = new GamePanel(GAME_DIMENSIONS[0], GAME_DIMENSIONS[1]);
        myStatsPanel = new StatsPanel();
        addObserver(myGamePanel);
        addObserver(myStatsPanel);
        myGamePanel.addGameObserver(myStatsPanel);
        
        setupFrame();
        orientUser();
        
        myGamePanel.start();
    }
    
    /**
     * Pops up a message to orient user as to game functionality.
     */
    private void orientUser() {
        orientUser("Welcome to TETRIS");
    }
    
    /**
     * Pops up a message to orient user as to game functionality.
     * 
     * @param the_title_string string to assign to title bar
     */
    private void orientUser(final String the_title_string) {
        Image image = new ImageIcon("images/tetris.png").getImage();  
        image = image.getScaledInstance(FRAME_ICON_DIMENSIONS, FRAME_ICON_DIMENSIONS, 
                                        java.awt.Image.SCALE_SMOOTH);
        JOptionPane.showMessageDialog(myFrame, getStartInfoPanel(), the_title_string, 
                                      JOptionPane.PLAIN_MESSAGE, new ImageIcon(image));  
    }

    /**
     * Method to set up frame and constituent components.
     */
    private void setupFrame() {
        myFrame.setLocationByPlatform(true);
        myFrame.setMinimumSize(FRAME_MIN_DIMENSION);
        myFrame.setTitle("TETRIS");
        
        //setup frame icon
        Image image = new ImageIcon("images/icon.png").getImage();  
        image = image.getScaledInstance(INTRO_ICON_DIMENSIONS, INTRO_ICON_DIMENSIONS, 
                                        java.awt.Image.SCALE_SMOOTH);
        myFrame.setIconImage(image);
        
        //set up resize listener
        myFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent the_event) {
                setChanged();
                notifyObservers(the_event);
            }
        });
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //setup main panel and add components
        final JPanel masterPanel = new JPanel(new GridLayout(1, 2));
        masterPanel.add(myGamePanel, new SpringLayout());
        masterPanel.add(myStatsPanel, new FlowLayout());
        
        //create menu, add items, add to frame
        final JMenuBar menuBar = new JMenuBar();
        setupFileMenu(menuBar);
        setupOptionMenu(menuBar);
        setupHelpMenu(menuBar);
        myFrame.setJMenuBar(menuBar);
        
        myFrame.add(masterPanel);
        myFrame.pack();
        myFrame.setVisible(true);
    }
    
    /**
     * Sets up the option menu and adds it to menu bar.
     * 
     * @param the_menuBar the main menu bar to add functionality to
     */
    private void setupOptionMenu(final JMenuBar the_menuBar) {
        
        //main option menu
        final JMenu optionMenu = new JMenu("Options");
        optionMenu.setMnemonic('o');
        
        //pause option setup
        final JMenuItem pauseOption = new JMenuItem("Resume Play");
        pauseOption.setMnemonic('p');
        pauseOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myGamePanel.requestFocus();
                myGamePanel.pause();
            }
        });
        
        //control customization item setup
        final JMenuItem setKeyOption = new JMenuItem(new SetControls(myGamePanel, myFrame));
        
        //control reset item setup
        final JMenuItem resetKeyOption = new JMenuItem("Reset Controls");
        resetKeyOption.setMnemonic('r');
        resetKeyOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myGamePanel.resetControls();
            }
            
        });
        
        //enable music checkbox setup
        final JCheckBox musicToggle = new JCheckBox("Music Enabled");
        musicToggle.setMnemonic('m');
        musicToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myGamePanel.enableSound(musicToggle.isSelected());
            } 
        });
        musicToggle.setSelected(true);
        
        //toggle grid setup
        final JCheckBox gridToggle = new JCheckBox("Grid Enabled");
        gridToggle.setMnemonic('g');
        gridToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myGamePanel.enableGrid(gridToggle.isSelected());
            } 
        });
        
      //toggle random color setup
        final JCheckBox colorToggle = new JCheckBox("Holiday Mode");
        colorToggle.setMnemonic('h');
        colorToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myGamePanel.enableHoliday(colorToggle.isSelected());
                myGamePanel.repaint();
            } 
        });
        
        //add items to option menu
        optionMenu.add(pauseOption);
        optionMenu.addSeparator();
        optionMenu.add(setKeyOption);
        optionMenu.add(resetKeyOption);
        optionMenu.addSeparator();
        optionMenu.add(musicToggle);
        optionMenu.add(gridToggle);
        optionMenu.add(colorToggle);
        
        the_menuBar.add(optionMenu);
    }
    
    /**
     * Sets up the file menu and adds it to menu bar.
     * 
     * @param the_menuBar the main menu bar to add functionality to
     */
    private void setupFileMenu(final JMenuBar the_menuBar) {
        
        //main file menu
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');
        
        //nem game option setup
        final JMenuItem newGameOption = new JMenuItem("New Game");
        newGameOption.setMnemonic('n');
        newGameOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myGamePanel.newGame();
                myStatsPanel.newGame();
            }
        });
        
        //exit option setup
        final JMenuItem exitOption = new JMenuItem("Exit");
        exitOption.setMnemonic('e');
        exitOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                myFrame.dispose();
            }
            
        });
        
        //add items to file menu
        fileMenu.add(newGameOption);
        fileMenu.addSeparator();
        fileMenu.add(exitOption);
        
        the_menuBar.add(fileMenu);
    }
    
    /**
    * Sets up the help menu and adds it to menu bar.
    * 
    * @param the_menuBar the main menu bar to add functionality to
    */
    private void setupHelpMenu(final JMenuBar the_menuBar) {
        final JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('h');
        
        final JMenuItem infoOption = new JMenuItem("Rules");
        infoOption.setMnemonic('r');
        infoOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                orientUser("Game Info");   
                myGamePanel.requestFocus();
                myGamePanel.pause();
            }  
        });
        
        final JMenuItem aboutOption = new JMenuItem("About...");
        aboutOption.setMnemonic('a');
        aboutOption.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent the_event) {
                JOptionPane.showMessageDialog(myFrame, "Tetris Functionality by Alan Fowler"
                        + "\nGUI and Extras by Jesse Bostic", "TCSS 305 - Autumn 2013", 
                        JOptionPane.INFORMATION_MESSAGE);
                
            }
        });
        
        helpMenu.add(infoOption);
        helpMenu.addSeparator();
        helpMenu.add(aboutOption);
        
        the_menuBar.add(helpMenu);
    }
    
    /**
     * Creates and returns an introductory panel for informing user.
     * 
     * @return a scroll pane for display in another window
     */
    private JScrollPane getStartInfoPanel() {
        final JTextArea textArea = new JTextArea(INTRO_TEXTAREA_DIMENSION[0], 
                                          INTRO_TEXTAREA_DIMENSION[1]);
        final JScrollPane scrollPane = new JScrollPane(textArea);
        
        final StringBuilder builder = new StringBuilder();
        builder.append("Welcome to Tetris!\n\n");
        builder.append("INTRODUCTION:\n");
        builder.append("This version of Tetris advances through levels at ");
        builder.append("set time intervals. When level indicator bar is full, ");
        builder.append("the next level is reached. ");
        builder.append("Music progression, speed, and the level indicator below play screen ");
        builder.append("inform player as to their current level. However, because of the ");
        builder.append("steady progression of levels (i.e. increasing speed), the faster ");
        builder.append("one plays, the higher the attainable score. Careful to avoid an ");
        builder.append("overflow! When pieces reach beyond the dotted overflow line at the ");
        builder.append("top of game board, the current game is over!  ");
        builder.append("Score over 10,000 for a ");
        builder.append("pat on the back.\n\n");
        builder.append("FEATURES:\n");
        builder.append("*8 Intense Levels*\n*Optional Sound and Music*\n*Assistive ");
        builder.append("Play Grid*\n*Holiday Mode*\n");
        builder.append("*Auto-Pause*\n*Customizable Keys*\n*Resizable Game Window*\n\n");
        builder.append("DEFAULT CONTROLS:\n");
        builder.append("MOVE LEFT - Left Arrow Key\n");
        builder.append("MOVE RIGHT - Right Arrow Key\n");
        builder.append("MOVE DOWN - Down Arrow Key\n");
        builder.append("HARD DROP - Up Arrow Key\n");
        builder.append("ROTATE PIECE - Space Bar\n");
        builder.append("PAUSE GAME - 'P' Key\n\n");
        builder.append("GAME SCORING:\n");
        builder.append("Piece Placed       =  10 pts\n");
        builder.append("1 Line Cleared    =  50 pts\n");
        builder.append("2 Lines Cleared  =  200 pts\n");
        builder.append("3 Lines Cleared  =  450 pts\n");
        builder.append("4 Lines Cleared  =  800 pts\n\n");
        builder.append("Press OK to Play!!");
        
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        
        textArea.setText(builder.toString());
        textArea.setCaretPosition(0);
        
        return scrollPane;
        
    }
    
}
