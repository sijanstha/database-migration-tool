package com.database.migration.tool.migrator.sdk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MigratorServiceApiConfig {
    private String baseurl;
    private String accessKey;
}
