package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class UserModel extends RepresentationModel<UserModel> {

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Ivan")
    private String name;

    @Schema(
            description = "Email пользователя",
            example = "ivan@example.com"
    )
    private String email;

    @Schema(description = "Возраст пользователя", example = "25")
    private Integer age;

    @Schema(
            description = "Дата создания пользователя",
            example = "2026-08-04T14:30:00"
    )
    private LocalDateTime createdAt;

    public UserModel() {
    }

    public UserModel(
            Long id,
            String name,
            String email,
            Integer age,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", createdAt=" + createdAt +
                '}';
    }
}
