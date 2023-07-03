package com.database.migration.tool.extractor.gui;

import com.database.migration.tool.extractor.service.MSAccessConnectionService;
import com.database.migration.tool.extractor.model.DBMessage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class JRootFrame extends JFrame {

    private JPanel contentPane;
    private WelcomePanel welcomePanel;
    private DBMessage dbMessage;

    public JRootFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 150, 550, 440);
        contentPane = new JPanel();
        add(contentPane);
        contentPane.setLayout(null);
        JHeaderPanel headerPanel = new JHeaderPanel();
        headerPanel.setBounds(0, 0, 550, 72);
        contentPane.add(headerPanel);
        this.dbMessage = MSAccessConnectionService.getCurrentMsAccessConnection();

        welcomePanel = new WelcomePanel(contentPane);
        welcomePanel.setBounds(0, 61, 550, 368);
        contentPane.add(welcomePanel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //TODO CHECK ALL FILES AND DELETE
                System.out.println("Closed");
                if (dbMessage.getCODE() == 0) {
                    try {
                        dbMessage.getDbCon().close();
                        System.out.println("ms access db connection closed");
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                e.getWindow().dispose();
            }
        });
    }
}
