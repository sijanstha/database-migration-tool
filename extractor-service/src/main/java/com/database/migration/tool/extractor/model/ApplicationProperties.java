package com.database.migration.tool.extractor.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationProperties {
    private String bootstrapServer;
    private String tableStructureTopic;
    private String tableRecordsTopic;
    private String migratorServiceUrl;
    private String migratorServiceAccessKey;
}
