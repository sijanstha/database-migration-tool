package com.database.migration.tool.extractor.service.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ThankYouPanel extends JPanel implements ActionListener {

    private JButton btnCancel;
    private JPanel rootPanel;
    private JTextArea txtThankYouMsg;
    private JLabel lblImg;

    public ThankYouPanel(JPanel rootPanel) {
        this.rootPanel = rootPanel;
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        txtThankYouMsg = new JTextArea("Congratulation!\nYou've successfully migrated\nyour Ms-Access database.\n"
                + "\n Thank you for using our tool \n for migration!");
        txtThankYouMsg.setEditable(false);
        txtThankYouMsg.setBounds(50, 80, 250, 250);
        txtThankYouMsg.setFont(new Font("Cambria Math", Font.PLAIN, 17));
        add(txtThankYouMsg);

        lblImg = new JLabel();
        lblImg.setIcon(new ImageIcon("res/thankyou.png"));
        lblImg.setBounds(300, 30, 200, 236);
        add(lblImg);

        btnCancel = new JButton("EXIT");
        btnCancel.setBounds(418, 284, 89, 23);
        add(btnCancel);

        btnCancel.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == btnCancel) {
            System.exit(0);
        }
    }
}
