package com.database.migration.tool.extractor.service.gui;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JRootFrame extends JFrame {

    private JPanel contentPane;
    private WelcomePanel welcomePanel;
    private ErrorTableListPanel errorPanel;

    public JRootFrame(int flag) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 150, 550, 440);
        contentPane = new JPanel();
        add(contentPane);
        contentPane.setLayout(null);
        JHeaderPanel headerPanel = new JHeaderPanel();
        headerPanel.setBounds(0, 0, 550, 72);
        contentPane.add(headerPanel);

        if (flag == 0) {
            welcomePanel = new WelcomePanel(contentPane);
            welcomePanel.setBounds(0, 61, 550, 368);
            contentPane.add(welcomePanel);
        } else {
            errorPanel = new ErrorTableListPanel(contentPane);
            errorPanel.setBounds(0, 61, 550, 368);
            contentPane.add(errorPanel);
        }

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO CHECK ALL FILES AND DELETE
                System.out.println("Closed");
                e.getWindow().dispose();
            }
        });
    }
}
