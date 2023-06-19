package com.database.migration.tool.core.response;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.time.Instant;

@Data
@Builder
public class ApiSuccessResponse<T> extends ApiResponse {
    private T body;
    private int code;
    private String timestamp;

    @Tolerate
    public ApiSuccessResponse() {
        this.timestamp = Instant.now().toString();
    }
}