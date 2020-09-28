package com.tuxdave.JChess.UI;

import com.tuxdave.JComponents.JPlaceHolderTextField;

import javax.swing.*;
import java.awt.*;

public class GPlayer extends JPanel {
    private JPlaceHolderTextField nickNameTextField;

    private final int WIDTH = 200;
    private final int HEIGHT = 200;

    {//setup static properties
        //aspect
        setBorder(BorderFactory.createLineBorder(Color.blue));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        //implicit
        setLayout(null);
    }

    public GPlayer() {
        super();
        //create components
        nickNameTextField = new JPlaceHolderTextField();
        nickNameTextField.setBounds(20,HEIGHT-40,WIDTH-40,20);
        nickNameTextField.setPlaceHolder("NickName...");
        nickNameTextField.setFont(new Font("ubuntu", Font.BOLD, 16));
        //add components
        add(nickNameTextField);
        //todo: finire di creare la UI e poi nell'altro progetto creare il fotoChanger
    }

    private Image profileImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Avatars/profile0.png"));
    private final int PROFILE_BODY = 96;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //draw the profile image, todo: add the change image feature
        g.drawRect(52, 20, PROFILE_BODY, PROFILE_BODY);
        g.drawImage(profileImage, 52, 20, this);
    }
}
