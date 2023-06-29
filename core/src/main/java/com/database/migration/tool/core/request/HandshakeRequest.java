package com.database.migration.tool.core.request;

import lombok.Getter;
import lombok.Setter;

import static com.database.migration.tool.core.validator.HandshakeRequestValidator.*;
import static com.database.migration.tool.core.validator.HandshakeRequestValidator.HandshakeRequestValidationResult.SUCCESS;
@Getter
@Setter
public class HandshakeRequest {
    private String mysqlhost;
    private String dbname;
    private int port;
    private String mysqluser;
    private String mysqlpassword;

    public void validate() {
        HandshakeRequestValidationResult validator = isMysqlHostValid()
                .and(isMysqlUserValid())
                .and(isDbNameValid())
                .apply(this);

        if (!validator.equals(SUCCESS)) {
            throw new IllegalArgumentException(validator.getMessage());
        }
    }

}
