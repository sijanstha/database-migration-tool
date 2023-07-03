package com.database.migration.tool.ingestor.listener;

import com.database.migration.tool.core.dto.TableStructureMeta;
import com.database.migration.tool.core.storage.ThreadLocalStorage;
import com.database.migration.tool.ingestor.config.DynamicTenantAwareService;
import com.database.migration.tool.ingestor.service.TableStructureMigratorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TableStructureKafkaListener {
    private final ObjectMapper objectMapper;
    private final DynamicTenantAwareService dynamicTenantAwareService;
    private final TableStructureMigratorService tableStructureMigratorService;

    @KafkaListener(topics = "${table.structure.migration.topic}", groupId = "${table.structure.migration.consumer.group}", containerFactory = "kafkaListenerContainerFactory")
    public void processMessage(String message) {
        log.info("got table structure message to process {}", message);
        try {
            TableStructureMeta tableStructureMeta = objectMapper.readValue(message, TableStructureMeta.class);
            log.info("converted dto: {}", tableStructureMeta);
            dynamicTenantAwareService.registerTenantDatasource(tableStructureMeta.getConnectionId());
            ThreadLocalStorage.setTenantName(tableStructureMeta.getConnectionId());
            tableStructureMigratorService.process(tableStructureMeta);
        } catch (JsonProcessingException e) {
            log.error("got exception while parsing json: {}", e.getMessage());
        }
    }
}
