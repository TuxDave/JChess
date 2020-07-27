package com.tuxdave.JChess.core.pieces;

import com.tuxdave.JChess.extras.PieceListener;
import com.tuxdave.JChess.extras.Vector2;

public class Pedone extends Pezzo {

    private boolean alreadyMoved = false;
    private boolean reverse = false;
    private PieceListener listener;

    /**
     * @param _id         everything possible string to recognize the piece;
     * @param _color      Black(B) or White(W);
     * @param _inizialPos the initial potition where can be the piece
     */
    public Pedone(String _id, char _color, Vector2 _inizialPos) {
        super(_id, _color, _inizialPos);
        if(color == 'B'){
            reverse = true;
        }
    }

    public void setPieceListener(PieceListener l){
        if(l != null){
            listener = l;
        }
    }

    @Override
    public Vector2[] getPossibleMoves() {
        Vector2[] v;
        short upOrDown = (short) (reverse ? -1 : 1);
        if(alreadyMoved){
            v = new Vector2[1];
            v[0] = new Vector2(position.x, position.y + 1*upOrDown);
        }else{
            v = new Vector2[2];
            v[0] = new Vector2(position.x, position.y + 1*upOrDown);
            v[1] = new Vector2(position.x, position.y + 2*upOrDown);
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
        listener.onPedoneMove(this);
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
            case "horse":
                return new Horse(id, color,position);
            case "ensign":
                return new Ensign(id, color,position);
            case "tower":
                return new Tower(id, color,position);
            case "queen":
            default:
                return new Queen(id, color,position);
        }
    }
}
