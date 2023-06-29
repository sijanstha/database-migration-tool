package com.database.migration.tool.core.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@ToString
public class TableContentMeta extends BaseMeta {
    private Set<String> columns;
    private Map<String, ColumnValueMeta> data;
}
