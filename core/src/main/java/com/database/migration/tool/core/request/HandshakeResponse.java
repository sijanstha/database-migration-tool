package com.database.migration.tool.core.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class HandshakeResponse {
    private String message;
    private boolean isConnectionEstablished;
    private String connectionId;
}
