import com.tuxdave.JChess.UI.GPlayerProfile;
import com.tuxdave.JChess.UI.JChess;

import javax.swing.*;

public class Test1 {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setResizable(false);
        f.setContentPane(new JChess(new GPlayerProfile("g1", "profile0.png"), new GPlayerProfile("g2", "profile1.png")));
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        //JOptionPane.showMessageDialog(null, "ErrorMsg", "Failure", JOptionPane.ERROR_MESSAGE);
    }
}