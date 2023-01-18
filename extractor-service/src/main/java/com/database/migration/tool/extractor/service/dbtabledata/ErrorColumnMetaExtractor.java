package com.database.migration.tool.extractor.service.dbtabledata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ErrorColumnMetaExtractor {
    public List<String> getTableName(String fileName) {

        List<String> tableNames = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
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

    public List<String> getColumnMetaDataArrayList(String fileName, String tableName) {
        List<String> columnNames = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
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
