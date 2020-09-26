package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.listener.GameListener;
import com.tuxdave.JChess.core.pieces.*;
import com.tuxdave.JChess.extras.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameLogger implements GameListener{
    public String history;
    private String lastMove;
    private String possibleArrocco;
    private boolean arroccoDone = false;

    public GameLogger(){
        history = "";
    }

    /**
     * add to the history the last move made
     */
    public void confirmLastMove(){
        if(arroccoDone){
            arroccoDone = false;
            lastMove = possibleArrocco;
        }
        history += lastMove + " ";
        System.out.println("history: " + history);
    }

    /**
     * @param type the arrocco type
     */
    public void createArroccoSignature(String type){
        if(type.equals("short")){
            possibleArrocco = "0-0";
        }else{
            possibleArrocco = "0-0-0";
        }
        arroccoDone = true;
    }

    /**
     * small interface to create a local method in the below method
     */
    private interface makeLastMoveInterface{
        String createMove(String mod);
        String createModifierFrom2Pieces(Pezzo p1, Pezzo p2);
    }
    /**
     * create and set the lastMove if is a simply move (not captures ecc)
     * @param p the source piece
     * @param snapshot the board after the move
     * @param isCheck true if now the king is checked
     * @param n the number of the living pieces
     */
    public void makeLastMove(final Pezzo p, final GameBoard snapshot, final int n, final boolean isCheck){

        makeLastMoveInterface i1 = new makeLastMoveInterface() {
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

            @Override
            public String createModifierFrom2Pieces(Pezzo p1, Pezzo p2) {
                String m;
                if (p1.getPosition().x == p2.getPosition().x) {//on the same column
                    m = Integer.toString(p1.getPosition().y);
                } else if (p1.getPosition().y == p2.getPosition().y) { //on the same row
                    m = Character.toString(((char) (p1.getPosition().x + 96)));
                } else { //on different row and column
                    m = Character.toString((char) (p1.getPosition().x + 96));
                }
                return m;
            }
        };

        String log;
        if(!(p instanceof Pedone)){ //----------TOWER and HORSE and ENSIGN and QUEEN----------
            //are there two pieces that can go to the same destination?
            Pezzo[] ps = snapshot.getAllPieces();

            Pezzo p1 = null, p2 = null, p3 = null, p4 = null;
            String id = p.getId();
            List<String> ids = new ArrayList<String>();

            for(Pezzo piece : ps){
                if(piece != null && piece.getId() != p.getId() && piece.getType().equals(p.getType()) && piece.getColor().equals(p.getColor()) && piece.canIGoHere(p.getPosition(), snapshot)){
                    //if piece isn't p, is a his friend and can go to him
                    ids.add(piece.getId());
                }
            }

            String modifier;   //if ids.size() == 0
            switch(ids.size()) {
                case 0:
                    modifier = "";
                    break;
                case 1:{
                    //creating modifier
                    p1 = snapshot.getPieceByIdAndColor(id, p.getColor());
                    p2 = snapshot.getPieceByIdAndColor(ids.get(0), p.getColor());
                    modifier = i1.createModifierFrom2Pieces(p1,p2);
                }
                break;
                case 2:{
                    p1 = snapshot.getPieceByIdAndColor(id, p.getColor());
                    p2 = snapshot.getPieceByIdAndColor(ids.get(0), p.getColor());
                    p3 = snapshot.getPieceByIdAndColor(ids.get(1), p.getColor());
                    //if((p1.getPosition().x == p2.getPosition().x || p1.getPosition().y == p2.getPosition().y) && (p1.getPosition().x == p3.getPosition().x || p1.getPosition().y == p3.getPosition().y))
                    boolean p2Alligned = p1.getPosition().x == p2.getPosition().x || p1.getPosition().y == p2.getPosition().y;
                    boolean p3Alligned = p1.getPosition().x == p3.getPosition().x || p1.getPosition().y == p3.getPosition().y;
                    if(p2Alligned && p3Alligned){
                        modifier = fromCoordsToString(p1.getPosition());
                    }else if(p2Alligned){
                        modifier = i1.createModifierFrom2Pieces(p1, p2);
                    }else if(p3Alligned){
                        modifier = i1.createModifierFrom2Pieces(p1, p3);
                    }else{
                        modifier = Character.toString((char) (p1.getPosition().x + 96));
                    }
                }
                break;
                case 3:
                default:{
                    Pezzo[] pezzi = new Pezzo[ids.size()+1];
                    pezzi[0] = snapshot.getPieceByIdAndColor(id, p.getColor());
                    //now the modifier is null
                    boolean orizontalAlligned = false, verticalAlligned = false;
                    //let's make true if needed
                    for(int i = 1; i < ids.size()+1; i++){//from second to fourth element

                        pezzi[i] = snapshot.getPieceByIdAndColor(ids.get(i-1), p.getColor());//assign all pieces

                        if(pezzi[0].getPosition().x == pezzi[i].getPosition().x){
                            orizontalAlligned = true;
                        }
                        if(pezzi[0].getPosition().y == pezzi[i].getPosition().y){
                            verticalAlligned = true;
                        }
                    }

                    //now intersecate the result
                    //if a boolean is true, the opposite dimension must be present, if all two are true, both the dimension have to be present
                    if(orizontalAlligned && verticalAlligned){
                        modifier = fromCoordsToString(pezzi[0].getPosition());
                    }else if(orizontalAlligned){
                        modifier = Integer.toString(pezzi[0].getPosition().y);
                    }else if(verticalAlligned){
                        modifier = Character.toString((char) (pezzi[0].getPosition().x + 96));
                    }else{
                        modifier = "";
                    }
                }
                break;
            }
                //modifier defined, let's create the move notation
            log = i1.createMove(modifier);
        }else{//----------PEDONE----------
            Pezzo p1 = snapshot.getPieceByIdAndColor(p.getId(), p.getColor());
            char modifier = (char)(p1.getPosition().x+96);
            log = i1.createMove(Character.toString(modifier));
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
    public void onMove(Pezzo p) {/*ignored*/}

    @Override
    public void onMove(String type){/*ignored*/}

    @Override
    public void onMorph(String newType) {
        lastMove += '=' + fromPieceToFirstLetter(newType);
    }
}
