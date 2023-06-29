/*
 This Class Fetchs the table details and dumps the table contents into .sql file
 Created by: Sijan Shrestha
 Created On: 23 july, 2017
 Modified On: 24 july, 2017
 */
package com.database.migration.tool.extractor.service.dbtabledata;

import com.database.migration.tool.core.dto.*;
import com.database.migration.tool.extractor.service.dbconnection.MSAccessConnect;
import com.database.migration.tool.extractor.service.scripts.CMNDBConfig;
import com.database.migration.tool.extractor.service.service.DataTypeMapperService;
import com.google.gson.Gson;

import javax.swing.*;
import java.sql.*;
import java.util.*;

public class TableDataExtractor extends Thread {
    private ArrayList<String> tableNameList;
    private Connection msAccessDbConnection;
    private JPanel rootPanel;
    private DataTypeMapperService dataTypeMapperService;
    private Gson gson;

    public TableDataExtractor(ArrayList<String> tableList, JPanel rootPanel) {
        this.tableNameList = tableList;
        this.rootPanel = rootPanel;
        this.dataTypeMapperService = new DataTypeMapperService();
        this.msAccessDbConnection = MSAccessConnect.getMsAccessDbConnection();
        this.gson = new Gson();
    }

    private void extractAndProcessDbTable() {
        for (int i = 0; i < tableNameList.size(); i++) {
            try {
                resolveTableStructureMetaData(tableNameList.get(i));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                this.tableNameList.remove(i); // because we don't want to process the table that has not been created in db because of exception
            }
        }
    }

    private void extractAndProcessTableRecords() {
        try {
            for (int tableIdx = 0; tableIdx < tableNameList.size(); tableIdx++) {
                msAccessDbConnection.setAutoCommit(false);
                String selectSql = "Select * FROM " + tableNameList.get(tableIdx);
                Statement rst = msAccessDbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                rst.setFetchSize(500);
                ResultSet rs = rst.executeQuery(selectSql);
                ResultSetMetaData rsmd = rs.getMetaData();
                int totalColumnsCount = rsmd.getColumnCount();

                TableContentMeta tableContentMeta = new TableContentMeta();
                Set<String> columns = new HashSet<>();
                List<Map<String, ColumnValueMeta>> columnValues = new ArrayList<>();
                while (rs.next()) {
                    Map<String, ColumnValueMeta> columnValueMetaMap = new HashMap<>();
                    for (int columnIdx = 1; columnIdx <= totalColumnsCount; columnIdx++) {
                        String columnName = this.resolveColumnName(rs, columnIdx);
                        ColumnValueMeta columnValueMeta = resolveColumnValueMeta(rs, rsmd.getColumnTypeName(columnIdx), columnIdx);
                        columns.add(columnName);
                        columnValueMetaMap.put(columnName, columnValueMeta);
                    }
                    columnValues.add(columnValueMetaMap);
                }
                tableContentMeta.setConnectionId(CMNDBConfig.getCONNECTION_ID());
                tableContentMeta.setStatement(SqlStatementEnum.INSERT);
                tableContentMeta.setType(SqlCommandEnum.DML);
                tableContentMeta.setTableName(tableNameList.get(tableIdx));
                tableContentMeta.setColumns(columns);
                tableContentMeta.setData(columnValues);
                System.out.println(gson.toJson(tableContentMeta));
                rst.close();
            }
            msAccessDbConnection.commit();
            msAccessDbConnection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean databaseMigrator() {
        this.extractAndProcessDbTable();
        this.extractAndProcessTableRecords();
        return false;
    }

    private ColumnValueMeta resolveColumnValueMeta(ResultSet resultSet, String columnType, int columnIndex) throws SQLException {
        ColumnValueMeta columnValueMeta = new ColumnValueMeta();
        if (resultSet.getString(columnIndex) == null || resultSet.getString(columnIndex) == "") {
            columnValueMeta.setValue(null);
        } else if (resultSet.getString(columnIndex).contains("'")) {
            int index = resultSet.getString(columnIndex).indexOf("'");
            columnValueMeta.setValue(resultSet.getString(columnIndex).substring(0, index) + "\\" + resultSet.getString(columnIndex).substring(index));
        } else {
            columnValueMeta.setValue(resultSet.getString(columnIndex));
        }
        columnValueMeta.setDataType(dataTypeMapperService.mapMsAccessToJavaDataType(columnType));
        return columnValueMeta;
    }

    private TableStructureMeta resolveTableStructureMetaData(String tableName) throws SQLException {
        TableStructureMeta tableStructureMeta = new TableStructureMeta();
        List<ColumnStructureMeta> columnStructureMetas = new ArrayList<>();

        DatabaseMetaData meta = msAccessDbConnection.getMetaData();
        ResultSet columnsMetaDataResultSet = meta.getColumns(null, null, tableName, null);
        ResultSet primaryKeys = meta.getPrimaryKeys(null, null, tableName);
        while (columnsMetaDataResultSet.next()) {
            ColumnStructureMeta columnMetaData = new ColumnStructureMeta();
            columnMetaData.setColumnName(columnsMetaDataResultSet.getString("COLUMN_NAME"));
            if (columnsMetaDataResultSet.getString("TYPE_NAME").equalsIgnoreCase("TIMESTAMP")) {
                columnMetaData.setDataType("DATETIME");
            } else {
                columnMetaData.setDataType(columnsMetaDataResultSet.getString("TYPE_NAME"));
            }
            columnMetaData.setSize(columnsMetaDataResultSet.getInt("COLUMN_SIZE"));
            columnMetaData.setAutoIncrement(columnsMetaDataResultSet.getString("IS_AUTOINCREMENT").equals("YES"));
            columnStructureMetas.add(columnMetaData);
        }

        tableStructureMeta.setConnectionId(CMNDBConfig.getCONNECTION_ID());
        tableStructureMeta.setStatement(SqlStatementEnum.CREATE);
        tableStructureMeta.setType(SqlCommandEnum.DDL);
        tableStructureMeta.setPrimaryKeyColumn(primaryKeys.next() ? primaryKeys.getString("COLUMN_NAME") : null);
        tableStructureMeta.setTableName(tableName);
        tableStructureMeta.setColumnMetaData(columnStructureMetas);
        // TODO: send this tableStructureMeta to Kafka topic
        System.out.println(gson.toJson(tableStructureMeta));
        columnsMetaDataResultSet.close();
        return tableStructureMeta;
    }

    private String resolveColumnName(ResultSet resultSet, int columnIndex) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        return resultSetMetaData.getColumnName(columnIndex);
    }

    @Override
    public void run() {
        boolean b = databaseMigrator();
        if (b) {
            new SQLScriptRunner(rootPanel).start();
        }
    }
}
