package com.tuxdave.JChess.core.chess.listener;

import com.tuxdave.JChess.core.chess.pieces.King;
import com.tuxdave.JChess.core.chess.pieces.Pezzo;

public interface GameListener {
    /**
     * notify which is the current turn
     * @param turn the turn incoming
     */
    public void turnPassed(String turn);

    /**
     * the king calls this method when it want to make an "Arrocco"
     * @param k the king is moving
     * @param type short or long
     */
    public void arrocco(King k, String type);

    /**
     * called when a piece moves
     * @param p the piece moved
     */
    public void onMove(Pezzo p);

    /**
     * called when play arrocco
     * @param type long/short lowerCase
     */
    public void onMove(String type);

    /**
     * called when a Pedone make morph
     * @param newType the new type of Pedone
     */
    public void onMorph(String newType);
}
