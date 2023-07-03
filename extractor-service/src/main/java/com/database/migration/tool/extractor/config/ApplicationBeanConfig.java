package com.database.migration.tool.extractor.config;

import com.database.migration.tool.core.utils.Utils;
import com.database.migration.tool.extractor.model.ApplicationProperties;
import com.database.migration.tool.extractor.service.DataTypeMapperService;
import com.database.migration.tool.extractor.service.KafkaMessageDispatcher;
import com.database.migration.tool.migrator.sdk.MigratorServiceApi;
import com.database.migration.tool.migrator.sdk.MigratorServiceApiConfig;
import com.google.gson.Gson;
import io.activej.inject.Injector;
import io.activej.inject.annotation.Inject;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationBeanConfig extends AbstractModule {
    @Provides
    @Inject
    public MigratorServiceApi migratorServiceApi(ApplicationProperties applicationProperties) {
        return MigratorServiceApi.client(new MigratorServiceApiConfig(applicationProperties.getMigratorServiceUrl(), applicationProperties.getMigratorServiceAccessKey()));
    }

    @Provides
    public DataTypeMapperService dataTypeMapperService() {
        return new DataTypeMapperService();
    }

    @Provides
    public Gson gson() {
        return new Gson();
    }

    @Provides
    public ApplicationProperties loadApplicationProperties() {
        System.out.println("loading application properties");
        try (InputStream input = new FileInputStream(Utils.loadResources("application.properties"))) {
            Properties prop = new Properties();
            prop.load(input);

            ApplicationProperties config = new ApplicationProperties();
            config.setMigratorServiceUrl(prop.getProperty("migrator.service.url"));
            config.setMigratorServiceAccessKey(prop.getProperty("migrator.service.access.key"));
            config.setBootstrapServer(prop.getProperty("kafka.bootstrap.servers"));
            config.setTableRecordsTopic(prop.getProperty("table.records.migration.topic"));
            config.setTableStructureTopic(prop.getProperty("table.structure.migration.topic"));
            return config;
        } catch (IOException ex) {
            throw new RuntimeException("cannot access config file, exiting");
        }
    }

    @Provides
    public KafkaProducer<String, String> kafkaProducerConfig() {
        Injector injector = Injector.of(new ApplicationBeanConfig());
        ApplicationProperties applicationProperties = injector.getInstance(ApplicationProperties.class);
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationProperties.getBootstrapServer());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new KafkaProducer<>(props);
    }

    @Provides
    @Inject
    public KafkaMessageDispatcher kafkaMessageDispatcher(KafkaProducer<String, String> producer, ApplicationProperties applicationProperties, Gson gson) {
        return new KafkaMessageDispatcher(producer, applicationProperties, gson);
    }
}
