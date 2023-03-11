package com.database.migration.tool.core.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HandshakeResponse {
    private String message;
    private boolean isConnectionEstablished;
}
