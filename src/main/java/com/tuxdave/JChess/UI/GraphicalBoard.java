package com.tuxdave.JChess.UI;

import com.tuxdave.JChess.extras.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;

public class GraphicalBoard extends JComponent {

    {//static properties
        setBorder(BorderFactory.createLineBorder(Color.blue));
        setPreferredSize(new Dimension(512,512));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draws table's lines
        final short CELL_SIZE = 64;
        for(int i = 0; i < 8; i++){
            g.drawLine(0, (i+1)*CELL_SIZE, getPreferredSize().width, (i+1)*CELL_SIZE);
            g.drawLine((i+1)*CELL_SIZE,0,(i+1)*CELL_SIZE, getPreferredSize().width);
        }

        //paints to grey some cells
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/grey_background.png"));
        short p = 0;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(j % 2 == p){
                    g.drawImage(image, j * CELL_SIZE, convertCoordsFromReal((i + 1) * CELL_SIZE), this);
                }
            }
            if(p == 0){
                p = 1;
            }else{
                p = 0;
            }
        }
    }

    /**
     * this method convert a matematic y coordinate in a swing coordinates (so using 0,0 on left-top) and a place of 512x512
     * @param _y geometric coords using 0,0 on left-bottom
     * @return the new y converted
     */
    private int convertCoordsFromReal(int _y){
        return getPreferredSize().height - _y;
    }
}
