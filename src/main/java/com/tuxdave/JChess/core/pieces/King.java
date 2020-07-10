package com.tuxdave.JChess.core.pieces;

import com.tuxdave.JChess.extras.Vector2;

public class King extends Pezzo{

    /**
     * @param _id         everything possible string to recognize the piece;
     * @param _color      Black(B) or White(W);
     * @param _inizialPos the initial potition where can be the piece
     */
    public King(String _id, char _color, Vector2 _inizialPos) {
        super(_id, _color, _inizialPos);
    }

    @Override
    public Vector2[] getPossibleMoves() {
        Vector2[] v = new Vector2[8];
        v[0] = new Vector2(position.x + 1, position.y + 1);
        v[1] = new Vector2(position.x + 1, position.y - 1);
        v[2] = new Vector2(position.x - 1, position.y + 1);
        v[3] = new Vector2(position.x - 1, position.y - 1);
        v[4] = new Vector2(position.x, position.y + 1);
        v[5] = new Vector2(position.x, position.y - 1);
        v[6] = new Vector2(position.x + 1, position.y);
        v[7] = new Vector2(position.x - 1, position.y);
        return v;
    }

    @Override
    void setType() {
        type = "king";
    }
}
