package com.database.migration.tool.migrator.sdk;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class MigratorServiceApiConfig {
    private String baseurl;
    private String accessKey;
}
