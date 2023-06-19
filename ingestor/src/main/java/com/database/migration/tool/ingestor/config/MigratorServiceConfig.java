package com.database.migration.tool.ingestor.config;

import com.database.migration.tool.migrator.sdk.MigratorServiceApi;
import com.database.migration.tool.migrator.sdk.MigratorServiceApiConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MigratorServiceConfig {
    @Value("${migrator.service.api.url}")
    private String url;
    @Value("${migrator.service.api.access.key}")
    private String accessKey;
    @Bean
    public MigratorServiceApi migratorServiceApi() {
        MigratorServiceApiConfig config = new MigratorServiceApiConfig();
        config.setBaseurl(url);
        config.setAccessKey(accessKey);
        return MigratorServiceApi.client(config);
    }
}
