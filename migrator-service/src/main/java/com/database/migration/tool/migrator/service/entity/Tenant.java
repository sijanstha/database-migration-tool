package com.database.migration.tool.migrator.service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tenant_info")
public class Tenant {
    @Id
    private String connectionId;
    private String mysqlConnectionUrl;
    private String mysqlUsername;
    private String mysqlUserPassword;
}
