package com.tuxdave.JChess.UI;

import javax.swing.*;
import java.awt.*;

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

        final short CELL_SIZE = 64;
        for(int i = 0; i < 8; i++){
            g.drawLine(0, (i+1)*CELL_SIZE, getPreferredSize().width, (i+1)*CELL_SIZE);
            g.drawLine((i+1)*CELL_SIZE,0,(i+1)*CELL_SIZE, getPreferredSize().width);
        }
    }
}
