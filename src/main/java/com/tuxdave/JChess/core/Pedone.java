package com.tuxdave.JChess.core;

import com.tuxdave.JChess.extras.Morphable;
import com.tuxdave.JChess.extras.Vector2;

public class Pedone extends Pezzo implements Morphable {

    public Pedone(int _id, Vector2 _inizialPos){
        type = "pedone";
        id = _id;
        if(_inizialPos.x <= CampoDiBattaglia.limits && _inizialPos.y <= CampoDiBattaglia.limits && _inizialPos.x > 0 && _inizialPos.y > 0){
            position = _inizialPos;
        }
    }

    @Override
    public Vector2[] getPossibleMoves() {
        return new Vector2[0];
    }

    @Override
    public boolean move(Vector2 _destination) {
        return false;
    }

    /*morph itself in another Piece
    possible parameters:
        horse | ensign | tower | queen
     */
    @Override
    public Pezzo morph(String _newType) {
        switch (_newType){
            /*todo:uncomment this when all piece's classes complited
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
        return new Pedone(1001, new Vector2(0,0));
    }
}
