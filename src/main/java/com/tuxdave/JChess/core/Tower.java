package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Vector2;

public class Tower extends Pezzo{
    public Tower(String _id, Vector2 _inizialPos) {
        super(_id, _inizialPos);
    }

    @Override
    public Vector2[] getPossibleMoves() {
        Vector2[] v = new Vector2[28];
        short lx = 1, ly = 0, inc = 1, count = 0;
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
        type = "tower";
    }
}
