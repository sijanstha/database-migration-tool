package com.database.migration.tool.migrator.service.controller;

import com.database.migration.tool.core.response.ApiSuccessResponse;
import com.database.migration.tool.core.response.TenantInfoResponse;
import com.database.migration.tool.migrator.service.entity.Tenant;
import com.database.migration.tool.migrator.service.service.TenantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tenant/info")
@AllArgsConstructor
@Slf4j
public class TenantInfoController {
    private final TenantService tenantService;

    @GetMapping("/{connectionId}")
    public ApiSuccessResponse<TenantInfoResponse> getTenantInfoByConnectionId(@PathVariable String connectionId) {
        log.info("Got request to fetch tenant info by connectionId [{}]", connectionId);
        Tenant tenant = tenantService.findTenant(connectionId)
                .orElseThrow(() -> new RuntimeException(String.format("Cannot find tenant info under this connectionId: %s", connectionId)));

        TenantInfoResponse body = new TenantInfoResponse(tenant.getConnectionId(), tenant.getMysqlConnectionUrl(), tenant.getMysqlUsername(), tenant.getMysqlUserPassword());
        log.info("Preparing to send tenant info [{}]", body);
        return ApiSuccessResponse.<TenantInfoResponse>builder()
                .body(body)
                .code(200)
                .build();
    }
}
