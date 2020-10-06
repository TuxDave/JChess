package com.tuxdave.JChess.UI;

import com.tuxdave.JChess.core.chess.listener.GameListener;
import com.tuxdave.JChess.extras.GImagePicker;
import com.tuxdave.JChess.extras.Vector2;
import com.tuxdave.JComponents.JPlaceHolderTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

public class GPlayer extends JPanel implements MouseListener, MouseMotionListener {
    JPlaceHolderTextField nickNameTextField;
    JButton readyButton;

    private final int WIDTH = 200;
    private final int HEIGHT = 200;

    {//setup static properties
        //aspect
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        //implicit
        setLayout(null);
    }

    public GPlayer() {
        super();
        //listener
        addMouseListener(this);
        addMouseMotionListener(this);

        //construct components
        nickNameTextField = new JPlaceHolderTextField();
        nickNameTextField.setPlaceHolder("Username...");
        nickNameTextField.setFont(new Font("Ubuntu", Font.BOLD, 16));
        nickNameTextField.setBounds(20, HEIGHT-60, WIDTH-40, 20);

        readyButton = new JButton(new ImageIcon(getClass().getResource("/Resources/Icons/spunta.png")));
        readyButton.setText("Ready!");
        readyButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
        readyButton.setBounds(20, HEIGHT-35, WIDTH-40, 20);

        //then add components
        add(nickNameTextField);
        add(readyButton);
    }

    private Image profileImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/Avatars/profile0.png"));
    private final Image canvas = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/contornoProfilo.png"));
    private final short PROFILE_BODY = 96;
    private final short CANVAS_BODY = 120;
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect((HEIGHT-PROFILE_BODY)/2, 20, PROFILE_BODY, PROFILE_BODY);
        g.drawImage(profileImage, (WIDTH-PROFILE_BODY)/2, 20, this);
    }

    //LISTENER MANAGEMENT

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Vector2 coords = new Vector2(mouseEvent.getX(), mouseEvent.getY());
        if(coords.isBetweenLimits((HEIGHT-PROFILE_BODY)/2, 20, (HEIGHT-PROFILE_BODY)/2+PROFILE_BODY, 20+PROFILE_BODY)){
            GImagePicker dialog1 = new GImagePicker("/Resources/Icons/Avatars", "profile", "png");
            dialog1.setVisible(true);
            dialog1.pack();
            if(dialog1.getImgName() != null){
                profileImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/Avatars/" + dialog1.getImgName()));
                repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent){/*ignored*/}
    @Override
    public void mouseReleased(MouseEvent mouseEvent){/*ignored*/}
    @Override
    public void mouseEntered(MouseEvent mouseEvent){/*ignored*/}
    @Override
    public void mouseExited(MouseEvent mouseEvent){/*ignored*/}
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {/*ignored*/}

    private boolean alreadyPainted = false;

    /**
     * paints a canvas along the profile image to say to the user that he can change it.
     * @param mouseEvent mouse position and other attributes
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Vector2 coords = new Vector2(mouseEvent.getX(), mouseEvent.getY());
        if(coords.isBetweenLimits((HEIGHT-PROFILE_BODY)/2, 20, (HEIGHT-PROFILE_BODY)/2+PROFILE_BODY, 20+PROFILE_BODY)){
            getGraphics().drawImage(canvas, (WIDTH-CANVAS_BODY)/2, 8, this);
            alreadyPainted = true;
        }else if(alreadyPainted){
            repaint();
        }
    }
}
