package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Vector2;

public abstract class Pezzo {
    protected String type;
    protected int id;
    protected Vector2 position = null;
    protected boolean alreadyMoved = false;

    //returns a table containing the possible target of the move
    public abstract Vector2[] getPossibleMoves();
    //returns true if the destination indicated is a possible target, and then move itself
    public abstract boolean move(Vector2 _destination);

    public int getId() {
        return id;
    }
}
