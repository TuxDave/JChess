package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Vector2;

public abstract class Pezzo {
    private String type;
    private int id;
    private Vector2 position = null;
    private boolean alreadyMoved = false;

    //returns a table containing the possible target of the move
    public abstract Vector2[] getPossibleMoves();
    //returns true if the destination indicated is a possible target, and then move itself
    public abstract boolean move(Vector2 _destination);

    public int getId() {
        return id;
    }
}
