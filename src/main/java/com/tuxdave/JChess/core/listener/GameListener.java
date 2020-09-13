package com.tuxdave.JChess.core.listener;

import com.tuxdave.JChess.core.pieces.King;
import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.extras.Vector2;

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
     * called after every finished move
     * todo: add parameters
     */
    public void logMove();

    /**
     * called when a piece moves
     * @param p the piece moved
     */
    public void onMove(Pezzo p);
    /**
     * called when play arrocco
     * @param p white/black - long/short (es: "white-long")
     */
    public void onMove(String type);
}
