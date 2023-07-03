package com.database.migration.tool.extractor.service;

import com.database.migration.tool.core.dto.ColumnDataType;

import java.util.HashMap;
import java.util.Map;

public class DataTypeMapperService {

    private final Map<String, ColumnDataType> ACCESS_TO_JAVA_DATA_TYPE_MAP;

    public DataTypeMapperService() {
        this.ACCESS_TO_JAVA_DATA_TYPE_MAP = new HashMap<>();
        this.ACCESS_TO_JAVA_DATA_TYPE_MAP.put("VARCHAR", ColumnDataType.STRING);
        this.ACCESS_TO_JAVA_DATA_TYPE_MAP.put("SMALLINT", ColumnDataType.INTEGER);
        this.ACCESS_TO_JAVA_DATA_TYPE_MAP.put("DOUBLE", ColumnDataType.DOUBLE);
        this.ACCESS_TO_JAVA_DATA_TYPE_MAP.put("BOOLEAN", ColumnDataType.BOOLEAN);
    }

    public ColumnDataType mapMsAccessToJavaDataType(String msAccessColumnDataType) {
        return this.ACCESS_TO_JAVA_DATA_TYPE_MAP.getOrDefault(msAccessColumnDataType, ColumnDataType.STRING);
    }

}
