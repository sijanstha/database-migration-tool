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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TableDataExtractor {
    private ArrayList<String> tableNameList;
    private Connection msAccessDbConnection;
    private DataTypeMapperService dataTypeMapperService;
    private Gson gson;
    private ExecutorService executor;
    private List<Future<?>> futures;
    private JTextArea jTextArea;

    public TableDataExtractor(ArrayList<String> tableList, JTextArea textArea) {
        this.tableNameList = tableList;
        this.dataTypeMapperService = new DataTypeMapperService();
        this.msAccessDbConnection = MSAccessConnect.getMsAccessDbConnection();
        this.gson = new Gson();
        this.executor = Executors.newFixedThreadPool(5);
        this.futures = new ArrayList<>();
        this.jTextArea = textArea;
    }

    public List<Future<?>> databaseMigrator() throws ExecutionException, InterruptedException {
        this.extractAndProcessDbTable();
        this.extractAndProcessTableRecords();
        executor.shutdown();
        return futures;
    }

    private void extractAndProcessDbTable() {
        for (int i = 0; i < tableNameList.size(); i++) {
            int finalI = i;
            Future<?> future = executor.submit(() -> resolveTableStructureMetaData(tableNameList.get(finalI)));
            futures.add(future);
        }
    }

    private void extractAndProcessTableRecords() {
        for (int tableIdx = 0; tableIdx < tableNameList.size(); tableIdx++) {
            int finalTableIdx = tableIdx;
            Future<?> future = executor.submit(() -> resolveTableRecords(finalTableIdx));
            futures.add(future);
        }
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

    private void resolveTableRecords(int tableIdx) {
        try {
            msAccessDbConnection.setAutoCommit(false);
            String selectSql = "Select * FROM " + tableNameList.get(tableIdx);
            Statement rst = msAccessDbConnection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            rst.setFetchSize(500);
            ResultSet rs = rst.executeQuery(selectSql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int totalColumnsCount = rsmd.getColumnCount();

            while (rs.next()) {
                TableContentMeta tableContentMeta = new TableContentMeta();
                Set<String> columns = new HashSet<>();
                Map<String, ColumnValueMeta> columnValueMetaMap = new HashMap<>();
                for (int columnIdx = 1; columnIdx <= totalColumnsCount; columnIdx++) {
                    String columnName = this.resolveColumnName(rs, columnIdx);
                    ColumnValueMeta columnValueMeta = resolveColumnValueMeta(rs, rsmd.getColumnTypeName(columnIdx), columnIdx);
                    columns.add(columnName);
                    columnValueMetaMap.put(columnName, columnValueMeta);
                }
                tableContentMeta.setData(columnValueMetaMap);
                tableContentMeta.setConnectionId(CMNDBConfig.getCONNECTION_ID());
                tableContentMeta.setStatement(SqlStatementEnum.INSERT);
                tableContentMeta.setType(SqlCommandEnum.DML);
                tableContentMeta.setTableName(tableNameList.get(tableIdx));
                tableContentMeta.setColumns(columns);
                System.out.println(gson.toJson(tableContentMeta));
            }
            rst.close();
            msAccessDbConnection.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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
        System.out.println(Thread.currentThread().getName() + " " + gson.toJson(tableStructureMeta));
        jTextArea.append("\n" + "Processing for table: " + tableName);
        columnsMetaDataResultSet.close();
        return tableStructureMeta;
    }

    private String resolveColumnName(ResultSet resultSet, int columnIndex) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        return resultSetMetaData.getColumnName(columnIndex);
    }
}
