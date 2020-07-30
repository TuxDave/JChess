package com.tuxdave.JChess.core;

public interface GameListener {
    /**
     * notify which is the current turn
     * @param turn the turn incoming
     */
    public void turnPassed(String turn);

    //public void onPedoneMove()
}
