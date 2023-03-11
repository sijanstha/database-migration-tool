package com.database.migration.tool.migrator.sdk;

import com.database.migration.tool.core.request.HandshakeRequest;
import com.database.migration.tool.core.request.HandshakeResponse;
import com.database.migration.tool.core.response.ApiSuccessResponse;
import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface MigratorServiceEndpoints {
    @RequestLine("POST /api/initial/handshake")
    ApiSuccessResponse<HandshakeResponse> initialHandshake(HandshakeRequest request);
}
