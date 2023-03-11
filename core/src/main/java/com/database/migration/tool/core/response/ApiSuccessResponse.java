package com.database.migration.tool.core.response;

import lombok.Data;

import java.time.Instant;

@Data
public class ApiSuccessResponse<T> extends ApiResponse {
    private T body;
    private int code;
    private String timestamp;

    public ApiSuccessResponse() {
        this.timestamp = Instant.now().toString();
    }
}