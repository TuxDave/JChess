package com.tuxdave.JChess.core.pieces;

import com.tuxdave.JChess.core.GameBoard;
import com.tuxdave.JChess.core.GameLogger;
import com.tuxdave.JChess.core.listener.GameListener;
import com.tuxdave.JChess.extras.Vector2;

public class Pedone extends Pezzo implements GameListener {
    private boolean alreadyMoved = false;
    public boolean justDoubleCase = false;
    private boolean reverse = false;

    public GameListener gameListener;

    /**
     * @param _id         everything possible string to recognize the piece;
     * @param _color      Black(B) or White(W);
     * @param _inizialPos the initial potition where can be the piece
     */
    public Pedone(String _id, char _color, Vector2 _inizialPos) {
        super(_id, _color, _inizialPos);
        if(color == 'B'){
            reverse = true;
        }
    }

    /**
     * says if the pawn can go on a diagonal position using the eat feature
     * @param destination the destination
     * @param b game board to catch the dates
     * @return true if it can.
     */
    public boolean canIGoHereByEat(Vector2 destination, GameBoard b){
        Pezzo p = b.getPieceByPosition(destination);
        if(p != null && !p.getColor().equals(getColor())){
            return true;
        }
        return false;
    }

    @Override
    public Vector2[] getPossibleMoves() {
        Vector2[] v;
        short upOrDown = (short) (reverse ? -1 : 1);
        if(alreadyMoved){
            v = new Vector2[1];
            v[0] = new Vector2(position.x, position.y + 1*upOrDown);
        }else{
            v = new Vector2[2];
            v[0] = new Vector2(position.x, position.y + 1*upOrDown);
            v[1] = new Vector2(position.x, position.y + 2*upOrDown);
        }
        return v;
    }

    /**
     * the pawn has more movement liberty, to make easier its strange eating policies
     * @param _destination
     */
    @Override
    public void move(Vector2 _destination) {
        Vector2[] moves = getPossibleMoves();
        boolean ok = true;
        if(position.y+2 == _destination.y || position.y-2 == _destination.y){
            justDoubleCase = true;
        }
        boolean enPassant = (position.x+1 == _destination.x && position.y == _destination.y) || (position.x -1 == _destination.x && position.y == _destination.y);
        position = _destination;
        alreadyMoved = true;
        if(enPassant){//after en passant adjustament position
            move(new Vector2(_destination.x, _destination.y+(getColor().equals("WHITE") ? 1 : -1)));
        }
    }

    @Override
    void setType() {
        type = "pedone";
    }

    public void setAlreadyMoved(boolean _alreadyMoved) {
        alreadyMoved = _alreadyMoved;
    }
    public boolean getAlreadyMoved(){
        return alreadyMoved;
    }

    /**morph itself in another Piece
        possible parameters:
            horse | ensign | tower | queen
         */
    public Pezzo morph(String _newType) {
        gameListener.onMorph(_newType);
        switch (_newType){
            case "horse":
                return new Horse(new String(_newType + Integer.toString(Integer.parseInt(id.substring(id.length()-1)) + 2)), color,position);
            case "ensign":
                return new Ensign(new String(_newType + Integer.toString(Integer.parseInt(id.substring(id.length()-1)) + 2)), color,position);
            case "tower":
                return new Tower(new String(_newType + Integer.toString(Integer.parseInt(id.substring(id.length()-1)) + 2)), color,position);
            case "queen":
            default:
                return new Queen(new String(_newType + Integer.toString(Integer.parseInt(id.substring(id.length()-1)) + 2)), color,position);
        }
    }

    /**
     * notify which is the current turn
     * @param turn the turn incoming
     */
    @Override
    public void turnPassed(String turn) {
        if(justDoubleCase && turn.equals(getColor())){
            justDoubleCase = false;
        }
    }
    @Override
    public void arrocco(King k, String type) {/*ignored*/}

    @Override
    public void onMove(Pezzo p){/*ignored*/}

    @Override
    public void onMove(String type){/*ignored*/}

    @Override
    public void onMorph(String newType) {/*ignored*/}
}
