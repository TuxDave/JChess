package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.extras.Vector2;

public class GameBoard {
    public static final short limits = 8;

    private Player[] players = new Player[2];
    {
        players[0] = new Player("p1", 1);//WHITE
        players[1] = new Player("p2", 2);//BLACK
    }
    public Pezzo[] getAllPieces(){
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
    public boolean isThereAPiece(Vector2 pos){
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
     * @param pos is the position where search a piece
     * @return a piece if found, else returns null
     */
    public Pezzo getPieceByPosition(Vector2 pos){
        Pezzo ps = null;
        for(Pezzo p : getAllPieces()){
            if(p.getPosition().equals(pos)){
                ps = p;
            }
        }
        return ps;
    }

    /**
     * @param _cell the cell to tests
     * @return true if the cell is acceptable (between the board limits)
     */
    public static boolean isAnAcceptableCell(Vector2 _cell){
        if(_cell.isBetweenLimits(0,0,GameBoard.limits, GameBoard.limits)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * delete a piece from the game board if is there one
     * @param p the piece which will be deleted
     */
    public void eatPiece(Pezzo p){
        int player, l = 0;
        if(p == null){
            System.out.println("nessun pezzo mangiato");
            return;
        }else{
            player = (p.getColor() == "WHITE" ? 0 : 1);
        }
        for(Pezzo p1 : players[player].getPieces()){
            if(p.equals(p1)){
                break;
            }
            l++;
        }
        if(l != players[player].getPieces().length)
            players[player].getPieces()[l] = null;
    }
}