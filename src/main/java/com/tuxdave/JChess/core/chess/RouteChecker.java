package com.tuxdave.JChess.core.chess;

import com.tuxdave.JChess.core.chess.pieces.*;
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
                            if(p instanceof Horse)
                                selectedCells[l++] = cell;
                            else //added kamikaze move prevent
                                if(canKingMoveHere(p.getColor(), board, cell))
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

                //to allow king to move arrocco:
                if(p instanceof King && !((King)p).alreadyMoved){//if is a King and not moved yet
                    short n = 8;
                    for(int i = 0; i < 2; i++){
                        boolean doIt = true;
                        Pezzo t = board.getPieceByPosition(new Vector2(n, p.getPosition().y));
                        if (t instanceof Tower && t.getColor().equals(p.getColor())) {
                            for(int j = p.getPosition().x+(i == 0 ? 1 : -1); (i == 0 ? j < t.getPosition().x : j > t.getPosition().x); j += (i == 0 ? 1 : -1)){//for each piece till the tower
                                if(board.isThereAPiece(new Vector2(j, p.getPosition().y))){
                                    doIt = false;
                                }
                            }

                            //decide if the arrocco is possible
                            boolean canDo = true;
                            Vector2 position = p.getPosition();
                            int direction = (i == 0 ? 1 : -1);//set the direction of arrocco
                            Vector2 dest = new Vector2(position.x + direction*2, position.y),
                                    street = new Vector2(position.x + direction, position.y);
                            Pezzo[] pieces = board.getPlayer((p.getColor().toLowerCase().equals("black") ? 0 : 1)).getPieces(); //get the opposite pieces
                            for(Pezzo piece : pieces){
                                if( (!(piece instanceof King)) && piece != null && (piece.canIGoHere(dest, board) || piece.canIGoHere(street, board))){
                                    canDo = false;
                                }
                            }

                            //apply
                            if(doIt && canDo){//if isn't there any piece till the tower we can do the Arrocco and isn't there dangerous route
                                selectedCells = Arrays.copyOf(selectedCells, ++l);
                                selectedCells[l - 1] = t.getPosition();
                            }
                        }
                        n = 1;
                    }
                }
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
                if(p instanceof Pedone || board.getPieceByPosition(route[index]).getColor().equals(p.getColor())){
                    //if the piece which collided with me is a friend of i'm a pawn, i will only stop the process
                    break;
                }else{
                    //i will hilight its cell because i can eat it
                    finalRoute[l++] = route[index];
                    break;
                }
            }
        }
        finalRoute = Arrays.copyOf(finalRoute, l);
        if(p instanceof Pedone){
            direction = (short) (p.getColor().equals("WHITE") ? 1 : -1);
            int orientation = 1;
            {//diagonal check
                for (int i = 0; i < 2; i++) {
                    Vector2 pos = Vector2.add(p.getPosition(), new Vector2(orientation, direction));
                    if (board.isThereAPiece(pos)) {
                        Pezzo pTemp = board.getPieceByPosition(pos);
                        if (!pTemp.getColor().equals(p.getColor())) {
                            finalRoute = Arrays.copyOf(finalRoute, ++l);
                            finalRoute[l - 1] = pos;
                        }
                    }
                    orientation = -1;
                }
            }{//en passant
                Vector2 pos = new Vector2(p.getPosition().x-1 ,p.getPosition().y);
                for(int i = 0; i < 2; i++){
                    Pezzo pTemp = board.getPieceByPosition(pos);
                    if(pTemp instanceof Pedone && !pTemp.getColor().equals(p.getColor()) && ((Pedone) pTemp).justDoubleCase){
                        finalRoute = Arrays.copyOf(finalRoute, ++l);
                        finalRoute[l-1] = pos;
                    }
                    pos = new Vector2(p.getPosition().x+1, p.getPosition().y);
                }
            }
        }
        return finalRoute;
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

    /**
     * returns if is possible the move by king
     * @param _color "WHITE/BLACK" color of the king
     * @param board the game board
     * @param destination the destination cell
     * @return true if the king can move here
     */
    public static boolean canKingMoveHere(String _color, GameBoard board, Vector2 destination){
        /*Pezzo[] pieces = board.getPlayer(_color.equals("WHITE") ? 1 : 0).getPieces();
        for(Pezzo p : pieces){
            if(!(p instanceof King) && p != null && p.canIGoHere(destination, board)){
                return false;
            }
        }*/

        //check the other moves
        GameBoard snapShot = board.createSnapShot();
        snapShot.getPieceByIdAndColor("king", _color).move(destination);
        King king = (King)snapShot.getPieceByIdAndColor("king", _color);
        if(king.amIUnderAttack(snapShot)){
            return false;
        }

        return true;
    }
}
