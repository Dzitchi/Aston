package org.example.controller;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void configureProperties(
            DynamicPropertyRegistry registry
    ) {

        registry.add(
                "spring.datasource.url",
                postgres::getJdbcUrl
        );

        registry.add(
                "spring.datasource.username",
                postgres::getUsername
        );

        registry.add(
                "spring.datasource.password",
                postgres::getPassword
        );
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {

        userRepository.deleteAll();
    }

    @Test
    void create_ShouldReturnCreatedUserWithHateoasLinks()
            throws Exception {

        String json = """
                {
                    "name": "Bob",
                    "email": "bob@test.com",
                    "age": 20
                }
                """;

        mockMvc.perform(
                        post("/api/users")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name")
                        .value("Bob"))
                .andExpect(jsonPath("$.email")
                        .value("bob@test.com"))
                .andExpect(jsonPath("$.age")
                        .value(20))
                .andExpect(jsonPath("$.createdAt")
                        .exists())
                .andExpect(jsonPath("$._links.self.href")
                        .exists())
                .andExpect(jsonPath("$._links.users.href")
                        .exists());
    }

    @Test
    void create_ShouldReturnBadRequest_WhenNameIsBlank()
            throws Exception {

        String json = """
            {
                "name": "",
                "email": "test@test.com",
                "age": 20
            }
            """;

        mockMvc.perform(
                        post("/api/users")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_ShouldReturnBadRequest_WhenEmailIsInvalid()
            throws Exception {

        String json = """
            {
                "name": "Bob",
                "email": "invalid-email",
                "age": 20
            }
            """;

        mockMvc.perform(
                        post("/api/users")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_ShouldReturnBadRequest_WhenAgeIsInvalid()
            throws Exception {

        String json = """
            {
                "name": "Bob",
                "email": "bob@test.com",
                "age": -1
            }
            """;

        mockMvc.perform(
                        post("/api/users")
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll_ShouldReturnUsersWithHateoasLinks()
            throws Exception {

        userRepository.save(
                new User(
                        "Bob",
                        "bob@test.com",
                        20
                )
        );

        mockMvc.perform(get("/api/users"))
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$._embedded")
                        .exists())
                .andExpect(jsonPath("$._embedded.userModelList")
                        .isArray())
                .andExpect(jsonPath("$._embedded.userModelList.length()")
                        .value(1))

                .andExpect(jsonPath("$._embedded.userModelList[0].name")
                        .value("Bob"))
                .andExpect(jsonPath("$._embedded.userModelList[0].email")
                        .value("bob@test.com"))

                .andExpect(jsonPath("$._embedded.userModelList[0]._links.self.href")
                        .exists())
                .andExpect(jsonPath("$._embedded.userModelList[0]._links.users.href")
                        .exists())
                .andExpect(jsonPath("$._links.self.href")
                        .exists());
    }

    @Test
    void getById_ShouldReturnUserWithHateoasLinks()
            throws Exception {

        User user = userRepository.save(
                new User(
                        "Bob",
                        "bob@test.com",
                        20
                )
        );

        mockMvc.perform(
                        get("/api/users/" + user.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(user.getId()))
                .andExpect(jsonPath("$.name")
                        .value("Bob"))
                .andExpect(jsonPath("$.email")
                        .value("bob@test.com"))
                .andExpect(jsonPath("$.age")
                        .value(20))

                .andExpect(jsonPath("$._links.self.href")
                        .value( "http://localhost/api/users/" + user.getId() ))
                .andExpect(jsonPath("$._links.users.href")
                        .value( "http://localhost/api/users" ));
    }

    @Test
    void update_ShouldUpdateUserAndReturnHateoasLinks()
            throws Exception {

        User user = userRepository.save(
                new User(
                        "Bob",
                        "bob@test.com",
                        20
                )
        );

        String json = """
                {
                    "name": "Updated Bob",
                    "email": "updated@test.com",
                    "age": 30
                }
                """;

        mockMvc.perform(
                        put("/api/users/" + user.getId())
                                .contentType(APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name")
                        .value("Updated Bob"))
                .andExpect(jsonPath("$.email")
                        .value("updated@test.com"))
                .andExpect(jsonPath("$.age")
                        .value(30))

                .andExpect(jsonPath("$._links.self.href")
                        .exists())
                .andExpect(jsonPath("$._links.users.href")
                        .exists());
    }

    @Test
    void update_ShouldReturnNotFound_WhenUserDoesNotExist()
            throws Exception {

        String json = """
                {
                    "name": "Updated Bob",
                    "email": "updated@test.com",
                    "age": 30
                }
                """;

        mockMvc.perform(
                        put("/api/users/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Пользователь с id 999 не найден")); }

    @Test
    void delete_ShouldDeleteUser()
            throws Exception {

        User user = userRepository.save(
                new User(
                        "Bob",
                        "bob@test.com",
                        20
                )
        );

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ShouldReturnNotFound_WhenUserDoesNotExist()
            throws Exception {

        mockMvc.perform(
                        delete("/api/users/999")
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("Пользователь с id 999 не найден"));
    }
}
