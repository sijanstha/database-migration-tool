package com.database.migration.tool.migrator.service.service;

import com.database.migration.tool.core.request.HandshakeRequest;
import com.database.migration.tool.core.request.HandshakeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Service
@Slf4j
public class MysqlConnectionVerifier {

    public HandshakeResponse verifyMysqlConnection(HandshakeRequest request) {
        request.validate();
        int mysqlPort = request.getPort() <= 0 ? 3306 : request.getPort();
        String url = String.format("jdbc:mysql://%s:%d/%s", request.getMysqlhost(), mysqlPort, request.getDbname());
        log.info("Preparing mysql jdbc url to verify connection, [{}]", url);
        try (Connection connection = DriverManager.getConnection(url, request.getMysqluser(), request.getMysqlpassword());
             Statement statement = connection.createStatement()) {
            boolean isConnectionEstablished = statement.execute("SELECT 1");
            String message = isConnectionEstablished ? "Connection established successfully" : "Couldn't establish mysql connection";
            return new HandshakeResponse(message, isConnectionEstablished);
        } catch (SQLException e) {
            log.error("Got exception while connecting mysql db, {}", e.getMessage());
            String message = String.format("Couldn't establish mysql connection, %s", e.getMessage());
            return new HandshakeResponse(message, false);
        }
    }
}
