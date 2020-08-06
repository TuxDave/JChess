package com.tuxdave.JChess.core.listener;

import com.tuxdave.JChess.core.pieces.King;

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
}
