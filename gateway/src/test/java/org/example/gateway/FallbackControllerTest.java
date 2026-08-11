package org.example.gateway;

import org.example.gateway.controller.FallbackController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(FallbackController.class)
@ActiveProfiles("test")
class FallbackControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void usersFallback_ShouldReturnServiceUnavailable() {

        webTestClient
                .get()
                .uri("/fallback/users")
                .exchange()
                .expectStatus()
                .isEqualTo(503)
                .expectBody()
                .json("""
                        {
                            "error": "User service временно недоступен"
                        }
                        """);
    }

    @Test
    void notificationsFallback_ShouldReturnServiceUnavailable() {

        webTestClient
                .get()
                .uri("/fallback/notifications")
                .exchange()
                .expectStatus()
                .isEqualTo(503)
                .expectBody()
                .json("""
                        {
                            "error": "Notification service временно недоступен"
                        }
                        """);
    }
}
