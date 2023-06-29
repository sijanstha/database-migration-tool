package com.database.migration.tool.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ColumnValueMeta {
    private ColumnDataType dataType;
    private String value;
}


