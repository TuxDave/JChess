package com.tuxdave.JChess.UI;

import com.tuxdave.JChess.core.Pezzo;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicalPiece extends JPanel {

    private Pezzo piece;
    public Image img;
    private JComponent core = new JComponent() {
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            g2.drawImage(img,0,0, this);
        }
    };

    public GraphicalPiece(Pezzo _p){
        super();
        if(_p != null)
            piece = _p;
        else
            throw new IllegalArgumentException("Provide a valid Pezzo()");
        String color;
        color = piece.getColor().toLowerCase();
        color = color.substring(0, 1).toUpperCase() + color.substring(1);
        img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/pieces/" +
                piece.getType() + "_" + color + ".png"));
    }

    {
        this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        setSize(48, 48);
        setMinimumSize(new Dimension(48, 48));
        setMaximumSize(getMinimumSize());
        setBounds(0, 0, 48, 48);
        setLayout(new BorderLayout());
        add(core);
    }
    public Pezzo getPiece(){
        return piece;
    }
}
