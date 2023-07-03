package com.database.migration.tool.extractor.service;

import com.database.migration.tool.core.dto.TableContentMeta;
import com.database.migration.tool.core.dto.TableStructureMeta;
import com.database.migration.tool.extractor.model.ApplicationProperties;
import com.google.gson.Gson;
import io.activej.inject.annotation.Inject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaMessageDispatcher {
    private final KafkaProducer<String, String> producer;
    private final ApplicationProperties applicationProperties;
    private final Gson gson;

    @Inject
    public KafkaMessageDispatcher(KafkaProducer<String, String> producer, ApplicationProperties applicationProperties, Gson gson) {
        this.producer = producer;
        this.applicationProperties = applicationProperties;
        this.gson = gson;
    }

    public void publishTableStructure(TableStructureMeta tableStructureMeta) {
        String json = gson.toJson(tableStructureMeta);
        System.out.printf("Preparing to send table structure %s%n", json);
        ProducerRecord<String, String> record = new ProducerRecord<>(applicationProperties.getTableStructureTopic(), json);
        this.producer.send(record);
        this.producer.flush();
    }

    public void publishTableRecords(TableContentMeta tableContentMeta) {
        String json = gson.toJson(tableContentMeta);
        System.out.printf("Preparing to send table records %s%n", json);
        ProducerRecord<String, String> record = new ProducerRecord<>(applicationProperties.getTableRecordsTopic(), json);
        this.producer.send(record);
        this.producer.flush();
    }
}
