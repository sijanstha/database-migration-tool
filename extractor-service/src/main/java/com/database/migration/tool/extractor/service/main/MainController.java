package com.database.migration.tool.extractor.service.main;

import com.database.migration.tool.extractor.service.gui.JRootFrame;
import com.database.migration.tool.extractor.service.scripts.CMNDBConfig;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainController {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            int flag;
            MainController mainControl = new MainController();
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

            JRootFrame frame = new JRootFrame(flag);
            frame.setResizable(false);
            frame.setTitle("Database Migration Wizard v1.0");
            ImageIcon icon = new ImageIcon("res/logo.jpg");
            frame.setIconImage(icon.getImage());
            frame.setVisible(true);
        });
    }

    public boolean checkPrevProcess() {
        File errorFile = new File("error_log.txt");
        return errorFile.exists() && errorFile.length() != 0 && new File(getConfigPropertyRelativeFilePath()).exists();
    }

    public void loadConfig() {
        try(InputStream input = new FileInputStream(getConfigPropertyRelativeFilePath())) {
            Properties prop = new Properties();
            prop.load(input);
            CMNDBConfig.setMYSQL_DB_NAME(prop.getProperty("mysqlDbName"));
            CMNDBConfig.setMYSQL_USER_NAME(prop.getProperty("mysqlDbUser"));
            CMNDBConfig.setMYSQL_USER_PWD(prop.getProperty("mysqlDbPwd"));
            CMNDBConfig.setMYSQL_IP(prop.getProperty("mysqlIp"));
            CMNDBConfig.setMYSQL_PORT(prop.getProperty("mysqlPort"));
            CMNDBConfig.setMSACCESS_PATH(prop.getProperty("msaccessPath"));
            CMNDBConfig.setMSACCESS_PWD(prop.getProperty("msaccessPwd"));
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private String getConfigPropertyRelativeFilePath() {
        return String.format("%s/config.properties", System.getProperty("user.home"));
    }
}
