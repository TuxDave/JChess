import com.tuxdave.JChess.UI.GraphicalBoard;
import com.tuxdave.JChess.core.GameBoard;
import com.tuxdave.JChess.core.pieces.Pezzo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class GraphicalBoardTest extends GraphicalBoard implements MouseListener {
    public void setBoard(GameBoard b){
        board = b;
    }

    public Pezzo p = null;

    public GraphicalBoardTest() {
        super();
    }
    {//static properties
        setBorder(BorderFactory.createLineBorder(Color.blue));
        setPreferredSize(new Dimension(512,512));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {//test clone a piece
        if(mouseEvent.getButton() == 2){
            System.out.println("C");
            p = board.getPlayer(0).getPieces()[0].clone();
        }else if(mouseEvent.getButton() == 3){
            System.out.println("V");
            board.getPlayer(0).re_AssignPiece(p, 0);
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
