package com.tuxdave.JChess.core;

public abstract class Pezzo {
    private String type;
    private int[] position = new int[2];
    private boolean alreadyMoved = false;

    /*
    * asks if apply the move or anly visualize it
    * returns a table containing the possible target of the move*/
    public abstract int[][] move(boolean active);
}
