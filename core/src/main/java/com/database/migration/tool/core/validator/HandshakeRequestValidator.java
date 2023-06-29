package com.database.migration.tool.core.validator;

import com.database.migration.tool.core.request.HandshakeRequest;
import com.database.migration.tool.core.utils.StringUtils;
import com.database.migration.tool.core.validator.HandshakeRequestValidator.HandshakeRequestValidationResult;

import java.util.function.Function;

import static com.database.migration.tool.core.validator.HandshakeRequestValidator.HandshakeRequestValidationResult.*;

public interface HandshakeRequestValidator extends Function<HandshakeRequest, HandshakeRequestValidationResult> {

    enum HandshakeRequestValidationResult {
        SUCCESS("Success"),
        MYSQL_HOST_NOT_VALID("Mysql host is required"),
        MYSQL_USER_NOT_VALID("Mysql user name is required"),
        DB_NAME_NOT_VALID("Mysql database name is required");

        private String message;

        HandshakeRequestValidationResult(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
    }

    static HandshakeRequestValidator isMysqlHostValid() {
        return request -> StringUtils.hasText(request.getMysqlhost()) ? SUCCESS : MYSQL_HOST_NOT_VALID;
    }

    static HandshakeRequestValidator isMysqlUserValid() {
        return request -> StringUtils.hasText(request.getMysqluser()) ? SUCCESS : MYSQL_USER_NOT_VALID;
    }

    static HandshakeRequestValidator isDbNameValid() {
        return request -> StringUtils.hasText(request.getDbname()) ? SUCCESS : DB_NAME_NOT_VALID;
    }

    default HandshakeRequestValidator and(HandshakeRequestValidator other) {
        return request -> {
            HandshakeRequestValidationResult requestValidationResult = this.apply(request);
            return requestValidationResult.equals(SUCCESS) ? other.apply(request) : requestValidationResult;
        };
    }

}
