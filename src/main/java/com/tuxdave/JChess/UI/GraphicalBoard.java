package com.tuxdave.JChess.UI;

import com.tuxdave.JChess.core.GameBoard;
import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.extras.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

public class GraphicalBoard extends JComponent {

    private Vector2[] selectedCells = {};
    private Vector2 hoveredCell = null;
    private GameBoard board = new GameBoard();
    private static final int CELL_SIZE = 64;

    {//static properties
        setBorder(BorderFactory.createLineBorder(Color.blue));
        setPreferredSize(new Dimension(512,512));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
    }
    {//adding some listeners
        GraphicalBoardListener l = new GraphicalBoardListener();
        addMouseListener(l);
        addMouseMotionListener(l);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draws table's lines
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

        //drawing pieces
        Pezzo[] pieces = board.getAllPieces();
        for(Pezzo p1 : pieces){
            if(p1 != null)
                drawPiece(p1,g);
        }
    }

    /**
     * paints green color on the selected cells
     * paints the blue rectangle around the hovered cell
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
        //hovering
        img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/mouseHover_rectangle.png"));
        if(hoveredCell != null){
            g.drawImage(img, hoveredCell.x, hoveredCell.y, this);
        }
    }

    /**
     * draw on this object a piece's graphical view
     * @param _p piece to be drawed
     * @param _g the graphics on which to draw the piece
     */
    private void drawPiece(Pezzo _p, Graphics _g){
        if(_p != null){
            Vector2 v = getPixelCoordsFromCellCoords(_p.getPosition());
            _g.drawImage(_p.getGraphicalView(), v.x, convertCoordsFromReal(v.y), this);
        }
    }

    /**
     * @param p - the piece from which to take the selected cells
     */
    private void updateSelectedCells(Pezzo p){ //todo: review this to calculate the routes
        if(p != null){
            String pColor = p.getColor();
            Vector2[] tempSel = p.getPossibleMoves();
            int l = 0;
            for(Vector2 _cell : tempSel){//find the max length of the array with possible position
                if(isAnAcceptableCell(_cell)){
                    l++;
                }
            }
            selectedCells = new Vector2[l];
            l = 0;
            //now i will leave a position if isn't there a friendly piece
            for(Vector2 _cell : tempSel){
                if(isAnAcceptableCell(_cell)){
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
        }else{//da qui è OK
            selectedCells = new Vector2[]{};
        }
        repaint();
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
        return new Vector2((_cellCoords.x-1)*CELL_SIZE, (_cellCoords.y)*CELL_SIZE);
    }

    /**
     *
     * @param _pixelCoords contains the pixel coords to convert in cell coords
     * @return a cell coord
     */
    private static Vector2 getCellCoordsFromPixelCoords(Vector2 _pixelCoords){
        return new Vector2((int)(_pixelCoords.x / 64) + 1, (int)(_pixelCoords.y / 64) + 1);
    }

    /**
     * this method converts a matematic y coordinate in a swing coordinates (so using 0,0 on left-top) and a place of 512x512
     * @param _y geometric coords using 0,0 on left-bottom
     * @return the new y converted
     */
    private int convertCoordsFromReal(int _y){
        return getPreferredSize().height - _y;
    }

    //i preferred creating a dedicated class that implements the methods because is clearer
    private class GraphicalBoardListener implements MouseListener, MouseMotionListener {
        private boolean eatingMode = false;

        /**catch the mouse position on the click time, checks if is there a piece and in case there is, get the possible moves*/
        private void startEatingMode(Vector2 clickCoords){
            eatingMode = true;
            //now check if is there a piece and if is there get the possible move and repaint all
            if(board.isThereAPiece(clickCoords)){
                updateSelectedCells(board.getPieceByPosition(clickCoords));
            }
        }

        /**eat a piece if is there one or simply move the piece*/
        private void stopEatingMode(Vector2 clickCoords){//todo: modify the selecter method and then this to allow it to move and eat.
            updateSelectedCells(null);
            eatingMode = false;
        }

        //mouseListener
        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            //save the coords in a vector2
            Vector2 clickCoords = new Vector2(mouseEvent.getX(), convertCoordsFromReal(mouseEvent.getY()));
            //then convert it in a cell coord
            clickCoords = getCellCoordsFromPixelCoords(clickCoords);
            if(!eatingMode){
                startEatingMode(clickCoords);
            }else{//eatingMode = true
                stopEatingMode(clickCoords);
            }
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) { }
        @Override
        public void mouseReleased(MouseEvent mouseEvent) { }
        @Override
        public void mouseEntered(MouseEvent mouseEvent) { }
        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            hoveredCell = null;
            repaint();
        }

        //mouseMotionListener
        @Override
        public void mouseDragged(MouseEvent mouseEvent) { }

        /**update HoveredCell in case the mouse changed cell position*/
        @Override
        public void mouseMoved(MouseEvent mouseEvent) {
            Vector2 hoverCoord = new Vector2(mouseEvent.getX(), mouseEvent.getY());
            hoverCoord = getCellCoordsFromPixelCoords(hoverCoord);
            hoverCoord.x = --hoverCoord.x*64;
            hoverCoord.y = --hoverCoord.y*64;
            if(hoveredCell == null || (!hoverCoord.equals(hoveredCell))){
                hoveredCell = hoverCoord;
                repaint();
            }
        }
    }
}
