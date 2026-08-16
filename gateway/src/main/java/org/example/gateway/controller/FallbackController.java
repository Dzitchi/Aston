package org.example.gateway.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Map;

@RestController
public class FallbackController {

    @RequestMapping("/fallback/users")
    public ResponseEntity<Map<String, String>> usersFallback() {

        log.warn("Сработал Fallback для User Service");

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "User service временно недоступен"
                ));
    }

    @RequestMapping("/fallback/notifications")
    public ResponseEntity<Map<String, String>> notificationsFallback() {

        log.warn("Сработал Fallback для Notification Service");

        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of(
                        "error", "Notification service временно недоступен"
                ));
    }

    private static final Logger log =
            LoggerFactory.getLogger(FallbackController.class);
}