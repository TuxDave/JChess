package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.listener.GameListener;
import com.tuxdave.JChess.core.pieces.King;
import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.core.pieces.Tower;
import com.tuxdave.JChess.extras.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameBoard implements GameListener {
    public static final short limits = 8;
    private String turn = "WHITE";
    private final List<GameListener> listeners = new ArrayList<GameListener>();

    private Player[] players = new Player[2];
    {
        players[0] = new Player("p1", 1);//WHITE
        players[1] = new Player("p2", 2);//BLACK
        //add listener refert to kins
        ((King)players[0].getPieces()[12]).addGameListener(this);
        ((King)players[1].getPieces()[12]).addGameListener(this);
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
            if(p != null && p.getPosition().equals(pos)){
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
            if(p != null && p.getPosition().equals(pos)){
                ps = p;
            }
        }
        return ps;
    }

    /**
     *
     * @param _id id to search
     * @param _color color "BLACK" or white to search
     * @return null if the pieces isn't found
     */
    public Pezzo getPieceByIdAndColor(String _id, String _color){
        Pezzo[] pieces = getAllPieces();
        for(int i = 0; i < pieces.length; i++){
            if(pieces[i] != null &&pieces[i].getId().equals(_id) && pieces[i].getColor().equals(_color)){
                return getAllPieces()[i];
            }
        }
        return null;
    }

    /**
     * @param _cell the cell to tests
     * @return true if the cell is acceptable (between the board limits)
     */
    public static boolean isAnAcceptableCell(Vector2 _cell){
        if(_cell.isBetweenLimits(1,1,GameBoard.limits, GameBoard.limits)){
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
        players[player].getPieces()[l] = null;
    }

    /**
     * @param n player number 0 = white, 1 = black
     * @return the selected player obj
     */
    public Player getPlayer(int n){
        if(n >= 0 && n <= 1){
            return players[n];
        }else{
            throw new IndexOutOfBoundsException("player number not found!");
        }
    }

    public void nextTurn(){
        turn = turn.equals("WHITE") ? "BLACK" : "WHITE";
        for(GameListener l : listeners){
            if(l != null){
                l.turnPassed(turn);
            }
        }

    }

    //ListenerManagement

    public void addGameListener(GameListener g){
        listeners.add(g);
    }

    /**
     * notify which is the current turn
     * @param turn the turn incoming
     */
    @Override
    public void turnPassed(String turn){/*ignored*/}

    /**
     * the king calls this method when it want to make an "Arrocco"
     * @param k    the king is moving
     * @param type short or long
     */
    @Override
    public void arrocco(King k, String type) {
        try{
            if (type.toLowerCase().equals("short")) {
                Tower t = (Tower) getPieceByPosition(new Vector2(8, k.getPosition().y));
                t.move(new Vector2(6, t.getPosition().y));
                k.move(new Vector2(7, k.getPosition().y));
            } else {
                Tower t = (Tower) getPieceByPosition(new Vector2(1, k.getPosition().y));
                t.move(new Vector2(4, t.getPosition().y));
                k.move(new Vector2(3, k.getPosition().y));
            }
        }catch(NullPointerException e){
            System.err.println("Unable to find a Tower in the target cell!");
        }
    }
}