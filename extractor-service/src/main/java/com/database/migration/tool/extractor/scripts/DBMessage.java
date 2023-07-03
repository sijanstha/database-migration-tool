package com.database.migration.tool.extractor.scripts;

import lombok.Getter;
import lombok.Setter;

import java.sql.Connection;

@Getter
@Setter
public class DBMessage extends AppMessage {
    private Connection dbCon;
}
