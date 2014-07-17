/*
 * Tetris - TCSS305 - Autumn 2013
 */

package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.AbstractPiece;
import model.Board;
import model.IPiece;
import model.JPiece;
import model.LPiece;
import model.OPiece;
import model.Piece;
import model.SPiece;
import model.TPiece;
import model.ZPiece;

/**
 * Class representing the score/stats panel of tetris game.
 * 
 * @author Jesse Bostic
 * @version Autumn 2013
 */
@SuppressWarnings("serial")
public class StatsPanel extends JPanel implements Observer {
    
    /**
     * The number of different types of tetris pieces.
     */
    private static final int NUMBER_OF_PIECE_TYPES = 7;
    
    /**
     * An array holding count for each type of piece. (T,S,Z,O,J,L,I)
     */
    private static final int[] PIECES_COUNT = new int[NUMBER_OF_PIECE_TYPES];
    
    /**
     * Count array position of TPiece.
     */
    private static final int TPIECE = 0;
    
    /**
     * Count array position of SPiece.
     */
    private static final int SPIECE = 1;
    
    /**
     * Count array position of ZPiece.
     */
    private static final int ZPIECE = 2;
    
    /**
     * Count array position of OPiece.
     */
    private static final int OPIECE = 3;
    
    /**
     * Count array position of JPiece.
     */
    private static final int JPIECE = 4;
    
    /**
     * Count array position of LPiece.
     */
    private static final int LPIECE = 5;
    
    /**
     * Count array position of IPiece.
     */
    private static final int IPIECE = 6;
    
    /**
     * The initial scale to which panel should be drawn.
     */
    private static final int INITIAL_SCALE = 20;
    
    /**
     * The background image.
     */
    private static final Image BACKGROUND_IMAGE = 
            new ImageIcon("images/tetris.jpg").getImage();
    
    /**
     * Height and width dimension of game over window icon.
     */
    private static final int ICON_DIMENSIONS = 40; 
    
    /**
     * Score panel border color.
     */
    private static final Color BORDER_COLOR = new Color(0, 0, 0, 125);
    
    /**
     * Score panel inner color.
     */
    private static final Color PANEL_COLOR = new Color(0, 0, 0, 175);
    
    /**
     * Points scored per piece played.
     */
    private static final int SCORE_PER_PIECE = 10;
    
    /**
     * Points scored "per" line (multiplier).
     */
    private static final int SCORE_PER_LINE = 50;
    
    /**
     * The ratio of scale to width.
     */
    private static final int WIDTH_RATIO = 23;
    
    /**
     * The ratio of scale to height.
     */
    private static final int HEIGHT_RATIO = 25;
    
    /**
     * Double representing 12.5 percent.
     */
    private static final double PERCENT_12 = .125;
    
    /**
     * Double representing 20 percent.
     */
    private static final double PERCENT_20 = .2;
    
    /**
     * Double representing 25 percent.
     */
    private static final double PERCENT_25 = .25;
    
    /**
     * Double representing 33 percent.
     */
    private static final double PERCENT_33 = .33;
    
    /**
     * Double representing 40 percent.
     */
    private static final double PERCENT_40 = .4;
    
    /**
     * Double representing 57 percent.
     */
    private static final double PERCENT_57 = .57;
    
    /**
     * Double representing 60 percent.
     */
    private static final double PERCENT_60 = .6;
    
    /**
     * Double representing 70 percent.
     */
    private static final double PERCENT_70 = .7;
    
    /**
     * Double representing 75 percent.
     */
    private static final double PERCENT_75 = .75;
    
    /**
     * Double representing 80 percent.
     */
    private static final double PERCENT_80 = .8;
    
    /**
     * Double representing 87.5 percent.
     */
    private static final double PERCENT_87 = .875;
    
    /**
     * Represents three pixel shift.
     */
    private static final int THREE_PIXELS = 3;
    
    /**
     * Represents five pixel shift.
     */
    private static final int FIVE_PIXELS = 5;
    
    /**
     * Represents ten pixel shift.
     */
    private static final int TEN_PIXELS = 10;
    
    /**
     * Represents twenty pixel shift.
     */
    private static final int TWENTY_PIXELS = 20;
    
    /**
     * Eleven pixels.
     */
    private static final int ELEVEN_PIXELS = 11;
    
    /**
     * Twelve pixels.
     */
    private static final int TWELVE_PIXELS = 12;
    
    /**
     * Thirteen pixels.
     */
    private static final int THIRTEEN_PIXELS = 13;
    
    /**
     * The multiplication symbol used for displaying pice type count.
     */
    private static final String PIECE_MULT_SYMBOL = "x ";
    
    /**
     * The next piece.
     */
    private Piece myNextPiece;
    
    /**
     * Pixel x-coordinate shift adjustment for next piece display.
     */
    private int myXAdjust;
    
    /**
     * Pixel y-coordinate shift adjustment for next piece display.
     */
    private int myYAdjust;
    
    /**
     * The number of lines cleared in current game.
     */
    private int myLineScore;
    
    /**
     * The number of pieces placed in current game.
     */
    private int myPieceScore;
    
    /**
     * The actual points scored in current game.
     */
    private int myTotalScore;
    
    /**
     * The current scale to which panel items are drawn.
     */
    private int myScale;
    
    /**
     * Whether a new piece has been counted by type.
     */
    private boolean myHasBeenCounted;
    
    /**
     * Constructs new stat panel object.
     */
    public StatsPanel() {
        myNextPiece = null;
        myXAdjust = 0;
        myYAdjust = 0;
        
        myLineScore = 0;
        myPieceScore = 0;
        myTotalScore = 0;
        
        myScale = INITIAL_SCALE;
        
        myHasBeenCounted = false;
    }
    
    /**
     * Updates the stats panel on board or game frame's change of state.
     * 
     * {@inheritDoc}
     * 
     * @param the_obs the observable object firing notify event
     * @param the_obj the object passed by observable object
     */
    @Override
    public void update(final Observable the_obs, final Object the_obj) {
        if (the_obs instanceof Board) {
            final Board board = (Board) the_obs;
            final Piece nextPiece = board.getNextPiece();
            countPieceType(board.getCurrentPiece(), board.getHeight());
            calculatePieceScore(nextPiece);
            calculatePieceAlignment(nextPiece);
            calculateLinesCleared(the_obj);
            if (board.isGameOver()) {
                Image image = new ImageIcon("images/tetris.png").getImage();  
                image = image.getScaledInstance(ICON_DIMENSIONS, ICON_DIMENSIONS, 
                                                java.awt.Image.SCALE_SMOOTH);
                JOptionPane.showMessageDialog(this.getParent(), "YOUR FINAL SCORE WAS >>> "
                                                + myTotalScore, "Game Over", 
                                                JOptionPane.PLAIN_MESSAGE, 
                                                new ImageIcon(image));
                 
            }
        } else if (the_obs instanceof TetrisGame) {
            final Dimension currentSize = ((ComponentEvent) the_obj).getComponent().getSize();
            final double width = currentSize.getWidth();
            final double height = currentSize.getHeight();
            if (width < height) {
                myScale = (int) width / WIDTH_RATIO;
            } else {
                myScale = (int) height / HEIGHT_RATIO - 1;
            }
        }
        repaint();
    }
    
    /**
     * Increments counts of piece types in current game.
     * (T,S,Z,O,J,L,I)
     * 
     * @param the_piece the current piece
     * @param the_board_height the height of current board
     */
    private void countPieceType(final Piece the_piece, final int the_board_height) {
        if (the_piece != myNextPiece && the_piece.getY() == the_board_height - 1 
                && !myHasBeenCounted) {
            myHasBeenCounted = true;
            if (the_piece instanceof TPiece) {
                PIECES_COUNT[TPIECE]++;
            } else if (the_piece instanceof SPiece) {
                PIECES_COUNT[SPIECE]++;
            } else if (the_piece instanceof ZPiece) {
                PIECES_COUNT[ZPIECE]++;
            } else if (the_piece instanceof OPiece) {
                PIECES_COUNT[OPIECE]++;
            } else if (the_piece instanceof JPiece) {
                PIECES_COUNT[JPIECE]++;
            } else if (the_piece instanceof LPiece) {
                PIECES_COUNT[LPIECE]++;
            } else if (the_piece instanceof IPiece) {
                PIECES_COUNT[IPIECE]++;
            }
        }
    }
    
    /**
     * Calculates and updates the score as a piece is placed.
     * 
     * @param the_piece the current piece in play on board
     */
    private void calculatePieceScore(final Piece the_piece) {
        if (the_piece != myNextPiece && myNextPiece != null) {
            myHasBeenCounted = false;
            myPieceScore++;
            myTotalScore += SCORE_PER_PIECE;
        }   
    }
    
    /**
     * Calculates the preferred alignment adjustment for next piece display.
     * 
     * @param the_piece the current piece in play on board
     */
    private void calculatePieceAlignment(final Piece the_piece) {
        if (!the_piece.equals(myNextPiece)) {
            myNextPiece = the_piece;
            if (myNextPiece instanceof IPiece) {
                myXAdjust = -TEN_PIXELS;
                myYAdjust = 0;
            } else if (myNextPiece instanceof OPiece) {
                myYAdjust = -FIVE_PIXELS; 
                myXAdjust = 0;
            } else {
                myXAdjust = 0;
                myYAdjust = FIVE_PIXELS;
            }
        }
    }
    
    /**
     * Updates lines cleared and score.
     * 
     * @param the_lines_cleared the most recent number of cleared lines
     */
    private void calculateLinesCleared(final Object the_lines_cleared) { 
        if (the_lines_cleared != null && the_lines_cleared instanceof Integer) {
            final int linesCleared = (int) the_lines_cleared;
            myLineScore += linesCleared;
            myTotalScore += linesCleared * linesCleared * SCORE_PER_LINE;
        }
    }
    
    /**
     * Sets up panel for a new game.
     */
    public void newGame() {
        myNextPiece = null;
        myXAdjust = 0;
        myYAdjust = 0;
        
        myLineScore = 0;
        myPieceScore = 0;
        myTotalScore = 0;
        
        for (int i = 0; i < PIECES_COUNT.length; i++) {
            PIECES_COUNT[i] = 0;
        }
        myHasBeenCounted = false;
    }
    
    /**
     * Draws the games score panel and background.
     * 
     * {@inheritDoc}
     * 
     * @param the_graphic graphics object for drawing
     */
    @Override
    public void paintComponent(final Graphics the_graphic) {
        super.paintComponent(the_graphic);
        final Graphics2D g2d = (Graphics2D) the_graphic;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                             RenderingHints.VALUE_ANTIALIAS_ON);
        
        final int panelWidth = getWidth();
        final int panelHeight = getHeight();
        
        //draw tiled background
        final int imageWidth = BACKGROUND_IMAGE.getWidth(this);
        final int imageHeight = BACKGROUND_IMAGE.getHeight(this);
        for (int i = 0; i * imageWidth < panelWidth; i++) {
            for (int j = 0; j * imageHeight < panelHeight; j++) {
                g2d.drawImage(BACKGROUND_IMAGE, i * imageWidth, j * imageHeight, this);
            }
        }
        
        drawPanel(g2d, panelWidth, panelHeight);
        drawNextPiece(g2d, panelWidth, panelHeight);
        drawPieceCount(g2d, panelWidth, panelHeight);

    }
    
    /**
     * Draws basic background and string aspects of score panel.
     * 
     * @param the_graphic the graphics object to draw on
     * @param the_width the current width of panel
     * @param the_height the current height of panel
     */
    private void drawPanel(final Graphics2D the_graphic, final int the_width, 
                           final int the_height) {
        //draw outer border of panel
        the_graphic.setColor(BORDER_COLOR);
        the_graphic.fillRoundRect(myScale - FIVE_PIXELS, myScale - FIVE_PIXELS, 
                          the_width - 2 * myScale + TEN_PIXELS, 
                          the_height - 2 * myScale + TEN_PIXELS, TEN_PIXELS, TEN_PIXELS);
        
        //draw inner border of panel
        the_graphic.setColor(PANEL_COLOR);
        the_graphic.fillRoundRect(myScale, myScale, the_width - 2 * myScale, 
                          the_height - 2 * myScale, FIVE_PIXELS, FIVE_PIXELS);
        
        //draw panel text
        the_graphic.setColor(Color.WHITE);
        the_graphic.drawRect((int) (the_width * PERCENT_33) - TEN_PIXELS, 
                             THREE_PIXELS * myScale, myScale * FIVE_PIXELS, 
                             myScale * FIVE_PIXELS);
        the_graphic.setFont(new Font("stretch", Font.BOLD, (int) (PERCENT_75 * myScale)));
        the_graphic.drawString("NEXT PIECE", (int) (the_width * PERCENT_33) - 1, 
                       (myScale - 1) * THREE_PIXELS);
        the_graphic.drawString("Lines Cleared: " + myLineScore, 
                       (int) (the_width * PERCENT_33) - TWENTY_PIXELS, 
                       myScale * TWELVE_PIXELS);
        the_graphic.drawString("Pieces Placed: " + myPieceScore, 
                       (int) (the_width * PERCENT_33) - TWENTY_PIXELS, 
                       myScale * ELEVEN_PIXELS);
        the_graphic.drawString("Total Score: " + myTotalScore, 
                       (int) (the_width * PERCENT_33) - TWENTY_PIXELS, 
                       myScale * THIRTEEN_PIXELS);
    }
    
    /**
     * Draws next piece on score panel.
     * 
     * @param the_graphic the graphics object to draw on
     * @param the_width the current width of panel
     * @param the_height the current height of panel
     */
    private void drawNextPiece(final Graphics2D the_graphic, final int the_width, 
                               final int the_height) {
        if (myNextPiece != null) {
            final int[][] piece = ((AbstractPiece) myNextPiece).getRotation();
            final Color currentColor = ((AbstractPiece) myNextPiece).getBlock().getColor();
        
            for (int[] block : piece) {
                the_graphic.setColor(currentColor);
                the_graphic.fillRoundRect((int) (the_width * PERCENT_33) 
                                  + block[1] * myScale + myXAdjust, 
                                  THREE_PIXELS * myScale + block[0] 
                                  * myScale + myScale / 2 + myYAdjust, 
                                  myScale, myScale, (int) (myScale * PERCENT_25), 
                                  (int) (myScale * PERCENT_25));  
                the_graphic.setColor(Color.BLACK);
                the_graphic.drawRoundRect((int) (the_width * PERCENT_33) 
                                  + block[1] * myScale + myXAdjust, 
                                  THREE_PIXELS * myScale + block[0] 
                                  * myScale + myScale / 2 + myYAdjust, 
                                  myScale, myScale, (int) (myScale * PERCENT_25), 
                                  (int) (myScale * PERCENT_25));
            }
        }
    }
    
    /**
     * Draws piece types and their count on score panel.
     * 
     * @param the_graphic the graphics object to draw on
     * @param the_width the current width of panel
     * @param the_height the current height of panel
     */
    private void drawPieceCount(final Graphics2D the_graphic, final int the_width, 
                                final int the_height) {
        final Piece[] pieces = {new TPiece((int) (the_width * PERCENT_20), 
                                           (int) (the_height * PERCENT_60)), 
                                new SPiece((int) (the_width * PERCENT_57), 
                                           (int) (the_height * PERCENT_60)), 
                                new ZPiece((int) (the_width * PERCENT_20), 
                                           (int) (the_height * PERCENT_70)), 
                                new OPiece((int) (the_width * PERCENT_57), 
                                           (int) (the_height * PERCENT_70)), 
                                new JPiece((int) (the_width * PERCENT_20), 
                                           (int) (the_height * PERCENT_80)), 
                                new LPiece((int) (the_width * PERCENT_57),
                                           (int) (the_height * PERCENT_80)), 
                                new IPiece((int) (the_width * PERCENT_40), 
                                           (int) (the_height * PERCENT_87))};
        for (int i = 0; i < pieces.length; i++) {
            pieces[i].rotate();
            final int[][] piecePosition = ((AbstractPiece) pieces[i]).getRotation();
            final Color pieceColor = ((AbstractPiece) pieces[i]).getBlock().getColor();
            for (int[] block : piecePosition) {
                the_graphic.setColor(pieceColor);
                the_graphic.fillRoundRect(pieces[i].getX() + block[1] * (myScale / 2), 
                                  pieces[i].getY() + block[0] * (myScale / 2), 
                                  myScale / 2, myScale / 2, (int) (myScale * PERCENT_12), 
                                  (int) (myScale * PERCENT_12));  
                the_graphic.setColor(Color.BLACK);
                the_graphic.drawRoundRect(pieces[i].getX() + block[1] * (myScale / 2), 
                                  pieces[i].getY() + block[0] * (myScale / 2), 
                                  myScale / 2, myScale / 2, (int) (myScale * PERCENT_12), 
                                  (int) (myScale * PERCENT_12));
            }
            the_graphic.setColor(Color.WHITE);
            if (pieces[i] instanceof IPiece) {
                the_graphic.drawString(PIECE_MULT_SYMBOL + PIECES_COUNT[i], 
                               pieces[i].getX() + THREE_PIXELS * myScale, 
                               pieces[i].getY() + myScale + TEN_PIXELS);
            } else {
                the_graphic.drawString(PIECE_MULT_SYMBOL + PIECES_COUNT[i], 
                               pieces[i].getX() + 2 * myScale, 
                               pieces[i].getY() + myScale);
            }
        }
    }

}
