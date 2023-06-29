package com.database.migration.tool.migrator.sdk;

import com.database.migration.tool.core.request.HandshakeRequest;
import com.database.migration.tool.core.request.HandshakeResponse;
import com.database.migration.tool.core.response.ApiSuccessResponse;
import com.database.migration.tool.core.response.TenantInfoResponse;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface MigratorServiceEndpoints {
    @RequestLine("POST /api/initial/handshake")
    ApiSuccessResponse<HandshakeResponse> initialHandshake(HandshakeRequest request);

    @RequestLine("GET /api/tenant/info/{connectionId}")
    ApiSuccessResponse<TenantInfoResponse> getTenantInfo(@Param("connectionId") String connectionId);
}
