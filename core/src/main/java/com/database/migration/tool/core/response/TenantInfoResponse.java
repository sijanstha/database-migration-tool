package com.database.migration.tool.core.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TenantInfoResponse {
    private String connectionId;
    private String url;
    private String user;
    private String password;
}
