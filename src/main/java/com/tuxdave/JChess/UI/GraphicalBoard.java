package com.tuxdave.JChess.UI;

import com.tuxdave.JChess.core.GameBoard;
import com.tuxdave.JChess.core.RouteChecker;
import com.tuxdave.JChess.core.pieces.Pedone;
import com.tuxdave.JChess.core.pieces.Pezzo;
import com.tuxdave.JChess.extras.Drawable;
import com.tuxdave.JChess.extras.PieceListener;
import com.tuxdave.JChess.extras.Vector2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GraphicalBoard extends JComponent {

    private Vector2[] selectedCells = {};
    private Vector2 hoveredCell = null;
    protected GameBoard board = new GameBoard();
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
            if(p1 != null) {
                try {
                    drawPiece(p1,g);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "There is some problems rendering a onScreen component");
                }
            }
        }
    }

    /**
     * paints green color on the selected cells
     * paints the blue rectangle around the hovered cell
     */
    private void highlightCell(Graphics g){
        Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/selected_background.png"));
        for(Vector2 _cell : selectedCells){
            if (GameBoard.isAnAcceptableCell(_cell)) {
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
    private void drawPiece(Pezzo _p, Graphics _g) throws Exception {
        if(!(_p instanceof Drawable)){
            throw new Exception("The first argument must be a instance of Drawable interface");
        }
        if(_p != null){
            Vector2 v = getPixelCoordsFromCellCoords(_p.getPosition());
            _g.drawImage(_p.getGraphicalView(), v.x, convertCoordsFromReal(v.y), this);
        }else{
            throw new NullPointerException("The piece passed is a null instance!");
        }
    }

    /**
     * @param p - the piece from which to take the selected cells
     */
    private void updateSelectedCells(Pezzo p){
        selectedCells = RouteChecker.getPossibleMoves(p, board);
        repaint();
    }
    private boolean isASelectedCell(Vector2 cell){
        if(selectedCells != null){
            for(Vector2 _cell : selectedCells){
                if(_cell.equals(cell)){
                    return true;
                }
            }
        }
        return false;
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
    private class GraphicalBoardListener implements MouseListener, MouseMotionListener, PieceListener {
        private boolean eatingMode = false;
        private Pezzo piece = null;

        /**catch the mouse position on the click time, checks if is there a piece and in case there is, get the possible moves*/
        private void startEatingMode(Vector2 clickCoords){
            eatingMode = true;
            //now check if is there a piece and if is there get the possible move and repaint all
            if(board.isThereAPiece(clickCoords)){
                updateSelectedCells(board.getPieceByPosition(clickCoords));
                piece = board.getPieceByPosition(clickCoords);
            }
        }

        /**eat a piece if is there one or simply move the piece*/
        private void eat(Vector2 clickCoords){
            if(isASelectedCell(clickCoords)) {
                //eat and then move
                Pezzo p = board.getPieceByPosition(clickCoords);
                if (p != null && !piece.getColor().equals(p.getColor())) {//if at the destination is there a piece:
                    board.eatPiece(p);
                }
                piece.move(clickCoords);
                //stop eating mode
                eatingMode = false;
                updateSelectedCells(null);//all cell now are unselected
            }
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
                eat(clickCoords);
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

        /**
         * do some action when a Pedone moves
         * @param p the Pedone interested
         */
        @Override
        public void onPedoneMove(Pedone p) {
            if(piece.getPosition().y == 8 || piece.getPosition().y == 1){
                //todo: inserire qui il dialog per scegliere il morph
            }
        }
    }
}
