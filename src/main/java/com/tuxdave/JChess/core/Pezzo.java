package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Vector2;

public abstract class Pezzo {
    protected String type;
    protected String id;
    protected Vector2 position = null;

    public Pezzo(String _id, Vector2 _inizialPos){
        //check if is the initial position in range with the limits of the battleLand
        if((_inizialPos.x <= CampoDiBattaglia.limits && _inizialPos.y <= CampoDiBattaglia.limits) || (_inizialPos.x > 0 && _inizialPos.y > 0)){
            position = _inizialPos;
        }else{
            throw new IllegalArgumentException("Initial position out of Bounds");
        }
        id = _id;
        setType();
    }

    /*returns a table containing the possible target of the move
    can generate also position that are out of game board, but in this case, the boardClass will exclude that possibility
     */
    public abstract Vector2[] getPossibleMoves();
    //returns true if the destination indicated is a possible target, and then move itself
    public boolean move(Vector2 _destination){
        Vector2[] moves = getPossibleMoves();
        boolean ok = false;
        for (Vector2 move : moves) {
            if (move.equals(_destination)) {
                ok = true;
            }
        }
        if(ok){
            position = _destination;
        }
        return ok;
    }

    public String getId() {
        return id;
    }
    public Vector2 getPosition(){
        return position;
    }
    //sets the type of the piece in base how to be written this method
    abstract void setType();
}
