package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.pieces.*;
import com.tuxdave.JChess.extras.Vector2;

import java.util.Arrays;

/**
 * class that will check the pieces route on moving and stop it if a orute is occupied
 */
public class RouteChecker{

    private static Vector2[] rayCasts = new Vector2[8];

    private static short NORTH_EAST = 0;
    private static short SOUTH_EAST = 1;
    private static short NORTH_WEST = 2;
    private static short SOUTH_WEST = 3;
    private static short NORTH = 4;
    private static short SOUTH = 5;
    private static short EAST = 6;
    private static short WEST = 7;

    private static void inizialize(){//inizializing
        King k = new King("a", 'B', new Vector2(0,0));
        rayCasts = k.getPossibleMoves();//i used a king on 0,0 to get the direction of the raycasts.
    }

    /**
     * @param p the piece from which calculate the possible moves in base of routes
     * @param board the game board used
     * @return an array containing all the possible moves
     */
    public static Vector2[] getPossibleMoves(Pezzo p, GameBoard board){
        inizialize();//creating the internal state of the class

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
            selectedCells = Arrays.copyOf(selectedCells, l);//resize: leaving the void position in the array
            //finish base selection (obviusly it will be deleted)

            if(!(p instanceof Horse)) {//all piece which aren't horse
                //start advanced selection (calculating routes)
                Vector2[] myRays = null;
                switch (p.getType()) {
                    case "pedone":
                        myRays = new Vector2[1];
                        myRays[0] = rayCasts[NORTH];
                        break;
                    case "ensign":
                        myRays = new Vector2[4];
                        myRays[0] = rayCasts[NORTH_EAST];
                        myRays[1] = rayCasts[NORTH_WEST];
                        myRays[2] = rayCasts[SOUTH_EAST];
                        myRays[3] = rayCasts[SOUTH_WEST];
                        break;
                    case "tower":
                        myRays = new Vector2[4];
                        myRays[0] = rayCasts[NORTH];
                        myRays[1] = rayCasts[SOUTH];
                        myRays[2] = rayCasts[WEST];
                        myRays[3] = rayCasts[EAST];
                        break;
                    case "king":
                    case "queen":
                        myRays = rayCasts; //all direction
                        break;
                    default:
                        throw new IllegalArgumentException("Type of piece (" + p.getType() + ")not expected!");
                }
                Vector2[][] routes = new Vector2[myRays.length][];
                for (short i = 0; i < myRays.length; i++) {
                    routes[i] = getRouteByRay(p, myRays[i]);
                }//now i have all the routes of the piece

                //copying the final routes in the returned variable
                l = 0; //compute the lenght of "selectedCells"
                selectedCells = new Vector2[l];
                for (Vector2[] route : routes) {
                    for (Vector2 cell : route) {
                        if (GameBoard.isAnAcceptableCell(cell)) {
                            selectedCells = Arrays.copyOf(selectedCells, ++l);
                            selectedCells[l - 1] = cell;
                        }
                    }
                }
            }else{//about horse
                selectedCells = new Vector2[8];
                l = 0;
                for(Vector2 cell : p.getPossibleMoves()){
                    if(GameBoard.isAnAcceptableCell(cell)){
                        selectedCells[l++] = cell;
                    }
                }
                selectedCells = Arrays.copyOf(selectedCells, l);
            }
        }else{//da qui è OK
            selectedCells = new Vector2[]{};
        }
        return selectedCells;
    }

    private static void checkRoute(){

    }

    /**
     * @param p piece which from calculate route
     * @param ray the route direction
     * @return the cells interested in route
     */
    private static Vector2[] getRouteByRay(Pezzo p, Vector2 ray){
        Vector2[] rets = new Vector2[GameBoard.limits];
        short l = 0;
        Vector2 pos = p.getPosition();
        for(Vector2 cell : p.getPossibleMoves()){
            Vector2 pos1 = Vector2.add(pos, ray);
            for(int i = 0; i < GameBoard.limits; i++){
                if(cell.equals(pos1)){
                    rets[l++] = cell;
                    break;
                }
                pos1 = Vector2.add(pos1, ray);
            }
        }
        return Arrays.copyOf(rets, l);
    }
}
