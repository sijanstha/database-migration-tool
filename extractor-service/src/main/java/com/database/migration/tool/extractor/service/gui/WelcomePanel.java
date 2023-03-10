package com.database.migration.tool.extractor.service.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomePanel extends JPanel implements ActionListener {

    private JButton btnCancel;
    private JButton btnNext;
    private JPanel rootPanel;

    public WelcomePanel(JPanel rootPanel) {
        setBorder(new LineBorder(new Color(0, 0, 0)));
        this.rootPanel = rootPanel;
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        btnNext = new JButton("Next");
        btnNext.setBounds(290, 284, 89, 23);
        add(btnNext);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(418, 284, 89, 23);
        add(btnCancel);

        btnNext.addActionListener(this);
        btnCancel.addActionListener(this);

        JLabel imgLabel = new JLabel("");
        imgLabel.setIcon(new ImageIcon("res/frontImg.png"));
        imgLabel.setBounds(10, 45, 212, 212);
        add(imgLabel);

        JTextArea txtrWelcome = new JTextArea();
        txtrWelcome.setFont(new Font("Cambria Math", Font.PLAIN, 16));
        txtrWelcome.setEditable(false);
        txtrWelcome.setText("Welcome!\nDatabase Migration Tool aims to \nconvert MS-Access Database to MySQL\n"
                + "preserving its data intregity.\n\n\nPlease Click 'NEXT' to proceed further\n"
                + "to convert your database");
        txtrWelcome.setBounds(232, 63, 308, 168);
        add(txtrWelcome);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == btnNext) {
            rootPanel.remove(1);
            rootPanel.add(new MsAccessPanel(rootPanel), 1);
            rootPanel.repaint();
        } else if (arg0.getSource() == btnCancel) {
            System.exit(0);
        }
    }
}
