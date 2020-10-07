package com.tuxdave.JChess.UI;

import com.tuxdave.JChess.UI.listener.ReadyListener;
import com.tuxdave.JChess.extras.GImagePicker;
import com.tuxdave.JChess.extras.Vector2;
import com.tuxdave.JComponents.JPlaceHolderTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GPlayerCreator extends JPanel implements MouseListener, MouseMotionListener {
    private JPlaceHolderTextField nickNameTextField;
    private JButton readyButton;

    private static int created = 0;

    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private final ReadyListener listener;
    private boolean readyState;
    private int number;

    {//setup static properties
        //aspect
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        //implicit
        setLayout(null);
    }

    public GPlayerCreator(ReadyListener l) {
        super();
        //listener
        addMouseListener(this);
        addMouseMotionListener(this);
        listener = l;
        readyState = false;
        number = created++;

        //construct components
        nickNameTextField = new JPlaceHolderTextField();
        nickNameTextField.setPlaceHolder("Username...");
        nickNameTextField.setFont(new Font("Ubuntu", Font.BOLD, 16));
        nickNameTextField.setBounds(20, HEIGHT-60, WIDTH-40, 20);

        readyButton = new JButton(new ImageIcon(getClass().getResource("/Resources/Icons/spunta.png")));
        readyButton.setText("Ready!");
        readyButton.setFont(new Font("Ubuntu", Font.BOLD, 16));
        readyButton.setBounds(20, HEIGHT-35, WIDTH-40, 20);
        readyButton.setEnabled(false);

        //then add components
        add(nickNameTextField);
        add(readyButton);
        //add the function to readyButton
        readyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                readyState = !readyState;
                if(readyState){
                    readyButton.setText("Unready!");
                    nickNameTextField.setEnabled(false);
                }else{
                    readyButton.setText("Ready!");
                    nickNameTextField.setEnabled(true);
                }
                listener.onReady(number, nickNameTextField.getText(), profileImgName, readyState);
            }
        });
        nickNameTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent){/*ingored*/}

            @Override
            public void keyPressed(KeyEvent keyEvent){/*ingored*/}

            @Override
            public void keyReleased(KeyEvent keyEvent){
                if(nickNameTextField.getText().equals("") || nickNameTextField.getText().contains(" ")){
                    readyButton.setEnabled(false);
                }else{
                    readyButton.setEnabled(true);
                }
            }
        });
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

    private String profileImgName = "profile0.png";
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Vector2 coords = new Vector2(mouseEvent.getX(), mouseEvent.getY());
        if(coords.isBetweenLimits((HEIGHT-PROFILE_BODY)/2, 20, (HEIGHT-PROFILE_BODY)/2+PROFILE_BODY, 20+PROFILE_BODY)){
            GImagePicker dialog1 = new GImagePicker("/Resources/Icons/Avatars", "profile", "png");
            dialog1.setVisible(true);
            dialog1.pack();
            if(dialog1.getImgName() != null){
                profileImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/Avatars/" + dialog1.getImgName()));
                profileImgName = dialog1.getImgName();
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

    private boolean allreadyPainted = false;

    /**
     * paints a canvas along the profile image to say to the user that he can change it.
     * @param mouseEvent mouse position and other attributes
     */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Vector2 coords = new Vector2(mouseEvent.getX(), mouseEvent.getY());
        if(coords.isBetweenLimits((HEIGHT-PROFILE_BODY)/2, 20, (HEIGHT-PROFILE_BODY)/2+PROFILE_BODY, 20+PROFILE_BODY)){
            getGraphics().drawImage(canvas, (WIDTH-CANVAS_BODY)/2, 8, this);
            allreadyPainted = true;
        }else if(allreadyPainted){
            repaint();
        }
    }
}
