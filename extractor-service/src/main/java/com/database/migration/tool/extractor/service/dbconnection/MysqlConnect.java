package com.database.migration.tool.extractor.service.dbconnection;

import com.database.migration.tool.extractor.service.scripts.CMNDBConfig;
import com.database.migration.tool.extractor.service.scripts.DBMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnect {

    private String mysqlIp = CMNDBConfig.getMYSQL_IP();
    private String mysqlPort = CMNDBConfig.getMYSQL_PORT();
    private String userName = CMNDBConfig.getMYSQL_USER_NAME();
    private String userPwd = CMNDBConfig.getMYSQL_USER_PWD();
    private String dbName = CMNDBConfig.getMYSQL_DB_NAME();
    private Connection connection = null;
    private DBMessage dbMsg;

    public MysqlConnect() {
        dbMsg = new DBMessage();
    }

    public DBMessage getCurrentMysqlConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + mysqlIp + ":" + mysqlPort + "/" + dbName, userName, userPwd);
            dbMsg.setCODE(0);
            dbMsg.setMSG("MYSql Connection Established!");
            dbMsg.setDbCon(connection);
        } catch (SQLException ex) {
            dbMsg.setCODE(101);
            System.out.println(ex.toString());
            dbMsg.setMSG(ex.getMessage());
        } catch (ClassNotFoundException ex) {
            dbMsg.setCODE(103);
            System.out.println(ex.toString());
            dbMsg.setMSG(ex.getMessage());
        }
        return dbMsg;
    }
}
