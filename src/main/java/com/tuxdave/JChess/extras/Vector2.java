package com.tuxdave.JChess.extras;

public class Vector2 {
    public int x;
    public int y;

    public Vector2(int _x, int _y){
        x = _x;
        y = _y;
    }

    public int[] getCoords(){
        return new int[]{x, y};
    }
    public void setCoords(int[] c){
        x = c[0];
        y = c[1];
    }
}
