package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.pieces.Horse;
import com.tuxdave.JChess.core.pieces.Pedone;
import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.core.pieces.Tower;
import com.tuxdave.JChess.extras.Vector2;

public class GameLogger {
    public String history;
    public String lastMove;

    public GameLogger(){
        history = "";
    }

    /**
     * add to the history the last move made
     */
    public void confirmLastMove(){
        history += lastMove + " ";
        System.out.println("history: " + history);
    }

    /**
     * create and set the lastMove if is a simply move (not captures ecc)
     * @param p the source piece
     * @param snapshot the board after the move
     * @param isCheck true if now the king is checked
     * @param n the number of the living pieces
     */
    public void makeLastMove(Pezzo p, GameBoard snapshot, int n, boolean isCheck){
        String log;
        if(!(p instanceof Tower) && !(p instanceof Horse) && !(p instanceof Pedone)){
            String piece = fromPieceToFirstLetter(p.getType());
            String move = fromCoordsToString(p.getPosition());
            if(snapshot.getPieceNumber() == n) //if no piece was ate now
                log = piece + move + (isCheck ? "+" : "");
            else{
                log = piece + "x" + fromPieceToFirstLetter(snapshot.getPieceByPosition(p.getPosition()).getType()) + move + (isCheck ? "+" : "");//i find the eaten piece.
            }
            lastMove = log;
        }else if(p instanceof Tower || p instanceof Horse){
            //todo: add here the check if two pieces can do the same move...
        }
    }

    /**
     * convert a geometric coord in a chess coord (2,3 -> b3)
     * @param c geometric coord
     * @return chess coord
     */
    public static String fromCoordsToString(Vector2 c){
        return new String((char)(c.x+96) + Integer.toString(c.y));
    }

    /**
     * return the letter of the piece like parameter
     * @param _type "king" or other one
     * @return "K" for example, "" for Pedone
     */
    public static String fromPieceToFirstLetter(String _type){
        String ret = "";

        switch(_type){
            case "king":
                ret = "K";
                break;
            case "queen":
                ret = "Q";
                break;
            case "ensign":
                ret = "B"; //like bishop...
                break;
            case "horse":
                ret = "N"; //like knight
                break;
            case "tower":
                ret = "R"; //like rook
                break;
            case "pedone":
                ret = ""; //pedone hasn't letter.
                break;
        }
        return ret;
    }
}
