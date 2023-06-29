package com.database.migration.tool.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ColumnStructureMeta {
    private String columnName;
    private String dataType;
    private int size;
    private boolean isAutoIncrement;
}
