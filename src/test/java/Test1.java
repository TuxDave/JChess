import com.tuxdave.JChess.UI.Launcher;

import javax.swing.*;

public class Test1 {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.add(new Launcher());
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setVisible(true);
    }
}