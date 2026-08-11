package org.example.gateway;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class GatewayRoutingTest {

    private static final WireMockServer wireMock =
            new WireMockServer(options().dynamicPort());

    @BeforeAll
    static void startWireMock() {
        wireMock.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMock.stop();
    }

    @DynamicPropertySource
    static void configureProperties(
            DynamicPropertyRegistry registry
    ) {
        registry.add(
                "wiremock.server.port",
                wireMock::port
        );
    }

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void usersRoute_ShouldForwardRequestToUserService() {

        wireMock.stubFor(
                get(urlEqualTo("/api/users/123"))
                        .willReturn(
                                aResponse()
                                        .withStatus(200)
                                        .withHeader(
                                                "Content-Type",
                                                "application/json"
                                        )
                                        .withBody("""
                                                {
                                                    "id": 123,
                                                    "name": "Bob",
                                                    "email": "bob@test.com",
                                                    "age": 20
                                                }
                                                """)
                        )
        );

        webTestClient
                .get()
                .uri("/api/users/123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .contentType("application/json")
                .expectBody()
                .json("""
                        {
                            "id": 123,
                            "name": "Bob",
                            "email": "bob@test.com",
                            "age": 20
                        }
                        """);

        wireMock.verify(
                getRequestedFor(
                        urlEqualTo("/api/users/123")
                )
        );
    }
}
