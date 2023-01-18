package com.database.migration.tool.extractor.service.dbconnection;

import com.database.migration.tool.extractor.service.scripts.CMNDBConfig;
import com.database.migration.tool.extractor.service.scripts.DBMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MSAccessConnect {

    private String path = CMNDBConfig.getMSACCESS_PATH() + ";password=";
    private String pwd = CMNDBConfig.getMSACCESS_PWD();
    private Connection connection = null;
    private DBMessage dbMsg;

    public MSAccessConnect() {
        dbMsg = new DBMessage();
    }

    public DBMessage getCurrentMsaccessConnection() {
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            connection = DriverManager.getConnection("jdbc:ucanaccess://" + path + pwd + ";memory=false");
            dbMsg.setCODE(0);
            dbMsg.setMSG("MS Access Connection Established!");
            dbMsg.setDbCon(connection);
        } catch (SQLException ex) {
            dbMsg.setCODE(101);
            System.out.println(ex.toString());
            dbMsg.setMSG(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MSAccessConnect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dbMsg;
    }
}
