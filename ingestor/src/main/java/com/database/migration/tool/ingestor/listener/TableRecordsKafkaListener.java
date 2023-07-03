package com.database.migration.tool.ingestor.listener;

import com.database.migration.tool.core.dto.TableContentMeta;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TableRecordsKafkaListener {
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${table.records.migration.topic}", groupId = "${table.records.migration.consumer.group}", containerFactory = "kafkaListenerContainerFactory")
    public void processMessage(String message) {
        log.info("got table records message to process {}", message);
        try {
            TableContentMeta tableContentMeta = objectMapper.readValue(message, TableContentMeta.class);
            log.info("converted dto: {}", tableContentMeta);
        } catch (JsonProcessingException e) {
            log.error("got exception while parsing json: {}", e.getMessage());
        }
    }
}