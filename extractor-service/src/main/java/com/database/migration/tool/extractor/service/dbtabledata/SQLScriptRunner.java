package com.database.migration.tool.extractor.service.dbtabledata;

import com.database.migration.tool.extractor.service.ScriptRunner;
import com.database.migration.tool.extractor.service.dbconnection.MysqlConnect;
import com.database.migration.tool.extractor.service.scripts.DBMessage;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLScriptRunner extends Thread {

    private String sqlFilePath = "dump.sql";
    private MysqlConnect mySqlConnect;
    private DBMessage dbMsg;
    private Connection con;
    private JPanel rootPanel;
    
    public SQLScriptRunner(JPanel rootPanel) {
        mySqlConnect = new MysqlConnect();
        dbMsg = mySqlConnect.getCurrentMysqlConnection();
        if (dbMsg.getCODE() != 0) {
            JOptionPane.showMessageDialog(null, dbMsg.getMSG());
            return;
        }
        con = dbMsg.getDbCon();
        this.rootPanel = rootPanel;
    }

    public void runSQLScript() {
        try {
            //Initializing object for ScriptRunner
            ScriptRunner sr = new ScriptRunner(con, false, rootPanel);
            //Giving the input sql file to the Reader
            Reader reader = new BufferedReader(new FileReader(sqlFilePath));
            //Executing sql script
            sr.runScript(reader);
        } catch (IOException | SQLException e) {
            System.out.println(e.getCause());
        }
    }

    public void run() {
        runSQLScript();
    }
}
