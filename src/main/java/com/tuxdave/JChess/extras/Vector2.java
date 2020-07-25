package com.tuxdave.JChess.extras;

public class Vector2{
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

    /**
     * WARNING: the numbers indicated as minimum and maximum are included and considered as possible
     * @param _min the minimum possible value of x and y
     * @param _max the maximum possible value of x and y
     * @return true if the vector is between the limits
     */
    public boolean isBetweenLimits(Vector2 _min, Vector2 _max){
        if(x >= _min.x && y >= _min.y && x <= _max.x && y <= _max.y){
            return true;
        }else{
            return false;
        }
    }

    /**
     * WARNING: the numbers indicated as minimum and maximum are included and considered as possible
     * @param x1 minimum x value
     * @param y1 minimum y value
     * @param x2 maximum x value
     * @param y2 maximum y value
     * @return true if the vector is between the limits
     */
    public boolean isBetweenLimits(int x1, int y1, int x2, int y2){
        return isBetweenLimits(new Vector2(x1,y1), new Vector2(x2,y2));
    }


    /**
     * @param v1 first vector
     * @param v2 second vector
     * @return the sum of two vectors (x1+x2 and y1+y2)
     */
    public static Vector2 add(Vector2 v1, Vector2 v2){
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }
}
