package com.database.migration.tool.extractor.main;

import com.database.migration.tool.extractor.gui.JRootFrame;
import com.database.migration.tool.extractor.scripts.CMNDBConfig;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MainController {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JRootFrame frame = new JRootFrame();
            frame.setResizable(false);
            frame.setTitle("Database Migration Wizard v1.0");
            ImageIcon icon = new ImageIcon("res/logo.jpg");
            frame.setIconImage(icon.getImage());
            frame.setVisible(true);
        });
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
            CMNDBConfig.setMSACCESS_PWD(prop.getProperty("connectionId"));
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    private String getConfigPropertyRelativeFilePath() {
        return String.format("%s/config.properties", System.getProperty("user.home"));
    }
}
