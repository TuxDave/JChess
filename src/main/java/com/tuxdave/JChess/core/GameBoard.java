package com.tuxdave.JChess.core;

public class GameBoard {
    public static final short limits = 8;

    //containts true if a piece is there a piece in that position
    private boolean[][] board = new boolean[limits][limits];
    {//inizializing
        for(int i = 0; i < limits; i++){
            for(int j = 0; j < limits; j++){
                board[i][j] = false;
            }
        }
    }


}