import com.tuxdave.JChess.UI.GraphicalBoard;
import com.tuxdave.JChess.core.GameBoard;

import javax.swing.*;
import java.awt.*;

class GraphicalBoardTest extends GraphicalBoard {
    public void setBoard(GameBoard b){
        board = b;
    }

    {//static properties
        setBorder(BorderFactory.createLineBorder(Color.blue));
        setPreferredSize(new Dimension(512,512));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
    }
}
