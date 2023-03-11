package com.database.migration.tool.core.request;

import com.database.migration.tool.core.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HandshakeRequest {
    private String mysqlhost;
    private String dbname;
    private int port;
    private String mysqluser;
    private String mysqlpassword;

    public void validate() {
        if (!StringUtils.hasText(mysqlhost)) {
            throw new IllegalArgumentException("Mysql host is required");
        }

        if (!StringUtils.hasText(mysqluser)) {
            throw new IllegalArgumentException("Mysql user name is required");
        }

        if (!StringUtils.hasText(dbname)) {
            throw new IllegalArgumentException("Mysql database name is required");
        }
    }

}
