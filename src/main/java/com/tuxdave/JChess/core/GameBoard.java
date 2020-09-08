package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.listener.ActionNotifier;
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
    private final List<GameListener> gameListeners = new ArrayList<GameListener>();
    private final List<ActionNotifier> actionNotifiers = new ArrayList<ActionNotifier>();

    public GameBoard saveBeforeMove;

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

    public GameBoard(){
        addGameListener(this);
    }

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
        King king = (King)getPieceByIdAndColor("king", turn);
        if(king.amIUnderAttack(this)){
            for(ActionNotifier a : actionNotifiers){
                a.notifyKingUnderAttackOnTurnEnds("After this move your king is again checked, change move...");
            }
            applySnapShot(saveBeforeMove);
            return;
        }

        turn = turn.equals("WHITE") ? "BLACK" : "WHITE";
        for(GameListener l : gameListeners){
            if(l != null){
                l.turnPassed(turn);
            }
        }

    }

    /**
     * create a snapshot of the current position of the game
     * @return a new board: the snapshot
     */
    public GameBoard createSnapShot(){
        GameBoard b = new GameBoard();
        for(int n = 0; n < 2;n++){
            for(int i = 0; i < 16; i++){
                if(players[n].getPieces()[i] != null)
                    b.getPlayer(n).re_AssignPiece(players[n].getPieces()[i].clone(), i);
                else
                    b.getPlayer(n).re_AssignPiece(null, i);
            }
        }
        for(ActionNotifier a : actionNotifiers){
            b.addActionNorifiers(a);
        }
        for(GameListener g : gameListeners){
            b.addGameListener(g);
        }
        b.turn = turn;
        return b;
    }

    public void applySnapShot(GameBoard b){
        for(int n = 0; n < 2;n++){
            for(int i = 0; i < 16; i++){
                if(b.getPlayer(n).getPieces()[i] != null)
                    players[n].re_AssignPiece(b.getPlayer(n).getPieces()[i].clone(), i);
                else
                    players[n].re_AssignPiece(null, i);
            }
        }
        //remove the listener duplication:
        removeAllActionNotifier();
        removeAllGameListener();

        for(ActionNotifier a : b.actionNotifiers){
            addActionNorifiers(a);
        }
        for(GameListener g : b.gameListeners){
            addGameListener(g);
        }
        turn = b.turn;
    }

    //ListenerManagement

    public void addGameListener(GameListener g){
        gameListeners.add(g);
    }
    public void removeAllGameListener(){
        for(int i = 0; i < gameListeners.size(); i++){
            gameListeners.remove(i);
        }
    }
    public void addActionNorifiers(ActionNotifier a) { actionNotifiers.add(a); }
    public void removeAllActionNotifier(){
        for(int i = 0; i < actionNotifiers.size(); i++){
            actionNotifiers.remove(i);
        }
    }

    /**
     * notify which is the current turn
     * @param turn the turn incoming (BLACK/WHITE)
     */
    @Override
    public void turnPassed(String turn){
        Pezzo[] pieces = getPlayer((turn.toLowerCase().equals("white") ? 1 : 0)).getPieces();//get the opposite pieces in base the current turn
        boolean check = false;
        for(Pezzo piece : pieces){
            if(piece != null && piece.amICheckingKing(this)){
                check = true;
            }
        }
        if(check){
            for(ActionNotifier a : actionNotifiers){//notify the attack to the UI
                if(a != null){
                    a.kingChecked(getPlayer((turn.equals("WHITE") ? 0 : 1)).nick);//notify which player has the king under attack
                }
            }
        }
    }

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