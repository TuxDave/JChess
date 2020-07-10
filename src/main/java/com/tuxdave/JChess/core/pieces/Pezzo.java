package com.tuxdave.JChess.core.pieces;

import com.tuxdave.JChess.core.BoardProperty;
import com.tuxdave.JChess.extras.Vector2;

public abstract class Pezzo{
    protected String type;
    protected String id;
    protected char color;
    protected Vector2 position;

    /**
     * @param _id everything possible string to recognize the piece;
     * @param _color Black(B) or White(W);
     * @param _inizialPos the initial potition where can be the piece
     * */
    public Pezzo(String _id, char _color,Vector2 _inizialPos){
        super();
        //check if is the initial position in range with the limits of the battleLand
        if((_inizialPos.x <= BoardProperty.limits && _inizialPos.y <= BoardProperty.limits) || (_inizialPos.x > 0 && _inizialPos.y > 0)){
            position = _inizialPos;
        }else{
            throw new IllegalArgumentException("Initial position out of Bounds");
        }
        _color = Character.toUpperCase(_color);
        if(_color != 'B' && _color != 'W'){
            throw new IllegalArgumentException("Possible color only Black(B) or White(W)...");
        }else{
            color = _color;
        }
        id = _id;
        setType();
    }

    /*@return a table containing the possible target of the move
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
    public String getType(){
        return type;
    }
    public String getColor(){
        if(color == 'B'){
            return "BLACK";
        }else{
            return "WHITE";
        }
    }
    //sets the type of the piece in base how to be written this method
    abstract void setType();
}
