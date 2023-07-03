package com.database.migration.tool.extractor.config;

import com.database.migration.tool.core.utils.Utils;
import com.database.migration.tool.extractor.service.DataTypeMapperService;
import com.database.migration.tool.migrator.sdk.MigratorServiceApi;
import com.database.migration.tool.migrator.sdk.MigratorServiceApiConfig;
import com.google.gson.Gson;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationBeanConfig extends AbstractModule {
    @Provides
    public MigratorServiceApi migratorServiceApi() {
        return MigratorServiceApi.client(loadConfig());
    }

    @Provides
    public DataTypeMapperService dataTypeMapperService() {
        return new DataTypeMapperService();
    }

    @Provides
    public Gson gson() {
        return new Gson();
    }

    private MigratorServiceApiConfig loadConfig() {
        try (InputStream input = new FileInputStream(Utils.loadResources("application.properties"))) {
            Properties prop = new Properties();
            prop.load(input);

            MigratorServiceApiConfig config = new MigratorServiceApiConfig();
            config.setBaseurl(prop.getProperty("migrator.service.url"));
            config.setAccessKey(prop.getProperty("migrator.service.access.key"));
            return config;
        } catch (IOException ex) {
            throw new RuntimeException("cannot access config file, exiting");
        }
    }
}
