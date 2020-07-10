package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.extras.Vector2;

public class GameBoard {
    public static final short limits = 8;

    private Player[] players = new Player[2];
    {
        players[0] = new Player("p1", 1);
        players[1] = new Player("p2", 2);
    }
    private Pezzo[] getAllPieces(){
        Pezzo[] ps = new Pezzo[32];
        short c = 0;
        for(short r = 0; r < 2; r++){
            for(Pezzo p : players[r].getPieces()){
                ps[c++] = p;
            }
        }
        return ps;
    }

    public GameBoard(){}

    /**
    * @param _n [1/2] select which player renames
     * @param _newNick set a new nickname*/
    public void setPlayerNick(int _n, String _newNick){
        if(_n > 0 && _n < 3){
            players[_n-1].nick = _newNick;
        }else{
            throw new IllegalArgumentException("_n parameter must be between 1 and 2");
        }
    }
    private boolean isThereAPiece(Vector2 pos){
        boolean b = false;
        Pezzo[] pieces = getAllPieces();
        for(Pezzo p : pieces){
            if(p.getPosition().equals(pos)){
                b = true;
            }
        }
        return b;
    }

    /**
     * @param pos is the position where search the a piece
     * @return a piece if found, else returns null
     */
    private Pezzo getPieceByPosition(Vector2 pos){
        Pezzo ps = null;
        for(Pezzo p : getAllPieces()){
            if(p.getPosition().equals(pos)){
                ps = p;
            }
        }
        return ps;
    }
}