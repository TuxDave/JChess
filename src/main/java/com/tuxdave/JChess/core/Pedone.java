package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Vector2;

public class Pedone extends Pezzo {

    private boolean alreadyMoved = false;

    /**
     * @param _id         everything possible string to recognize the piece;
     * @param _color      Black(B) or White(W);
     * @param _inizialPos the initial potition where can be the piece
     */
    public Pedone(String _id, char _color, Vector2 _inizialPos) {
        super(_id, _color, _inizialPos);
    }

    @Override
    public Vector2[] getPossibleMoves() {
        Vector2[] v;
        if(alreadyMoved){
            v = new Vector2[1];
            v[0] = new Vector2(position.x, position.y + 1);
        }else{
            v = new Vector2[2];
            v[0] = new Vector2(position.x, position.y + 1);
            v[1] = new Vector2(position.x, position.y + 2);
        }
        return v;
    }

    @Override
    public boolean move(Vector2 _destination) {
        Vector2[] moves = getPossibleMoves();
        boolean ok = false;
        for (Vector2 move : moves) {
            if (move.equals(_destination)) {
                ok = true;
            }
        }
        if(ok){
            position = _destination;
            alreadyMoved = true;
        }
        return ok;
    }

    @Override
    void setType() {
        type = "pedone";
    }

    /*morph itself in another Piece
        possible parameters:
            horse | ensign | tower | queen
         */
    public Pezzo morph(String _newType) {
        switch (_newType){
            /*todo:uncomment this when all piece's classes will be complited, use this.position and this.id
            case "horse":
                return new Horse();
                break;
            case "ensign":
                return new Ensign();
                break;
            case "tower":
                return new Tower();
                break;
            case "queen":
            default:
                return new Queen();
             */
        }
        return new Pedone(id, color, new Vector2(0,0));
    }
}
