package com.database.migration.tool.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TableStructureMeta extends BaseMeta {
    private String primaryKeyColumn;
    private List<ColumnStructureMeta> columnMetaData;
}
