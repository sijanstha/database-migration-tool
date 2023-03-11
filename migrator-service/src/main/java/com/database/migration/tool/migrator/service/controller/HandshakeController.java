package com.database.migration.tool.migrator.service.controller;

import com.database.migration.tool.core.request.HandshakeRequest;
import com.database.migration.tool.core.request.HandshakeResponse;
import com.database.migration.tool.core.response.ApiResponse;
import com.database.migration.tool.core.response.ApiSuccessResponse;
import com.database.migration.tool.migrator.service.service.MysqlConnectionVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class HandshakeController {
    private final MysqlConnectionVerifier connectionVerifier;

    @PostMapping("/initial/handshake")
    public ApiResponse doHandshake(@RequestBody HandshakeRequest request) {
        log.info("Got request for initial handshake, [{}]", request);
        HandshakeResponse handshakeResponse = connectionVerifier.verifyMysqlConnection(request);
        ApiSuccessResponse<HandshakeResponse> response = new ApiSuccessResponse<>();
        response.setBody(handshakeResponse);
        response.setCode(handshakeResponse.isConnectionEstablished() ? HttpStatus.OK.value() : HttpStatus.BAD_REQUEST.value());
        return response;
    }
}
