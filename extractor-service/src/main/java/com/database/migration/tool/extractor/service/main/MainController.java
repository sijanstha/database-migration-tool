package com.database.migration.tool.extractor.service.main;

import com.database.migration.tool.extractor.service.ConfigModule;
import com.database.migration.tool.extractor.service.ImageResolver;
import com.database.migration.tool.extractor.service.gui.ErrorTableListPanel;
import com.database.migration.tool.extractor.service.gui.WelcomePanel;
import com.database.migration.tool.extractor.service.scripts.CMNDBConfig;
import io.activej.inject.Injector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainController {

    public boolean checkPrevProcess() {
        boolean flag = false;
        File file = new File("error_log.txt");
        if (file.exists()) {
            if (file.length() != 0) {
                if (new File(System.getProperty("user.home") + "/config.properties").exists()) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    public void loadConfig() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(System.getProperty("user.home") +"/config.properties");
            prop.load(input);
            CMNDBConfig.setMYSQL_DB_NAME(prop.getProperty("mysqlDbName"));
            CMNDBConfig.setMYSQL_USER_NAME(prop.getProperty("mysqlDbUser"));
            CMNDBConfig.setMYSQL_USER_PWD(prop.getProperty("mysqlDbPwd"));
            CMNDBConfig.setMYSQL_IP(prop.getProperty("mysqlIp"));
            CMNDBConfig.setMYSQL_PORT(prop.getProperty("mysqlPort"));
            CMNDBConfig.setMSACCESS_PATH(prop.getProperty("msaccessPath"));
            CMNDBConfig.setMSACCESS_PWD(prop.getProperty("msaccessPwd"));
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        }
    }
    

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                int flag;
                MainController mainControl = new MainController();
                Injector injector = Injector.of(new ConfigModule());
                JPanel contentPane = injector.getInstance(JPanel.class);
                WelcomePanel welcomePanel = injector.getInstance(WelcomePanel.class);
                ErrorTableListPanel errorTableListPanel = injector.getInstance(ErrorTableListPanel.class);
                ImageResolver resolver = injector.getInstance(ImageResolver.class);

                if (mainControl.checkPrevProcess()) {
                    String[] options = new String[2];
                    options[0] = new String("Proceed");
                    options[1] = new String("No");
                    String message = "Proceed to correct error where you left?";
                    int input = JOptionPane.showOptionDialog(null, message, "Confirm Dialog", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);
                    if (input != 0) {
                        flag = 0;
                    } else {
                        mainControl.loadConfig();
                        flag = 1;
                    }
                } else {
                    flag = 0;
                }

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setBounds(350, 150, 550, 440);
                frame.setResizable(false);
                frame.setTitle("Database Migration Wizard v1.0");
                frame.add(contentPane);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        //TODO CHECK ALL FILES AND DELETE
                        System.out.println("Closed");
                        e.getWindow().dispose();
                    }
                });
                if (flag == 0) {
                    welcomePanel.setBounds(0, 61, 550, 368);
                    contentPane.add(welcomePanel);
                } else {
                    errorTableListPanel.setBounds(0, 61, 550, 368);
                    contentPane.add(errorTableListPanel);
                }
                ImageIcon icon = new ImageIcon("res/logo.jpg");
                frame.setIconImage(icon.getImage());
                frame.setVisible(true);
            }
        });
    }
}
