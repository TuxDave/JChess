package com.tuxdave.JChess.extras;

public class Vector2 {
    public int x;
    public int y;

    public Vector2(int _x, int _y){
        x = _x;
        y = _y;
    }
    public Vector2(){
        this(0,0);
    }

    public int[] getCoords(){
        return new int[]{x, y};
    }
    public void setCoords(int[] c){
        x = c[0];
        y = c[1];
    }

    public String toString(){
        return "" + x + "," + y;
    }

    public boolean equals(Vector2 other){
        if(x == other.x && y == other.y){
            return true;
        }else{
            return false;
        }
    }
}
