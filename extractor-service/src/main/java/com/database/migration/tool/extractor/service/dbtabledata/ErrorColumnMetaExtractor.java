
package com.database.migration.tool.extractor.service.dbtabledata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ErrorColumnMetaExtractor {
    public ArrayList<String> getTableName(String fileName){
        
        ArrayList<String> tableNames = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("%");
                    tableNames.add(tokens[0]);
            }
            reader.close();
        } catch (Exception ioe) {
            ioe.getMessage();
        }
        return tableNames;
    }
    
    public ArrayList<String> getColumnMetaDataArrayList(String fileName, String tableName) {
        ArrayList<String> columnNames = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("%");
                for (int i = 1; i < tokens.length; i++) {

                    if (tokens[0].equalsIgnoreCase(tableName)) {

                        columnNames.add(tokens[i]);
                    }
                }
            }
            reader.close();
        } catch (Exception ioe) {
            ioe.getMessage();
        }
        return columnNames;
    }
}
