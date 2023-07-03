package com.database.migration.tool.ingestor.config;

import com.database.migration.tool.core.response.TenantInfoResponse;
import com.database.migration.tool.core.storage.ThreadLocalStorage;
import com.database.migration.tool.migrator.sdk.MigratorServiceApi;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class DynamicTenantAwareService extends AbstractRoutingDataSource {
    private final Map<String, HikariDataSource> tenants;

    @Autowired
    private MigratorServiceApi migratorServiceApi;

    public DynamicTenantAwareService() {
        this.tenants = new HashMap<>();
    }

    @Override
    protected DataSource determineTargetDataSource() {
        String lookupKey = (String) determineCurrentLookupKey();
        return tenants.get(lookupKey);
    }

    @Override
    public void afterPropertiesSet() {
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return ThreadLocalStorage.getTenantName();
    }

    private HikariDataSource buildDataSource(TenantInfoResponse configuration) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(configuration.getUrl());
        config.setUsername(configuration.getUser());
        config.setPassword(configuration.getPassword());
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        return new HikariDataSource(config);
    }

    public void registerTenantDatasource(String connectionId) {
        if (!tenants.containsKey(connectionId)) {
            log.info("setting up tenant with connection id: {}", connectionId);
            try {
                TenantInfoResponse tenantInfo = migratorServiceApi.getTenantInfo(connectionId);
                log.info("got tenant info response: {}", tenantInfo);
                this.tenants.computeIfAbsent(tenantInfo.getConnectionId(), k -> buildDataSource(tenantInfo));
            } catch (FeignException exception) {
                log.error("got exception while fetching tenant info {}", exception.getMessage());
            }
        }
    }

    public void releaseTenantConnection(String connectionId) {
        HikariDataSource dataSource = tenants.getOrDefault(connectionId, null);
        if (dataSource != null) {
            dataSource.close();
            tenants.remove(connectionId);
        }
    }
}
