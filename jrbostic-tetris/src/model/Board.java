/*
 * TCSS 305 - Project Tetris
 */

package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

/**
 * A representation of a Tetris game board.
 * 
 * @author Alan Fowler
 * @version Autumn 2013
 */
public final class Board extends Observable {
    // constants

    /** The width of a standard Tetris board. */
    private static final int DEFAULT_WIDTH = 10;

    /** The height of a standard Tetris board. */
    private static final int DEFAULT_HEIGHT = 20;

    /** The representation of the side walls of the board in string output. */
    private static final String WALL = "|";

    /** The representation of the board corners in string output. */
    private static final String CORNER = "+";

    /** The representation of the board floor in string output. */
    private static final String FLOOR = "-";

    /** The representation of an empty grid position in string output. */
    private static final String EMPTY = " ";

    /** The representation of a frozen block in string output. */
    private static final String FROZEN = "X";

    /** The representation of the current piece's position in string output. */
    private static final String CURRENT_PIECE = "*";

    /** The random number generator used for choosing new pieces. */
    private static final Random RANDOM = new Random();

    /** The minimum size of a board. */
    private static final int MIN_SIZE = 5;

    /** The number of rotations to return a piece to initial state. */
    private static final int ROTATIONS = 4;

    /** The number of extra rows above the board to display in String output. */
    private static final int EXTRA_ROWS = 4;

    // instance fields

    /**
     * The width of this Tetris board.
     */
    private int myWidth;

    /**
     * The height of this Tetris board.
     */
    private int myHeight;

    /**
     * The current frozen blocks on the board.
     */
    private final List<Block[]> myBlocks;

    /**
     * The piece currently being moved on the board.
     */
    private Piece myCurrentPiece;

    /**
     * The next piece to be used in the board.
     */
    private Piece myNextPiece;

    /**
     * The predetermined list of pieces to be fed into the board.
     */
    private List<Piece> myPieces;

    /**
     * The boolean representing the game state of Tetris.
     */
    private boolean myGameOver;

    /**
     * Constructs a Board using the specified dimensions.
     * 
     * @param theWidth the height to assign
     * @param theHeight the width to assign
     * @param thePieces the sequence of pieces to use; empty list indicates a random game
     */
    public Board(final int theWidth, final int theHeight,
                 final List<Piece> thePieces) {
        super();
        myPieces = new ArrayList<>();
        myBlocks = new LinkedList<Block[]>();
        newGame(theWidth, theHeight, thePieces);
    }

    /**
     * Constructs a Board using default dimensions (10x20) and random pieces.
     */
    public Board() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT, new LinkedList<Piece>());
    }

    /**
     * Creates a new game on a Board of the specified width and height
     * and loads the given set of pieces.
     * 
     * @param theWidth The width of the board
     * @param theHeight The height of the board
     * @param thePieces The pieces to use
     */
    public void newGame(final int theWidth, final int theHeight,
                        final List<Piece> thePieces) {
        if (theWidth < MIN_SIZE || theHeight < MIN_SIZE) {
            throw new IllegalArgumentException();
        }
        myCurrentPiece = null;
        myWidth = theWidth;
        myHeight = theHeight;
        myBlocks.clear();
        if (thePieces == null) {
            myPieces.clear();
        } else {
            myPieces = thePieces;
        }
        myGameOver = false;
        setNextPiece();
        assignCurrentPiece();
        setChanged();
        notifyObservers();
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return myWidth;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return myHeight;
    }

    /**
     * Returns the current frozen blocks on the board.
     * 
     * @return the current frozen blocks on the board.
     */
    public List<Block[]> getFrozenBlocks() {
        return myBlocks;
    }

    /**
     * Returns the current Piece being moved.
     * 
     * @return the current Piece being moved.
     */
    public Piece getCurrentPiece() {
        return myCurrentPiece;
    }

    /**
     * Attempts to move the current piece to the left.
     * 
     * @return true if it is possible to move the current piece to the left;
     *         false otherwise
     */
    public boolean moveLeft() {
        final int[][] blocks = ((AbstractPiece) myCurrentPiece).getBoardCoordinates();
        boolean canPass = true;

        // perform bounds checking on each block
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i][0] == 0 || // block is already at the left wall
                blockAt(blocks[i][0] - 1, blocks[i][1]) != Block.EMPTY) {
                // block to left is occupied
                canPass = false;
                break; // can't move, no need to keep checking
            }
        }

        if (canPass) {
            myCurrentPiece.moveLeft();
            setChanged();
            notifyObservers();
        }
        return canPass;
    }

    /**
     * Attempts to move the current piece to the right.
     * 
     * @return true if it is possible to move the current piece to the right;
     *         false otherwise
     */
    public boolean moveRight() {
        final int[][] blocks = ((AbstractPiece) myCurrentPiece).getBoardCoordinates();
        boolean canPass = true;

        // perform bounds checking on each block
        for (int i = 0; i < blocks.length; i++) {
            if (blocks[i][0] == myWidth - 1 || // block is already at the right
                                                // wall
                blockAt(blocks[i][0] + 1, blocks[i][1]) != Block.EMPTY) {
                // block to right is occupied
                canPass = false;
                break; // can't move, no need to keep checking
            }
        }

        if (canPass) {
            myCurrentPiece.moveRight();
            setChanged();
            notifyObservers();
        }
        return canPass;
    }

    /**
     * Attempts to move the current piece down.
     * 
     * @return true if it is possible to move the current piece down;
     *         false otherwise
     */
    public boolean moveDown() {
        final int[][] blocks = ((AbstractPiece) myCurrentPiece).getBoardCoordinates();
        boolean can_pass = true;

        // perform bounds checking on each block
        for (int i = 0; i < blocks.length; i++) {
            // Is this block at the bottom, or is there a piece below it?
            if (blocks[i][1] == 0 || // block is at the bottom
                blockAt(blocks[i][0], blocks[i][1] - 1) != Block.EMPTY) {
                // block below is occupied
                can_pass = false;
                break; // can't move, no need to keep checking
            }
        }

        if (can_pass) {
            myCurrentPiece.moveDown();
            setChanged();
            notifyObservers();
        } else {
            freeze();
        }
        return can_pass;
    }

    /**
     * Drops the current Piece all the way down.
     */
    public void hardDrop() {
        boolean can_pass = true;
        while (can_pass) {
            can_pass = moveDown();
        }
    }

    /**
     * Attempts to rotate the current piece clockwise.
     * 
     * @return true if it is possible to rotate the current piece;
     *         false otherwise
     */
    public boolean rotate() {
        myCurrentPiece.rotate();
        final int[][] blocks = ((AbstractPiece) myCurrentPiece).getBoardCoordinates();
        boolean can_pass = true;

        for (int[] dimension : blocks) {
            if (dimension[0] >= myWidth
                    || blockAt(dimension[0], dimension[1]) != Block.EMPTY) {

                for (int i = 1; i < ROTATIONS; i++) {
                    myCurrentPiece.rotate();
                }
                can_pass = false;
                break;
            }
        }
        if (can_pass) {
            setChanged();
            notifyObservers();
        }
        return can_pass;
    }

    /**
     * Updates the game by one step.
     */
    public void step() {
        moveDown();
    }

    /**
     * Returns the next piece to be used.
     * 
     * @return the next piece to be used.
     */
    public Piece getNextPiece() {
        return myNextPiece;
    }

    /**
     * Returns whether the game is over or not.
     * 
     * @return Whether the game is over or not.
     */
    public boolean isGameOver() {
        return myGameOver;
    }

    /**
     * Initializes the current piece.
     */
    private void assignCurrentPiece() {
        myCurrentPiece = myNextPiece;
        setNextPiece();
    }

    /**
     * Sets the next piece.
     */
    private void setNextPiece() {
        if (myPieces == null || myPieces.isEmpty()) {
            myNextPiece = randomPiece(myWidth / 2 - 2, myHeight);
        } else {
            myNextPiece = myPieces.remove(0);
        }
    }

    /**
     * Creates a new piece randomly chosen from the possible pieces at the
     * specified coordinates.
     * 
     * @param theX The x-coordinate
     * @param theY The y-coordinate
     * 
     * @return A randomly chosen piece
     */
    private Piece randomPiece(final int theX, final int theY) {
        final Block[] blocks = Block.values();
        Piece result;

        switch (blocks[RANDOM.nextInt(blocks.length)]) {
            case I:
                result = new IPiece(theX, theY);
                break;

            case J:
                result = new JPiece(theX, theY);
                break;

            case L:
                result = new LPiece(theX, theY);
                break;

            case O:
                result = new OPiece(theX, theY);
                break;

            case S:
                result = new SPiece(theX, theY);
                break;

            case T:
                result = new TPiece(theX, theY);
                break;

            case Z:
                result = new ZPiece(theX, theY);
                break;

            default: // If EMPTY, recursively try again
                result = randomPiece(theX, theY);
                break;
        }
        return result;
    }

    /**
     * Retrieves the block at the specified coordinates.
     * 
     * @param theX The x-coordinate
     * @param theY The y-coordinate
     * 
     * @return The block at the specified coordinates, EMPTY if there is no
     *         block at the specified coordinates, null if the specified
     *         coordinates are outside of the board
     */
    private Block blockAt(final int theX, final int theY) {
        Block result = null; // outside the board coordinates
        if (theX < myWidth && theX >= 0 && theY >= 0) {
            // inside the board
            result = Block.EMPTY; // blocks above the board are empty
            if (theY < myBlocks.size()) {
                result = myBlocks.get(theY)[theX];
            }
        }
        return result;
    }

    /**
     * Evaluates if the current piece occupies the provided position.
     * 
     * @param theX The x-coordinate to examine
     * @param theY The y-coordinate to examine
     * @return true if the current piece occupies the position, false otherwise
     */
    private boolean currentPieceAt(final int theX, final int theY) {
        boolean result = false;
        final int[][] blocks = ((AbstractPiece) myCurrentPiece).getBoardCoordinates();

        for (int block = 0; block < blocks.length; block++) {
            if (blocks[block][1] == theY && blocks[block][0] == theX) {
                result = true;
            }
        }
        return result;
    }

    /**
     * The current piece cannot move down so add its blocks to the board.
     */
    private void freeze() {
        final int[][] coordinates = ((AbstractPiece) myCurrentPiece).getBoardCoordinates();

        for (int block = 0; block < coordinates.length; block++) {
            final int x = coordinates[block][0];
            final int y = coordinates[block][1];

            // Add rows until this block can fit in one.
            while (y >= myBlocks.size()) {
                final Block[] new_row = new Block[myWidth];
                for (int i = 0; i < myWidth; i++) {
                    new_row[i] = Block.EMPTY;
                }
                myBlocks.add(new_row);
            }
            final Block[] row = myBlocks.get(y);
            row[x] = ((AbstractPiece) myCurrentPiece).getBlock();
        }
        clearLines();
        if (myBlocks.size() > myHeight) {
            myGameOver = true;
        } else {
            assignCurrentPiece();
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Checks if there are any lines that need to be cleared
     * and removes them from the board.
     */
    private void clearLines() {
        int linesClearedCount = 0;
        for (int i = myBlocks.size() - 1; i >= 0; i--) {
            boolean clear = true;
            final Block[] blocks = myBlocks.get(i);

            for (Block block : blocks) {
                if (block == Block.EMPTY) {
                    clear = false;
                    break;
                }
            }
            if (clear) {
                myBlocks.remove(i);
                linesClearedCount++;
            }
        }
        setChanged();
        notifyObservers(linesClearedCount);
    }

    /**
     * Returns a string that represents the current state of the board.
     * 
     * @return The string representation of the board
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();

        for (int i = myHeight + EXTRA_ROWS - 1; i > myHeight - 1; i--) {
            sb.append(EMPTY);
            sb.append(getRowString(i));
            sb.append('\n');
        }

        for (int i = myHeight - 1; i >= 0; i--) {
            sb.append(WALL);
            sb.append(getRowString(i));
            sb.append(WALL);
            sb.append('\n');
        }
        sb.append(CORNER);
        for (int i = 0; i < myWidth; i++) {
            sb.append(FLOOR);
        }
        sb.append(CORNER);
        return sb.toString();
    }

    /**
     * Returns a string representing the blocks in the given row.
     * 
     * @param theRow The row to represent
     * @return The string representation
     */
    private String getRowString(final int theRow) {
        final StringBuilder sb = new StringBuilder();

        if (myBlocks.size() - 1 < theRow) {
            for (int column = 0; column < myWidth; column++) {
                if (currentPieceAt(column, theRow)) {
                    sb.append(CURRENT_PIECE);
                } else {
                    sb.append(EMPTY);
                }
            }
        } else {
            final Block[] row_blocks = myBlocks.get(theRow);

            for (int column = 0; column < myWidth; column++) {
                if (currentPieceAt(column, theRow)) {
                    sb.append(CURRENT_PIECE);
                } else if (row_blocks[column] == Block.EMPTY) {
                    sb.append(EMPTY);
                } else {
                    sb.append(FROZEN);
                }
            }
        }
        return sb.toString();
    }
}
