package com.tuxdave.JChess.core.chess;

import com.tuxdave.JChess.core.chess.listener.ActionNotifier;
import com.tuxdave.JChess.core.chess.listener.GameListener;
import com.tuxdave.JChess.core.chess.pieces.King;
import com.tuxdave.JChess.core.chess.pieces.Pedone;
import com.tuxdave.JChess.core.chess.pieces.Pezzo;
import com.tuxdave.JChess.core.chess.pieces.Tower;
import com.tuxdave.JChess.extras.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameBoard implements GameListener {
    public static final short limits = 8;
    private String turn;
    public final List<GameListener> gameListeners = new ArrayList<GameListener>();
    private final List<ActionNotifier> actionNotifiers = new ArrayList<ActionNotifier>();
    public GameBoard saveBeforeMove;

    public int currentTurn;
    public GameLogger logger;//also the listener of pawns

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
        logger = new GameLogger();
        turn = "WHITE";
        currentTurn = 0;

        players = new Player[2];//player inizializing
        {
            players[0] = new Player("p1", 1);//WHITE
            players[1] = new Player("p2", 2);//BLACK
            //add listener refert to kins
            ((King)players[0].getPieces()[12]).addGameListener(this);
            ((King)players[1].getPieces()[12]).addGameListener(this);
        }

        {//add logger to pawns as listener implementer
            Pezzo[] ps = getAllPieces();
            for(int i = 0; i < 32; i++){
                if(ps[i] instanceof Pedone){
                    ((Pedone) ps[i]).gameListener = logger;
                }
            }
        }
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

        //remove all existent listener (all scraps)
        b.removeAllActionNotifier();
        b.removeAllGameListener();

        for(Pezzo p : b.getAllPieces()){
            if(p instanceof Pedone){
                b.addGameListener((Pedone)p);
            }
        }

        for(ActionNotifier a : actionNotifiers){
            b.addActionNorifiers(a);
        }
        for(GameListener g : gameListeners){
            if(!(g instanceof Pedone)){
                b.addGameListener(g);
            }
        }
        b.turn = turn;
        b.currentTurn = currentTurn;
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

        for(Pezzo p : getAllPieces()){
            if(p instanceof Pedone){
                addGameListener((Pedone)p);
            }
        }

        for(ActionNotifier a : b.actionNotifiers){
            addActionNorifiers(a);
        }
        for(GameListener g : b.gameListeners){
            if(!(g instanceof Pedone)) {
                addGameListener(g);
            }
        }
        turn = b.turn;
        currentTurn = b.currentTurn;
    }

    /**
     * get the number of the pieces not null
     * @return the living pieces number
     */
    public int getPieceNumber(){
        int c = 0;
        Pezzo[] pieces = getAllPieces();
        for(int i = 0; i < 32; i++){
            if(pieces[i] != null){
                c++;
            }
        }
        return c;
    }

    //ListenerManagement

    public void addGameListener(GameListener g){
        gameListeners.add(g);
    }
    public void removeAllGameListener(){
        int n = gameListeners.size();
        for(int i = 0; i < n; i++){
            gameListeners.remove(0);
        }
    }
    public void addActionNorifiers(ActionNotifier a) {
        actionNotifiers.add(a);
    }
    public void removeAllActionNotifier(){
        int n = actionNotifiers.size();
        for(int i = 0; i < n; i++){
            actionNotifiers.remove(0);
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

        currentTurn++;
        logger.confirmLastMove();

    }

    @Override
    public void onMorph(String newType) {/*ignored*/}

    /**
     * called when a piece moves
     * @param p the piece moved
     */
    @Override
    public void onMove(Pezzo p) {
        //i will pass the snapshot to have the previus status of board
        logger.makeLastMove(p, saveBeforeMove, getPieceNumber(),((King)(getPieceByIdAndColor("king", (p.getColor().equals("WHITE") ? "BLACK" : "WHITE")))).amIUnderAttack(this) );
    }

    /**
     * called when play arrocco
     * @param type
     */
    @Override
    public void onMove(String type) {
        logger.createArroccoSignature(type);
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
                //listener call
                for(GameListener g : gameListeners){
                    g.onMove("short");
                }
            } else {
                Tower t = (Tower) getPieceByPosition(new Vector2(1, k.getPosition().y));
                t.move(new Vector2(4, t.getPosition().y));
                k.move(new Vector2(3, k.getPosition().y));
                //listener call
                for(GameListener g : gameListeners){
                    g.onMove("long");
                }
            }
        }catch(NullPointerException e){
            System.err.println("Unable to find a Tower in the target cell!");
        }
    }
}