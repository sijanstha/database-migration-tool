package com.database.migration.tool.migrator.service.repository;

import com.database.migration.tool.migrator.service.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, String> {
}
