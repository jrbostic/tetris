/*
 * Tetris - TCSS305 - Autumn 2013
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.AbstractPiece;
import model.Block;
import model.Board;
import model.Piece;
import tools.ColorGenerator;
import tools.MusicPlayer;

/**
 * Class representing the game panel display for active game board.
 * 
 * @author Jesse Bostic
 * @version Autumn 2013
 *
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Observer {
    
    /**
     * Holds the control key configuration by <name, keycode>.
     */
    private static final Map<String, Integer> CONTROL_KEYS = new HashMap<String, Integer>();
    
    /**
     * The initial timer delay controlling game speed.
     */
    private static final int START_DELAY = 1000;
    
    /**
     * The max levels supported for game.
     */
    private static final int HIGHEST_LEVEL = 8;
    
    /**
     * The quantifier used to meter timer delay decrement.
     */
    private static final int FIRE_COUNT_QUANTIFIER = 60;
    
    /**
     * The background image for panel.
     */
    private static final Image BACKGROUND_IMAGE = 
            new ImageIcon("images/tetris.jpg").getImage();
    
    /**
     * The margin between left and top margins of panel and board.
     */
    private static final int BOARD_COORD_ADJUST = 10;
    
    /**
     * The color of play grid.
     */
    private static final Color GRID_COLOR = new Color(255, 255, 255, 25);
    
    /**
     * The color of overflow line (game over marker).
     */
    private static final Color OVERFLOW_LINE_COLOR = new Color(255, 255, 255, 50);
    
    /**
     * The color around edges of board and level display.
     */
    private static final Color BOARD_EDGE_COLOR = new Color(0, 0, 0, 175);
    
    /**
     * The rounding of blocks.
     */
    private static final int BLOCK_ROUNDING = 5;
    
    /**
     * The rounding of frames for board and level display.
     */
    private static final int FRAME_ROUNDING = 5;
    
    /**
     * The ratio of scale to width.
     */
    private static final int WIDTH_RATIO = 24;
    
    /**
     * The ratio of scale to height.
     */
    private static final int HEIGHT_RATIO = 26;
    
    /**
     * Double representing 33 percent.
     */
    private static final double PERCENT_33 = .33;
    
    /**
     * Double representing 75 percent.
     */
    private static final double PERCENT_75 = .75;
    
    /**
     * Three pixels.
     */
    private static final int THREE_PIXELS = 3;
    
    /**
     * Four pixels.
     */
    private static final int FOUR_PIXELS = 4;
    
    /**
     * Five pixels.
     */
    private static final int FIVE_PIXELS = 5;
    
    /**
     * Nine pixels.
     */
    private static final int NINE_PIXELS = 9;
    
    /**
     * Ten pixels.
     */
    private static final int TEN_PIXELS = 3;
    
    /**
     * The game board.
     */
    private final Board myBoard;
    
    /**
     * The timer controlling game speed.
     */
    private final Timer myTimer;
    
    /**
     * The player controlling flow of music.
     */
    private final MusicPlayer myMusicPlayer;
    
    /**
     * The width of game board.
     */
    private final int myBoardWidth;
    
    /**
     * The height of game board.
     */
    private final int myBoardHeight;
    
    /**
     * The key listener for an active game.
     */
    private KeySmasher myPlayListener;
    
    /**
     * The key listener for a paused game.
     */
    private KeySmasher myPausedListener;
    
    /**
     * The scale to which panel contents should be drawn.
     */
    private int myScale;
    
    /**
     * A count of timer firings on current level.
     */
    private int myTimeElapsed;
    
    /**
     * The current level of game.
     */
    private int myLevel;
    
    /**
     * Holds whether game is paused.
     */
    private boolean myIsPaused;
    
    /**
     * Holds whether grid is enabled.
     */
    private boolean myGridEnabled;
    
    /**
     * Holds whether holiday mode enabled.
     */
    private boolean myHolidayEnabled;
    
    /**
     * Holds currently active piece.
     */
    private Piece myCurrentPiece;
    
    /**
     * Constructor for game board and panel.
     * 
     * @param the_width the block width of game board
     * @param the_height the block height of game board
     */
    public GamePanel(final int the_width, final int the_height) {
        myBoardWidth = the_width;
        myBoardHeight = the_height;
        myBoard = new Board(myBoardWidth, myBoardHeight, new LinkedList<Piece>());
        myBoard.addObserver(this);
        
        myTimer = new Timer(START_DELAY, new TickListener());
        myMusicPlayer = new MusicPlayer();
        setupGameState();
        
        myScale = myBoardHeight;
    }
    
    /**
     * Setup non-final basic game state.
     */
    private void setupGameState() {
        setFocusable(true);
        myPlayListener = new KeySmasher();
        myPausedListener = myPlayListener.getPauseListener();
        addKeyListener(myPlayListener);
        myGridEnabled = false;
        myHolidayEnabled = false;        
        myTimeElapsed = 0;
        myLevel = 1;
    }
    
    /**
     * Method to start a newly constructed game.
     */
    public void start() {
        myCurrentPiece = myBoard.getCurrentPiece();
        myTimer.start();
        myMusicPlayer.start();
    }
    
    /**
     * Method to clear old and start a new game.
     */
    public void newGame() {
        myTimeElapsed = 0;
        myLevel = 1;
        myBoard.newGame(myBoardWidth, myBoardHeight, new LinkedList<Piece>());
        myMusicPlayer.reset();
        myIsPaused = false;
        myTimer.setDelay(START_DELAY);
        removeKeyListener(myPausedListener);
        removeKeyListener(myPlayListener);
        addKeyListener(myPlayListener);
        start();
    }
    
    /**
     * Method to reset key controls to defaults.
     */
    public void resetControls() {
        myPlayListener = new KeySmasher();
        myPausedListener = myPlayListener.getPauseListener();
    }
    
    /**
     * Method to pause game.
     */
    public void pause() {
        if (myTimer.isRunning() && !myIsPaused) {
            myTimer.stop();
            addKeyListener(myPausedListener);
            removeKeyListener(myPlayListener);
            myIsPaused = true;
            myMusicPlayer.pause();
            update((Observable) myBoard, null);
        } else if (!myBoard.isGameOver() && myIsPaused) {
            myTimer.start();
            addKeyListener(myPlayListener);
            removeKeyListener(myPausedListener);
            myMusicPlayer.resume();
            myIsPaused = false;
        }
    }
    
    /**
     * Method to enable/disable sound.
     * 
     * @param the_enabler whether sound should be enabled
     */
    public void enableSound(final boolean the_enabler) {
        myMusicPlayer.setEnabled(the_enabler);
        if (!myBoard.isGameOver() && !myIsPaused) {
            myMusicPlayer.resume();
        }
    }
    
    /**
     * Method to enable/disable grid.
     * 
     * @param the_enabler whether grid should be enabled
     */
    public void enableGrid(final boolean the_enabler) {
        myGridEnabled = the_enabler;
    }
    
    /**
     * Method to enable/disable holiday mode.
     * 
     * @param the_enabler whether holiday mode should be enabled
     */
    public void enableHoliday(final boolean the_enabler) {
        myHolidayEnabled = the_enabler;
    }
    
    /**
     * Query as to whether game is paused.
     * 
     * @return true if paused, false otherwise
     */
    public boolean isPaused() {
        return myIsPaused;
    }
    
    /**
     * Method to add game observer to board.
     * 
     * @param the_ob the observer to add to board
     */
    public void addGameObserver(final Observer the_ob) {
        myBoard.addObserver(the_ob);
    }
    
    /**
     * Query to obtain reference to control keys.
     * 
     * @return map from control key names to assigned keycodes
     */
    public Map<String, Integer> getControlKeys() {
        return myPlayListener.getKeyMap();
    }
    
    /**
     * Paints the game panel.
     * 
     * {@inheritDoc}
     * 
     * @param the_graphic the graphics object to be drawn on
     */
    @Override
    public void paintComponent(final Graphics the_graphic) {
        super.paintComponent(the_graphic);
        final Graphics2D g2d = (Graphics2D) the_graphic;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        //draw tiled background
        final int imageWidth = BACKGROUND_IMAGE.getWidth(this);
        final int imageHeight = BACKGROUND_IMAGE.getHeight(this);
        final int panelWidth = getWidth();
        final int panelHeight = getHeight();
        for (int i = 0; i * imageWidth < panelWidth; i++) {
            for (int j = 0; j * imageHeight < panelHeight; j++) {
                g2d.drawImage(BACKGROUND_IMAGE, i * imageWidth, j * imageHeight, this);
            }
        }
        
        //draw background
        if (myHolidayEnabled) {
            g2d.setColor(ColorGenerator.generateColor().brighter());
        } else {
            g2d.setColor(BOARD_EDGE_COLOR);
        }
        g2d.fillRoundRect(BOARD_COORD_ADJUST - FOUR_PIXELS, BOARD_COORD_ADJUST, 
                          myBoardWidth * myScale + NINE_PIXELS, 
                          (myBoardHeight + 1) * myScale + FIVE_PIXELS, 
                          FRAME_ROUNDING, FRAME_ROUNDING);
        g2d.setColor(Color.BLACK);
        g2d.fillRect(BOARD_COORD_ADJUST, BOARD_COORD_ADJUST, myBoardWidth * myScale + 1, 
                     (myBoardHeight + 1) * myScale + 1);
        
        if (!myIsPaused && myCurrentPiece != null) {
            
            drawGameInProgress(g2d);
            
        } else if (myIsPaused) { //if paused, draw on board
            g2d.setFont(new Font("Paused Font", Font.ITALIC, myScale));
            g2d.setColor(Color.WHITE);
            g2d.drawString("*PAUSED*", myScale * THREE_PIXELS, 
                           myScale * THREE_PIXELS * TEN_PIXELS);
        }
        
    }
    
    /**
     * Draws frozen blocks, current block, level display and grid.
     * 
     * @param the_graphic the graphics2D object to be drawn on
     */
    private void drawGameInProgress(final Graphics2D the_graphic) {
        //draw frozen blocks
        int row = 0;
        for (Block[] b : myBoard.getFrozenBlocks()) {
            int column = 0;
            for (Block block : b) {
                if (myHolidayEnabled && block != Block.EMPTY) {
                    the_graphic.setColor(ColorGenerator.generateColor());
                } else {
                    the_graphic.setColor(block.getColor().darker());
                }
                the_graphic.fillRoundRect(BOARD_COORD_ADJUST + column * myScale, 
                                  BOARD_COORD_ADJUST + (myBoardHeight - row) 
                                  * myScale, myScale, myScale, BLOCK_ROUNDING, 
                                  BLOCK_ROUNDING);
                the_graphic.setColor(Color.BLACK);
                the_graphic.drawRoundRect(BOARD_COORD_ADJUST + column * myScale, 
                                  BOARD_COORD_ADJUST + (myBoardHeight - row)
                                  * myScale, myScale, myScale, BLOCK_ROUNDING, 
                                  BLOCK_ROUNDING);
                column++;
            }
            row++;
        }  
        
        //draw current block
        final Piece p = myBoard.getCurrentPiece();
        final int[][] piece = ((AbstractPiece) p).getBoardCoordinates();
        final Color currentColor = ((AbstractPiece) p).getBlock().getColor();
        
        for (int[] block : piece) {
            if (myHolidayEnabled) {
                the_graphic.setColor(ColorGenerator.generateColor());
            } else {
                the_graphic.setColor(currentColor);
            }
            the_graphic.fillRoundRect(BOARD_COORD_ADJUST + block[0] * myScale, 
                                      BOARD_COORD_ADJUST + (myBoardHeight - block[1]) 
                                      * myScale, myScale, myScale, BLOCK_ROUNDING, 
                                      BLOCK_ROUNDING);  
            the_graphic.setColor(Color.BLACK);
            the_graphic.drawRoundRect(BOARD_COORD_ADJUST + block[0] * myScale, 
                                      BOARD_COORD_ADJUST + (myBoardHeight - block[1]) 
                                      * myScale, myScale, myScale, BLOCK_ROUNDING, 
                                      BLOCK_ROUNDING);
        }  
        
        //draw current level
        the_graphic.setColor(BOARD_EDGE_COLOR);
        the_graphic.fillRoundRect(BOARD_COORD_ADJUST * (int) (myScale * PERCENT_33) - 2, 
                          2 * (BOARD_COORD_ADJUST + 1) + (myBoardHeight + 1) 
                          * myScale, FOUR_PIXELS * (myScale + 1), 
                          myScale + 2, FRAME_ROUNDING, FRAME_ROUNDING);
        the_graphic.setPaint(new GradientPaint(BOARD_COORD_ADJUST 
                      * (int) (myScale * PERCENT_33) - 2, 2 * (BOARD_COORD_ADJUST + 1) 
                      + (myBoardHeight + 1) * myScale, Color.GREEN.darker(), 
                      BOARD_COORD_ADJUST * (int) (myScale * PERCENT_33) + FOUR_PIXELS 
                      * (myScale + 1) - 2, 2 * BOARD_COORD_ADJUST + myBoard.getHeight() 
                      * myScale + myScale + 2 + myScale + 2, Color.RED.brighter()));
        the_graphic.fillRoundRect(BOARD_COORD_ADJUST * (int) (myScale * PERCENT_33) - 2, 
                          2 * BOARD_COORD_ADJUST + myBoardHeight * myScale 
                          + myScale + 2, (int) ((FOUR_PIXELS * (myScale + 1)) 
                          * ((double) myTimeElapsed / (myLevel * FIRE_COUNT_QUANTIFIER))), 
                          myScale + 2, FRAME_ROUNDING, FRAME_ROUNDING);
        the_graphic.setColor(Color.WHITE);
        the_graphic.drawRoundRect(BOARD_COORD_ADJUST * (int) (myScale * PERCENT_33) - 2, 
                          2 * BOARD_COORD_ADJUST + myBoardHeight * myScale 
                          + myScale + 2, FOUR_PIXELS * (myScale + 1), myScale + 2, 
                          FRAME_ROUNDING, FRAME_ROUNDING);
        
        the_graphic.setFont(new Font("Level Font", Font.BOLD, myScale));
        the_graphic.setColor(Color.WHITE);
        the_graphic.drawString("LEVEL " + myLevel, BOARD_COORD_ADJUST 
                       * (int) (myScale * PERCENT_33), 2 * BOARD_COORD_ADJUST 
                       + myBoardHeight * myScale + 2 * myScale);
        
        //draw overflow
        the_graphic.setColor(OVERFLOW_LINE_COLOR);
        for (int i = 0; i < myBoardWidth; i++) {
            the_graphic.drawLine(BOARD_COORD_ADJUST + myScale * i + FIVE_PIXELS, 
                         BOARD_COORD_ADJUST + myScale, BOARD_COORD_ADJUST + myScale 
                         * (i + 1) - FIVE_PIXELS, BOARD_COORD_ADJUST + myScale);
        }
        
        //draw grid
        if (myGridEnabled) {
            the_graphic.setColor(GRID_COLOR);
            for (int i = 1; i < myBoardWidth; i++) {
                the_graphic.drawLine(BOARD_COORD_ADJUST + myScale * i, BOARD_COORD_ADJUST, 
                             BOARD_COORD_ADJUST + myScale * i, BOARD_COORD_ADJUST 
                             + myBoardHeight * myScale + myScale);
            }
            for (int i = 1; i <= myBoardHeight; i++) {
                the_graphic.drawLine(BOARD_COORD_ADJUST, BOARD_COORD_ADJUST + myScale * i, 
                             BOARD_COORD_ADJUST + myBoardWidth * myScale, 
                             BOARD_COORD_ADJUST + myScale * i);
            }
        }
    }
    
    /**
     * Updates the GUI on board or frame change of state.
     * 
     * {@inheritDoc}
     * 
     * @param the_observer the observable object firing notify event
     * @param the_object the object passed by observable object
     */
    @Override
    public void update(final Observable the_observer, final Object the_object) {
        if (the_observer instanceof TetrisGame) {
            final Dimension currentSize = 
                    ((ComponentEvent) the_object).getComponent().getSize();
            final double width = currentSize.getWidth();
            final double height = currentSize.getHeight();
            if (width < height) {
                myScale = (int) width / WIDTH_RATIO;
            } else {
                myScale = (int) height / HEIGHT_RATIO - 1;
            }
        }
        if (!this.isFocusOwner() && !myIsPaused && myTimeElapsed > 0 
                && !myBoard.isGameOver()) {
            pause();
        }
        
        repaint();
        
    }
    
    /**
     * Class to for listening to timer and performing appropriate action on each firing.
     * 
     * @author Jesse Bostic
     * @version Autumn 2013
     */
    private class TickListener implements ActionListener {
        
        /**
         * Determines what should be done at each firing of timer.
         * 
         * {@inheritDoc}
         * 
         * @param the_event the timer firing event
         */
        @Override
        public void actionPerformed(final ActionEvent the_event) {
            if (!myBoard.isGameOver()) { 
                myBoard.step();
                myTimeElapsed++;
                if (myTimeElapsed == (myLevel * FIRE_COUNT_QUANTIFIER)) { 
                    myTimeElapsed = 0;
                    myLevel++;
                    myTimer.setDelay((int) (myTimer.getDelay() * PERCENT_75));
                    if (myLevel <= HIGHEST_LEVEL) {
                        myMusicPlayer.levelUp();
                    }
                }
            } else {
                myTimer.stop();
                myMusicPlayer.gameOver();
                for (KeyListener l : getKeyListeners()) {
                    removeKeyListener(l);
                }
            }         
        }
        
    }
    
    /**
     * Class for handling key control interactions with game board.
     * 
     * @author Jesse Bostic
     * @version Autumn 2013
     */
    private class KeySmasher extends KeyAdapter {
        
        /**
         * String representing move left action.
         */
        private static final String LEFT = "left";
        
        /**
         * String representing move right action.
         */
        private static final String RIGHT = "right";
        
        /**
         * String representing move down action.
         */
        private static final String DOWN = "down";
        
        /**
         * String representing hard drop action.
         */
        private static final String DROP = "instadrop";
        
        /**
         * String representing rotate action.
         */
        private static final String ROTATE = "rotate";
        
        /**
         * String representing pause action.
         */
        private static final String PAUSE = "pause";
        
        /**
         * Array for holding action names.
         */
        private final String[] myControls = {LEFT, RIGHT, DOWN, DROP, ROTATE, PAUSE};
        
        /**
         * Array for holding keycodes assigned to actions.
         */
        private final int[] myControlKeys = {KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, 
                                             KeyEvent.VK_DOWN, KeyEvent.VK_UP, 
                                             KeyEvent.VK_SPACE, KeyEvent.VK_P};
        
        /**
         * Constructor that sets up key control map.
         */
        public KeySmasher() {
            for (int i = 0; i < myControls.length; i++) {
                CONTROL_KEYS.put(myControls[i], myControlKeys[i]);
            }
        }
        
        /**
         * Query to obtain controls during game paused event.
         * 
         * @return key listener for paused game
         */
        protected KeySmasherPaused getPauseListener() {
            return new KeySmasherPaused();
        }
        
        /**
         * Query to obtain the map holding map for controls.
         * 
         * @return key control map <name, keycode>
         */
        protected Map<String, Integer> getKeyMap() {
            return CONTROL_KEYS;
        }
        
        /**
         * Determines what game should do given a key press.
         * 
         * {@inheritDoc}
         * 
         * @param the_event the keypress event to be analyzed
         */
        @Override
        public void keyPressed(final KeyEvent the_event) {
            if (the_event.getKeyCode() == CONTROL_KEYS.get(LEFT)) {
                myBoard.moveLeft();
            } else if (the_event.getKeyCode() == CONTROL_KEYS.get(RIGHT)) {
                myBoard.moveRight();
            } else if (the_event.getKeyCode() == CONTROL_KEYS.get(DOWN)) {
                myBoard.moveDown();
            } else if (the_event.getKeyCode() == CONTROL_KEYS.get(DROP)) {
                myBoard.hardDrop();
            } else if (the_event.getKeyCode() == CONTROL_KEYS.get(ROTATE)) {
                myBoard.rotate();
            } else if (the_event.getKeyCode() == CONTROL_KEYS.get(PAUSE)) {  
                pause();
            } 
        }
        
        /**
         * Class for handling key controls while game is paused.
         * 
         * @author Jesse Bostic
         * @version Autumn 2013
         */
        private class KeySmasherPaused extends KeySmasher {
            
            /**
             * Responds only to corresponding unpause key event.
             * 
             * {@inheritDoc}
             * 
             * @param the_event the key event to be analyzed
             */
            @Override
            public void keyPressed(final KeyEvent the_event) {
                if (the_event.getKeyCode() == CONTROL_KEYS.get(PAUSE)) {  
                    pause();
                } 
                
            }
        }
    }
}
