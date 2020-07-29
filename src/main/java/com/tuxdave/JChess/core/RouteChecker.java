package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.pieces.King;
import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.extras.Vector2;

import java.util.Arrays;

/**
 * class that will check the pieces route on moving and stop it if a orute is occupied
 */
public class RouteChecker{

    private static Vector2[] rayCasts = new Vector2[8];

    private final static short NORTH_EAST = 0;
    private final static short SOUTH_EAST = 1;
    private final static short NORTH_WEST = 2;
    private final static short SOUTH_WEST = 3;
    private final static short NORTH = 4;
    private final static short SOUTH = 5;
    private final static short EAST = 6;
    private final static short WEST = 7;

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

        Vector2[] selectedCells = new Vector2[]{};
        short l;
        if(p != null){
            if(p.getType() != "horse" && p.getType() != "king") {//all piece which aren't horse and king
                //start advanced selection (calculating routes)
                Vector2[] myRays = null;
                switch (p.getType()) {
                    case "pedone":
                        myRays = new Vector2[1];
                        if(p.getColor() == "WHITE")
                            myRays[0] = rayCasts[NORTH];
                        else
                            myRays[0] = rayCasts[SOUTH];
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

                //start the real routeChecker!
                for(short i = 0; i < routes.length; i++){
                    routes[i] = checkRoute(p, board, routes[i]);
                }
                //route checker finished

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
            }else{//about horse and king
                selectedCells = new Vector2[8];
                l = 0;
                for(Vector2 cell : p.getPossibleMoves()){
                    if(GameBoard.isAnAcceptableCell(cell)){
                        if(!board.isThereAPiece(cell)){
                            selectedCells[l++] = cell;
                        }else{
                            Pezzo p1 = board.getPieceByPosition(cell);
                            if(!p1.getColor().equals(p.getColor())){
                                selectedCells[l++] = cell;
                            }
                        }
                    }
                }
                selectedCells = Arrays.copyOf(selectedCells, l);
            }
        }else{//da qui è OK
            selectedCells = new Vector2[]{};
        }
        return selectedCells;
    }

    /**
     * create a correct route using collision methods.
     * @param p piece from which is generated the route
     * @param board game board containing all other pieces
     * @param route the route followed
     * @return a correct route
     */
    private static Vector2[] checkRoute(Pezzo p, GameBoard board, Vector2[] route){
        //setup
        short index, direction, l = 0;
        if(p.getPosition().getClosest(route[0], route[route.length-1]).equals(route[0])){
            index = 0;
            direction = 1;
        }else{
            index = (short) (route.length-1);
            direction = -1;
        }
        //start routing
        Vector2[] finalRoute = route;
        for(; (index == route.length-1 ? index >= 0 : index < route.length); index = (short) (index + direction)){
            if(!board.isThereAPiece(route[index])){ //if isn't there a piece
                finalRoute[l++] = route[index];
            }else{
                if(board.getPieceByPosition(route[index]).getColor().equals(p.getColor())){
                    //if the piece which collided with me is a friend, i will only stop the process
                    break;
                }else{
                    //i will hilight its cell because i can eat it
                    finalRoute[l++] = route[index];
                    break;
                }
            }
        }
        return Arrays.copyOf(finalRoute, l);
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
