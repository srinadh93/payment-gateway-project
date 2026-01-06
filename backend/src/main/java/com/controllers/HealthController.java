package com.gateway.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> checkHealth() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "healthy");
        status.put("timestamp", java.time.LocalDateTime.now().toString());

        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(1)) {
                status.put("database", "connected");
            } else {
                status.put("database", "disconnected");
            }
        } catch (Exception e) {
            status.put("database", "error: " + e.getMessage());
            return ResponseEntity.internalServerError().body(status);
        }

        return ResponseEntity.ok(status);
    }
}