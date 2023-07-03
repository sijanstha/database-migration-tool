package com.database.migration.tool.extractor.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableColumnMetaExtractor {

    private final String fileName = "table_meta_data.txt";
    private final Map<String, List<String>> columnsCache = new HashMap<>();

    public String getColumnMetaData(String tableName) {
        List<String> columns = resolveColumns(tableName);
        if (columns.isEmpty())
            return null;
        return String.join(",", columns);
    }

    public List<String> resolveColumns(String tableName) {
        if (columnsCache.containsKey(tableName))
            return columnsCache.get(tableName);

        List<String> columnNames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("%");
                if (tokens[0].equalsIgnoreCase(tableName)) {
                    for (int i = 1; i < tokens.length; i++) {
                        columnNames.add(tokens[i]);
                    }
                }
            }
        } catch (Exception ioe) {
            System.out.println("Got exception while parsing column names from file: " + ioe.getMessage());
        }
        columnsCache.put(tableName, columnNames);
        return columnNames;
    }

    public ArrayList<String> getTableName() {

        ArrayList<String> tableNames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split("%");
                tableNames.add(tokens[0]);
            }
        } catch (Exception ioe) {
            ioe.getMessage();
        }
        return tableNames;
    }
}
