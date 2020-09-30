package com.tuxdave.JChess.UI;

import com.tuxdave.JComponents.JPlaceHolderTextField;

import javax.swing.*;
import java.awt.*;

public class GPlayer extends JPanel {
    JPlaceHolderTextField nickNameTextField;
    JButton readyButton;

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

        //todo: finire di creare la UI, login and register
        //todo:nell'altro progetto creare il fotoChanger
    }

    private Image profileImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/Avatars/profile0.png"));
    private final int PROFILE_BODY = 96;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //draw the profile image, todo: add the change image feature
        g.drawRect(52, 20, PROFILE_BODY, PROFILE_BODY);
        g.drawImage(profileImage, 52, 20, this);
    }
}
