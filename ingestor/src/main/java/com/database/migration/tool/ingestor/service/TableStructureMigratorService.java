package com.database.migration.tool.ingestor.service;

import com.database.migration.tool.core.dto.TableStructureMeta;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TableStructureMigratorService {
    private final JdbcTemplate jdbcTemplate;

    public void process(TableStructureMeta tableStructureMeta) {
        log.info("processing on thread: {}", Thread.currentThread().getName());
        String sql = String.format("create table %s(id int primary key, name varchar(100))", tableStructureMeta.getTableName());
        jdbcTemplate.execute(sql);
    }
}
