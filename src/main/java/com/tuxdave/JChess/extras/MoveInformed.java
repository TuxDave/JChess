package com.tuxdave.JChess.extras;

public interface MoveInformed {
    /**
     * add the move to its stack
     * @param move the new move, with out the number because every obj has the own one
     */
    public void addMove(String move);
}
