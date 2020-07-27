package com.tuxdave.JChess.extras;

import com.tuxdave.JChess.core.pieces.Pedone;

public interface PieceListener {
    /**
     * do some action when a Pedone moves
     * @param p the Pedone interested
     */
    public void onPedoneMove(Pedone p);
}
