/*
 * TCSS 305 - Project Tetris
 */

package model;

import java.awt.Color;

/**
 * The different types of blocks that will be stored in a Board's grid.
 * 
 * @author Alan Fowler
 * @version Autumn 2013
 */
public enum Block {
    /** AN empty space in the grid. */
    EMPTY(" ", Color.BLACK),
    /** A Block from an IPiece. */
    I("I", Piece.I),
    /** A Block from a JPiece. */
    J("J", Piece.J),
    /** A Block from an LPiece. */
    L("L", Piece.L),
    /** A Block from an OPiece. */
    O("O", Piece.O),
    /** A Block from an SPiece. */
    S("S", Piece.S),
    /** A Block from a TPiece. */
    T("T", Piece.T),
    /** A Block from a ZPiece. */
    Z("Z", Piece.Z);

    /**
     * The character corresponding to a particular value of the enumeration.
     */
    private String myLetter;

    /**
     * The Color corresponding to a particular value of the enumeration.
     */
    private Color myColor;

    // Constructor

    /**
     * Constructs a new Block with the specified letter.
     * 
     * @param theLetter The letter.
     * @param theColor The Color.
     */
    private Block(final String theLetter, final Color theColor) {
        myLetter = theLetter;
        myColor = theColor;
    }

    /**
     * Returns the Color of this Block.
     * 
     * @return the Color of this Block.
     */
    public Color getColor() {
        return myColor;
    }

    /**
     * Returns a String representation of this Block.
     * 
     * @return a String representation of this Block.
     */
    @Override
    public String toString() {
        return myLetter;
    }
}
