package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Vector2;

public class Pedone extends Pezzo{

    public Pedone(int _id, Vector2 _inizialPos){
        id = _id;

    }

    @Override
    public Vector2[] getPossibleMoves() {
        return new Vector2[0];
    }

    @Override
    public boolean move(Vector2 _destination) {
        return false;
    }
}
