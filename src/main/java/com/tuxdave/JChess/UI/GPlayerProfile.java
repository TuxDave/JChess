package com.tuxdave.JChess.UI;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;

public class GPlayerProfile extends JPanel {
    private JPanel panel1;
    private JLabel imageLabel;
    private JLabel nameLabel;
    private JTextArea textArea1;

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(5, 6, new Insets(0, 0, 0, 0), -1, -1));
        imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(getClass().getResource("/Resources/Icons/Avatars/profile0.png")));
        imageLabel.setText("");
        panel1.add(imageLabel, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
        nameLabel = new JLabel();
        nameLabel.setText("Label");
        panel1.add(nameLabel, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Eaten pieces:");
        panel1.add(label1, new GridConstraints(2, 2, 1, 2, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, new GridConstraints(3, 2, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        textArea1 = new JTextArea();
        scrollPane1.setViewportView(textArea1);
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(4, 1, 1, 4, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, 1, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 10), new Dimension(-1, 10), new Dimension(-1, 10), 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(3, 0, 2, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(10, -1), new Dimension(10, -1), new Dimension(10, -1), 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(3, 5, 2, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, new Dimension(10, -1), new Dimension(10, -1), new Dimension(10, -1), 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panel1;
    }

    private final int WIDTH = 200;
    private final int HEIGHT = 512;

    public GPlayerProfile(String _nick, String _imageName) {
        super();
        add(panel1);

        Dimension d = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(d);
        setMinimumSize(d);
        setMaximumSize(d);
        panel1.setPreferredSize(d);
        panel1.setMinimumSize(d);
        panel1.setMaximumSize(d);

        imageLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Resources/Icons/Avatars/" + _imageName))));
        nameLabel.setText(_nick);
    }
}
