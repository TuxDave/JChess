package com.tuxdave.JChess.core.pieces;

import com.tuxdave.JChess.core.AttackSystem;
import com.tuxdave.JChess.core.GameBoard;
import com.tuxdave.JChess.core.RouteChecker;
import com.tuxdave.JChess.extras.Drawable;
import com.tuxdave.JChess.extras.Vector2;

import java.awt.*;

public abstract class Pezzo implements Drawable, AttackSystem {
    protected String type;
    protected String id;
    protected char color;
    protected Vector2 position;

    /**
     * @param _id everything possible string to recognize the piece;
     * @param _color Black(B) or White(W);
     * @param _inizialPos the initial potition where can be the piece
     * */
    public Pezzo(String _id, char _color,Vector2 _inizialPos){
        super();
        //check if is the initial position in range with the limits of the battleLand
        if((_inizialPos.x <= GameBoard.limits && _inizialPos.y <= GameBoard.limits) || (_inizialPos.x > 0 && _inizialPos.y > 0)){
            position = _inizialPos;
        }else{
            throw new IllegalArgumentException("Initial position out of Bounds");
        }
        _color = Character.toUpperCase(_color);
        if(_color != 'B' && _color != 'W'){
            throw new IllegalArgumentException("Possible color only Black(B) or White(W)...");
        }else{
            color = _color;
        }
        id = _id;
        setType();
    }

    /*@return a table containing the possible target of the move
    can generate also position that are out of game board, but in this case, the boardClass will exclude that possibility
     */
    public abstract Vector2[] getPossibleMoves();

    //returns true if the destination indicated is a possible target, and then move itself
    public void move(Vector2 _destination){
        if(_destination.isBetweenLimits(1,1,8,8)){
            position = _destination;
        }else{
            throw new IllegalArgumentException("Invalid target cell to move!");
        }
    }

    public String getId() {
        return id;
    }
    public Vector2 getPosition(){
        return position;
    }
    public String getType(){
        return type;
    }
    public String getColor(){
        if(color == 'B'){
            return "BLACK";
        }else{
            return "WHITE";
        }
    }
    //sets the type of the piece in base how to be written this method
    abstract void setType();

    /**
     * compare a piece with itself
     * @param p the other piece
     * @return true if the two pieces are equals
     */
    public boolean equals(Pezzo p){
        return p != null && p.getColor() == getColor() && p.getPosition().equals(getPosition()) && p.getType() == getType();
    }

    //implementing interfaces

    @Override
    public Image getGraphicalView() {
        String _color = getColor().toLowerCase();
        _color = _color.substring(0,1).toUpperCase() + _color.substring(1);
        return Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/pieces/" +
                getType() + "_" + _color + ".png"));
    }

    /**
     * check if the oposite king is under attack by me
     * @return true if am i checking king
     */
    @Override
    public boolean amICheckingKing(GameBoard board) {
        Vector2 kingPos = board.getPlayer((getColor().toLowerCase().equals("black") ? 0 : 1)).getPieces()[12].getPosition();//get the king of the else color position
        Vector2[] moves = RouteChecker.getPossibleMoves(this, board);
        for(Vector2 move : moves){
            if(move.equals(kingPos)){
                return true;
            }
        }
        return false;
    }

    /**
     * check if is actually a possible target of a move
     * @param _pos the destination
     * @return true if i can go there
     */
    @Override
    public boolean canIGoHere(Vector2 _pos, GameBoard board) {
        Vector2[] moves = RouteChecker.getPossibleMoves(this, board);
        for(Vector2 move : moves){
            if(move.equals(_pos)){
                return true;
            }
        }
        return false;
    }

    /**
     * Create a clone
     * @return a clone not refered at the original Piece
     */
    public Pezzo clone(){
        Pezzo p;
        if(this instanceof Pedone){
            p = new Pedone(this.getId(), this.color, new Vector2(this.getPosition().x, this.getPosition().y));
        }else if(this instanceof King){
            p = new King(this.getId(), this.color, new Vector2(this.getPosition().x, this.getPosition().y));
        }else if(this instanceof Queen){
            p = new Queen(this.getId(), this.color, new Vector2(this.getPosition().x, this.getPosition().y));
        }else if(this instanceof Horse){
            p = new Horse(this.getId(), this.color, new Vector2(this.getPosition().x, this.getPosition().y));
        }else if(this instanceof Ensign){
            p = new Ensign(this.getId(), this.color, new Vector2(this.getPosition().x, this.getPosition().y));
        }else if(this instanceof Tower){
            p = new Tower(this.getId(), this.color, new Vector2(this.getPosition().x, this.getPosition().y));
        }else{
            p = null;
            System.err.println("null");
        }
        return p;
    }
}
