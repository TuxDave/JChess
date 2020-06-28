package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Vector2;

public class Horse extends Pezzo{
    public Horse(String _id, Vector2 _inizialPos) {
        super(_id, _inizialPos);
    }

    @Override
    void setType() {
        type = "horse";
    }

    @Override
    public Vector2[] getPossibleMoves() {
        Vector2[] v = new Vector2[8];
        v[0] = new Vector2(position.x+1, position.y+2);
        v[1] = new Vector2(position.x-1, position.y+2);
        v[2] = new Vector2(position.x+1, position.y-2);
        v[3] = new Vector2(position.x-1, position.y-2);
        v[4] = new Vector2(position.x+2, position.y+1);
        v[5] = new Vector2(position.x+2, position.y-1);
        v[6] = new Vector2(position.x-2, position.y+1);
        v[7] = new Vector2(position.x-2, position.y-1);
        return v;
    }
}