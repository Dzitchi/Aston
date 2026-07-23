package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponseDto> getAll() {

        logger.info("GET /api/users");

        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponseDto getById(
            @PathVariable Long id
    ) {
        logger.info(
                "GET /api/users/{}",
                id
        );

        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto create(
            @Valid @RequestBody UserRequestDto request
    ) {
        logger.info(
                "POST /api/users, email={}",
                request.getEmail()
        );

        return userService.create(request);
    }

    @PutMapping("/{id}")
    public UserResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto request
    ) {
        logger.info(
                "PUT /api/users/{}",
                id
        );

        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id
    ) {
        logger.info(
                "DELETE /api/users/{}",
                id
        );

        userService.delete(id);
    }

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);
}
