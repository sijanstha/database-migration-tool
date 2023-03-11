package com.database.migration.tool.extractor.service.config;

import com.database.migration.tool.migrator.sdk.MigratorServiceApi;
import com.database.migration.tool.migrator.sdk.MigratorServiceApiConfig;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;

public class MigratorServiceConfig extends AbstractModule {
    @Provides
    public MigratorServiceApi migratorServiceApi() {
        MigratorServiceApiConfig config = new MigratorServiceApiConfig();
        config.setBaseurl("http://localhost:8080");
        return MigratorServiceApi.client(config);
    }
}
