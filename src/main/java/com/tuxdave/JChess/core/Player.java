package com.tuxdave.JChess.core;

import com.tuxdave.JChess.core.pieces.*;
import com.tuxdave.JChess.extras.Vector2;

import java.text.ParseException;

public class Player {
    private final String color;
    public String nick;
    private Pezzo[] pieces;

    /**
     * @param _nick player's nickname
     * @param _c player's color [1 = white/ 2 = black]
     */
    public Player(String _nick, int _c ){
        nick = _nick;
        if(_c == 1){
            color = "white";
        }else if(_c == 2){
            color = "black";
        }else{
            throw new IllegalArgumentException("Color must be only 1(white) or 2(black)");
        }

        //pieces creation
        short y = (short)(color.equals("white") ? 2 : 7);
        pieces = new Pezzo[16];
        for(short i = 0; i < GameBoard.limits; i++){//pedoni created
            pieces[i] = new Pedone("pedone" + (i+1), color.toCharArray()[0], new Vector2(i+1,y));
        }
        if (color.equals("white")) { //update of y of the position
            y--;
        } else {
            y++;
        }//y updated, creation of all the other pieces
        short x = 1;
        pieces[8] = new Tower("tower1", color.toCharArray()[0], new Vector2(x++, y));
        pieces[9] = new Horse("horse1", color.toCharArray()[0], new Vector2(x++, y));
        pieces[10] = new Ensign("ensign1", color.toCharArray()[0], new Vector2(x++, y));
        pieces[11] = new Queen("queen", color.toCharArray()[0], new Vector2(x++, y));
        pieces[12] = new King("king", color.toCharArray()[0], new Vector2(x++, y));
        pieces[13] = new Ensign("ensign2", color.toCharArray()[0], new Vector2(x++, y));
        pieces[14] = new Horse("horse2", color.toCharArray()[0], new Vector2(x++, y));
        pieces[15] = new Tower("tower2", color.toCharArray()[0], new Vector2(x, y));
    }

    public String getColor() {
        return color;
    }
    public Pezzo[] getPieces(){
        return pieces.clone();
    }
}
