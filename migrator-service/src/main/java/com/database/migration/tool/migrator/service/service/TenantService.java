package com.database.migration.tool.migrator.service.service;

import com.database.migration.tool.core.utils.StringUtils;
import com.database.migration.tool.migrator.service.entity.Tenant;
import com.database.migration.tool.migrator.service.repository.TenantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TenantService {
    private final TenantRepository tenantRepository;
    private final TenantCache tenantCache;

    public void saveTenant(String connectionId, String url, String user, String password) {
        Tenant tenant = new Tenant();
        tenant.setConnectionId(connectionId);
        tenant.setMysqlConnectionUrl(url);
        tenant.setMysqlUsername(user);
        tenant.setMysqlUserPassword(password);

        log.info("preparing to save tenant info to db [{}]", tenant);
        tenantRepository.save(tenant);
        tenantCache.addTenantToCache(tenant);
        log.info("tenant info saved with connection id [{}]", connectionId);
    }

    public Optional<Tenant> findTenant(String connectionId) {
        if (!StringUtils.hasText(connectionId))
            return Optional.empty();
        return tenantCache.lookupTenant(connectionId);
    }
}
