package com.database.migration.tool.migrator.sdk;

import com.database.migration.tool.core.request.HandshakeRequest;
import com.database.migration.tool.core.request.HandshakeResponse;
import com.database.migration.tool.core.response.ApiSuccessResponse;
import com.database.migration.tool.core.response.TenantInfoResponse;
import feign.Feign;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

import java.util.HashMap;
import java.util.Map;

public class MigratorServiceApi {
    private MigratorServiceEndpoints endpoints;

    private static final Map<MigratorServiceApiConfig, MigratorServiceApi> migratorServiceApiCache = new HashMap<>();

    private MigratorServiceApi(MigratorServiceEndpoints endpoints) {
        this.endpoints = endpoints;
    }

    public static MigratorServiceApi client(MigratorServiceApiConfig config) {
        if (!migratorServiceApiCache.containsKey(config)) {
            migratorServiceApiCache.put(config, new MigratorServiceApi(Feign.builder()
                    .logger(new Slf4jLogger())
                    .encoder(new JacksonEncoder())
                    .decoder(new JacksonDecoder())
                    .client(new ApacheHttpClient())
                    .target(MigratorServiceEndpoints.class, config.getBaseurl())));
        }

        return migratorServiceApiCache.get(config);
    }

    public HandshakeResponse initiateInitialHandshake(HandshakeRequest request) {
        ApiSuccessResponse<HandshakeResponse> response = this.endpoints.initialHandshake(request);
        return response.getBody();
    }

    public TenantInfoResponse getTenantInfo(String connectionId) {
        ApiSuccessResponse<TenantInfoResponse> response = this.endpoints.getTenantInfo(connectionId);
        return response.getBody();
    }
}
