package com.tuxdave.JChess.UI;

import com.tuxdave.JChess.core.GameBoard;
import com.tuxdave.JChess.extras.Vector2;

import javax.swing.*;
import java.awt.*;

public class GraphicalBoard extends JComponent {

    private Vector2[] selectedCells = {};

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

        //this hilight some cells
        highlightCell(g);
        //todo: add the method that update the array containing the list of the selected cells

        //todo: add the method that paints the pieces on screen
    }

    /**
     * paints green color on the selected cells
     */
    private void highlightCell(Graphics g){
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/selected_background.png"));
        for(Vector2 _cell : selectedCells){
            if (isAnAcceptableCell(_cell)) {
                _cell = getPixelCoordsFromCellCoords(_cell);
                _cell.y = convertCoordsFromReal(_cell.y);
                g.drawImage(img, _cell.x, _cell.y, this);
            } else {
                throw new IllegalArgumentException("Cell not found!");
            }
        }
    }

    /**
     * @param _cell the cell to tests
     * @return true if the cell is acceptable (between the board limits)
     */
    private static boolean isAnAcceptableCell(Vector2 _cell){
        if(_cell.isBetweenLimits(0,0,GameBoard.limits, GameBoard.limits)){
            return true;
        }else{
            return false;
        }
    }
    /**
     * @param _x the cell's x to tests
     * @param _y the cell's y to tests
     * @return true if the cell is acceptable (between the board limits)
     */
    private static boolean isAnAcceptableCell(int _x, int _y){
        return isAnAcceptableCell(new Vector2(_x, _y));
    }

    /**
     * @param _cellCoords cell's coords to tranform in the coords of the pixel on north-ovest of the cell size
     * @return a Vector2 containing the coords by pixel
     */
    private static Vector2 getPixelCoordsFromCellCoords(Vector2 _cellCoords){
        return new Vector2((_cellCoords.x-1)*64, _cellCoords.y*64);
    }

    /**
     * this method converts a matematic y coordinate in a swing coordinates (so using 0,0 on left-top) and a place of 512x512
     * @param _y geometric coords using 0,0 on left-bottom
     * @return the new y converted
     */
    private int convertCoordsFromReal(int _y){
        return getPreferredSize().height - _y;
    }
}
