package com.database.migration.tool.core.dto;

import lombok.*;

@Getter
@Setter
@ToString
public abstract class BaseMeta {
    private String connectionId;
    private SqlCommandEnum type;
    private SqlStatementEnum statement;
    private String tableName;
}