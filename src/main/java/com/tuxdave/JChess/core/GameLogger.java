package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.listener.GameListener;
import com.tuxdave.JChess.core.pieces.*;
import com.tuxdave.JChess.extras.Vector2;

public class GameLogger implements GameListener{
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
     * small interface to create a local method in the below method
     */
    private interface makeLastMoveInterface{
        String createMove(String mod);
    }
    /**
     * create and set the lastMove if is a simply move (not captures ecc)
     * @param p the source piece
     * @param snapshot the board after the move
     * @param isCheck true if now the king is checked
     * @param n the number of the living pieces
     */
    public void makeLastMove(final Pezzo p, final GameBoard snapshot, final int n, final boolean isCheck){

        makeLastMoveInterface i = new makeLastMoveInterface() {

            /**
             * create the log string
             * @param mod the modifier (when there are collisions) "" if there isn't
             * @return the move like notation
             */
            @Override
            public String createMove(String mod) {
                String log;
                String piece = fromPieceToFirstLetter(p.getType());
                String move = fromCoordsToString(p.getPosition());
                if(snapshot.getPieceNumber() == n) { //if no piece was ate now
                    if (p instanceof Pedone) {//if p is a pawn and no piece was ate now, modifier = ""
                        mod = "";
                    }
                    log = piece + mod + move + (isCheck ? "+" : "");
                }else{
                    log = piece + mod + "x" /*+ fromPieceToFirstLetter(snapshot.getPieceByPosition(p.getPosition()).getType())*/ + move + (isCheck ? "+" : "");//i find the eaten piece.
                    //the commented part of the line over is unnecessary...
                }
                return log;
            }
        };

        String log;
        if(!(p instanceof Pedone)){ //----------TOWER and HORSE and ENSIGN and QUEEN----------
            //are there two pieces that can go to the same destination?
            Pezzo p1, p2;
            String id = p.getId(), id2 = p.getId();

            if(id2.toCharArray()[id2.length()-1] == '1'){
                id2 = id2.substring(0, id2.length()-1);
                id2 += '2';
            }else if(id2.toCharArray()[id2.length()-1] == '2'){
                id2 = id2.substring(0, id2.length()-1);
                id2 += '1';
            }/*else{ todo: finish this: the anti-collision method multiple: with the fake pieces (from pawn morph) and with, for ex: 3 ensigns
                for(Pezzo piece : snapshot.getAllPieces()){
                    if(piece != null && piece.getType().equals(p.getType()) && piece.getColor().equals(p.getColor()) && piece.canIGoHere(p.getPosition(), snapshot)){
                        //if one piece (and one only for now) can collide with the other one
                        id2 = id2.substring(0, id2.length()-1);
                        id2 += piece.getId().toCharArray()[piece.getId().length()-1];
                    }
                }
            }*/

            p1 = snapshot.getPieceByIdAndColor(id, p.getColor());
            p2 = snapshot.getPieceByIdAndColor(id2, p.getColor());
            if(p2 != null && p2.canIGoHere(p.getPosition(), snapshot)){//if the other same piece can go to the position of p
                char modifier;//contains the char of the row or column to identify which piece correctly
                if(p1.getPosition().x == p2.getPosition().x){//on the same column
                    modifier = Integer.toString(p1.getPosition().y).toCharArray()[0];
                }else if(p1.getPosition().y == p2.getPosition().y){ //on the same row
                    modifier = (char)(p1.getPosition().x+96);
                }else{ //on different row and column
                    modifier = (char)(p1.getPosition().x+96);
                }
                //modifier defined, let's create the move notation
                log = i.createMove(Character.toString(modifier));
            }else{//if isn't there the notation "collision" problem
                log = i.createMove("");
            }
        }else{//----------PEDONE----------
            Pezzo p1 = snapshot.getPieceByIdAndColor(p.getId(), p.getColor());
            char modifier = (char)(p1.getPosition().x+96);
            log = i.createMove(Character.toString(modifier));
        }
        lastMove = log;
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

    //GameLogger Listener Manager Segment Below


    @Override
    public void turnPassed(String turn) {/*ignored*/}

    @Override
    public void arrocco(King k, String type) {/*ignored*/}

    @Override
    public void logMove()  {/*ignored*/}

    @Override
    public void onMove(Pezzo p) {/*ignored*/}

    @Override
    public void onMove(String type)  {/*ignored*/}

    @Override
    public void onMorph(String newType) {
        lastMove += '=' + fromPieceToFirstLetter(newType);
    }
}
