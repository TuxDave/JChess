package com.tuxdave.JChess.core;

import com.tuxdave.JChess.UI.GraphicalBoard;
import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.extras.Vector2;

import java.util.Arrays;

/**
 * class that will check the pieces route on moving and stop it if a orute is occupied
 */
public class RouteChecker {
    /**
     * @param p the piece from which calculate the possible moves in base of routes
     * @param board the game board used
     * @return an array containing all the possible moves
     */
    public static Vector2[] getPossibleMoves(Pezzo p, GameBoard board){
        //start base selection
        Vector2[] selectedCells = new Vector2[]{};
        if(p != null){
            String pColor = p.getColor();
            Vector2[] tempSel = p.getPossibleMoves();
            int l = 0;
            for(Vector2 _cell : tempSel){//find the max length of the array with possible position
                if(GameBoard.isAnAcceptableCell(_cell)){
                    l++;
                }
            }
            selectedCells = new Vector2[l];
            l = 0;
            //now i will leave a position if isn't there a friendly piece
            for(Vector2 _cell : tempSel){
                if(GameBoard.isAnAcceptableCell(_cell)){
                    if(!board.isThereAPiece(_cell)){
                        selectedCells[l++] = _cell;
                    }else {
                        Pezzo p1 = board.getPieceByPosition(_cell);
                        if(!p1.getColor().equals(p.getColor())){
                            selectedCells[l++] = _cell;
                        }
                    }
                }
            }
            selectedCells = Arrays.copyOf(selectedCells, l-1);//resize: leaving the void position in the array
            //finish base selection
            //start advanced selection (calculating routes)
            //todo: calcolare le rotte da qui
        }else{//da qui è OK
            selectedCells = new Vector2[]{};
        }
        return selectedCells;
    }
}
