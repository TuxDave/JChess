package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Vector2;

//this is not a real listener, but only an interface
public interface AttackSystem {

    /**
     * check if the oposite king is under attack by me
     * @return true if am i checking king
     */
    public boolean amICheckingKing(GameBoard g);

    /**
     * check if is actually a possible target of a move
     * @param _pos the destination
     * @return true if i can go there
     */
    public boolean canIGoHere(Vector2 _pos, GameBoard b);
}
