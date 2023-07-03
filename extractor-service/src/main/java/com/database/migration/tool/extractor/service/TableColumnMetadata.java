package com.database.migration.tool.extractor.service;

import com.database.migration.tool.extractor.dbconnection.MSAccessConnect;
import com.database.migration.tool.extractor.scripts.AppMessage;
import com.database.migration.tool.extractor.scripts.DBMessage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TableColumnMetadata {
    private final String[] table = {"TABLE"};
    private final ArrayList<String> tableArray = new ArrayList<>();

    public AppMessage getTableMetadata() {
        AppMessage appMsg = new AppMessage();
        DBMessage dbMsg = MSAccessConnect.getCurrentMsAccessConnection();
        if (dbMsg.getCODE() != 0) {
            appMsg.setCODE(dbMsg.getCODE());
            appMsg.setMSG(dbMsg.getMSG());
            return appMsg;
        }
        Connection con = dbMsg.getDbCon();
        try {
            DatabaseMetaData meta = con.getMetaData();
            ResultSet res = meta.getTables(null, null, "%", table);

            while (res.next()) {
                tableArray.add(res.getString(3));
            }
            res.close();

            File file = new File("table_meta_data.txt");
            if (file.exists()) {
                file.delete();
            }
            BufferedWriter fw = new BufferedWriter(new FileWriter(file));

            for (int i = 0; i < tableArray.size(); i++) {
                //  FileWriter fw = new FileWriter(file, true); // to append ",true"
                String table_name = tableArray.get(i);
                fw.write(table_name);
                res = meta.getColumns(null, null, table_name, null);
                while (res.next()) {
                    fw.append("%" + res.getString("COLUMN_NAME"));
                }
                fw.append("\n");
            }
            fw.close();
            appMsg.setCODE(0);
            appMsg.setMSG("Tables Metadata Written Successfully");
        } catch (IOException e) {
            appMsg.setCODE(102);
            appMsg.setMSG(e.getMessage());
        } catch (SQLException ex) {
            appMsg.setCODE(101);
            appMsg.setMSG(ex.getMessage());
        }
        return appMsg;
    }
}
