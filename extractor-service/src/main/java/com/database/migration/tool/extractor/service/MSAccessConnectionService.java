package com.database.migration.tool.extractor.service;

import com.database.migration.tool.extractor.model.CMNDBConfig;
import com.database.migration.tool.extractor.model.DBMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MSAccessConnectionService {
    private static DBMessage dbMessage;

    public static DBMessage getCurrentMsAccessConnection() {
        if (dbMessage == null || dbMessage.getCODE() != 0) {
            dbMessage = new DBMessage();
            try {
                Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                Connection connection = DriverManager.getConnection(String.format("jdbc:ucanaccess://%s;password=%s;memory=false", CMNDBConfig.getMSACCESS_PATH(), CMNDBConfig.getMSACCESS_PWD()));
                dbMessage.setCODE(0);
                dbMessage.setMSG("MS Access Connection Established!");
                dbMessage.setDbCon(connection);
            } catch (SQLException ex) {
                dbMessage.setCODE(101);
                dbMessage.setMSG(ex.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MSAccessConnectionService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dbMessage;
    }

    public static Connection getMsAccessDbConnection() {
        DBMessage dbMessage = getCurrentMsAccessConnection();
        if (dbMessage.getCODE() != 0)
            throw new RuntimeException("Cannot connect to ms access db");

        return dbMessage.getDbCon();
    }
}
