import com.tuxdave.JChess.UI.GPlayerProfile;
import com.tuxdave.JChess.UI.Launcher;

import javax.swing.*;

public class Test1 {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.add(new GPlayerProfile("TuxDave","profile1.png"));
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}