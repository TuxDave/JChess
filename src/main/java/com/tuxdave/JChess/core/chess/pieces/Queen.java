package com.tuxdave.JChess.core.chess.pieces;

import com.tuxdave.JChess.extras.Vector2;

public class Queen extends Ensign{
    /**
     * @param _id         everything possible string to recognize the piece;
     * @param _color      Black(B) or White(W);
     * @param _inizialPos the initial potition where can be the piece
     */
    public Queen(String _id, char _color, Vector2 _inizialPos) {
        super(_id, _color, _inizialPos);
    }

    @Override
    public Vector2[] getPossibleMoves() {
        Vector2[] v = super.getPossibleMoves();
        short lx = 1, ly = 0, inc = 1, count = (short)v.length;
        v = java.util.Arrays.copyOf(v, v.length * 2);
        for(short c = 0; c < 4; c++){
            for(int i = 0; i < 7; i++){
                v[count++] = new Vector2(position.x + (inc*lx), position.y + (inc++*ly));
            }
            if(c == 0){
                lx = -1;
            }else if(c == 1){
                lx = 0;
                ly = 1;
            }else{
                ly = -1;
            }
            inc = 1;
        }
        return v;
    }

    @Override
    void setType() {
        type = "queen";
    }
}
