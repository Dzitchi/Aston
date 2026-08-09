package org.example.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с ошибкой")
public class ErrorResponse {

    @Schema(
            description = "Описание ошибки",
            example = "Пользователь с id 999 не найден"
    )
    private String error;

    public ErrorResponse() {
    }

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
