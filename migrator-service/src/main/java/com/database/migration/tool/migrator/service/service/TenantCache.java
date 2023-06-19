package com.database.migration.tool.migrator.service.service;

import com.database.migration.tool.migrator.service.entity.Tenant;
import com.database.migration.tool.migrator.service.repository.TenantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class TenantCache {
    private final Map<String, Tenant> tenantCache;
    @Autowired
    private TenantRepository tenantRepository;

    public TenantCache() {
        tenantCache = new HashMap<>();
        tenantRepository.findAll().stream().forEach(tenant -> addTenantToCache(tenant));
        log.info("Tenant info loaded");
    }

    public Optional<Tenant> lookupTenant(String connectionId) {
        Tenant cacheValue = tenantCache.getOrDefault(connectionId, null);
        if (cacheValue != null)
            return Optional.ofNullable(cacheValue);

        Optional<Tenant> optionalTenant = tenantRepository.findById(connectionId);
        if (optionalTenant.isPresent()) {
            addTenantToCache(optionalTenant.get());
            return optionalTenant;
        }

        return Optional.empty();
    }

    public void addTenantToCache(Tenant tenant) {
        tenantCache.computeIfAbsent(tenant.getConnectionId(), k -> tenant);
    }

}
