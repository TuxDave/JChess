package com.tuxdave.JChess.extras;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * make you able to pick an image from a list
 */
public class GImagePicker extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel choosePane;
    private JLabel l1;

    private final ArrayList<Image> images = new ArrayList<Image>();
    Image selectedImage = null;

    private final ArrayList allCreated = new ArrayList<JSelectableImage>();
    private int matricola = 0;
    private String imgName = null;

    private class JSelectableImage extends JLabel implements MouseListener {
        private boolean selected;
        private int id;
        private String name;

        /**
         * construct the JLabel
         *
         * @param image
         * @param nameAndNumberAndExtension example profile4.png
         */
        public JSelectableImage(Icon image, String nameAndNumberAndExtension) {
            super(image);

            id = matricola++;
            setOpaque(true);
            selected = false;
            name = nameAndNumberAndExtension;
            addMouseListener(this);
            allCreated.add(this);
        }

        @Override
        public void mouseClicked(MouseEvent mouseEvent) {
            buttonOK.setEnabled(true);
            for (Object i : allCreated) {
                if (((JSelectableImage) i).selected && ((JSelectableImage) i).id != id) {
                    ((JSelectableImage) i).selected = false;
                    ((JSelectableImage) i).mouseExited(mouseEvent);
                }
            }
            setBackground(Color.BLUE);
            selected = true;
            imgName = name;
        }

        @Override
        public void mousePressed(MouseEvent mouseEvent) {/*ignored*/}

        @Override
        public void mouseReleased(MouseEvent mouseEvent) {/*ignored*/}

        @Override
        public void mouseEntered(MouseEvent mouseEvent) {
            if (!selected)
                setBackground(Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent mouseEvent) {
            if (!selected)
                setBackground(choosePane.getBackground());
        }
    }

    /**
     * construct the object, setting the path and the generic image name al quale il following a number from 0 to x
     *
     * @param _path        the path containing all images
     * @param _genericName the name of the base image (es: profile, the program will search profile0.png, 1,2,3 ecc
     * @param _extension   the extension of the model of image
     */
    public GImagePicker(String _path, String _genericName, String _extension) {
        $$$setupUI$$$();
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        //construct algorithm

        if (!_path.endsWith("/")) {
            _path += "/";
        }
        int c = 0;
        Image img;
        do {
            try {
                img = Toolkit.getDefaultToolkit().getImage(getClass().getResource(_path + _genericName + c++ + "." + _extension));
                images.add(img);
            } catch (NullPointerException e) {
                if (c == 1) {
                    throw new IllegalArgumentException("file not found at specified path! (cause maybe the path, name or extension)");
                } else {
                    break;
                }
            }
        } while (true);

        choosePane.setLayout(new GridLayout((int) Math.sqrt(images.size()), (int) Math.sqrt(images.size())));
        int temp = 0;
        for (Image image : images) {
            Image img1 = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
            choosePane.add(new JSelectableImage(new ImageIcon(img1), _genericName + temp++ + "." + _extension));
        }

        Dimension d = new Dimension(96 * (int) Math.sqrt(temp), 96 * (int) Math.sqrt(temp - 1) + 50);
        if (d.width < 300)
            d.width = 300;
        if (d.height < 150)
            d.height = 150;
        setMinimumSize(d);
        setResizable(false);
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        imgName = null;
        dispose();
    }

    //==========================================================================

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPane = new JPanel();
        contentPane.setLayout(new GridLayoutManager(3, 1, new Insets(10, 10, 10, 10), -1, -1));
        contentPane.add(choosePane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        contentPane.add(panel1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
        panel1.add(panel2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonOK = new JButton();
        buttonOK.setEnabled(false);
        buttonOK.setText("OK");
        panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonCancel = new JButton();
        buttonCancel.setText("Cancel");
        panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        l1 = new JLabel();
        Font l1Font = this.$$$getFont$$$("Ubuntu", Font.BOLD, 15, l1.getFont());
        if (l1Font != null) l1.setFont(l1Font);
        l1.setText("Select the new profile image:");
        contentPane.add(l1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPane;
    }

    private void createUIComponents() {
        choosePane = new JPanel();
        choosePane.setLayout(new GridLayout());
    }

    public String getImgName() {
        if (imgName != null) {
            return imgName;
        } else {
            return null;
        }
    }
}
