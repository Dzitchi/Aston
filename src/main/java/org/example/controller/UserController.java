package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.UserModel;
import org.example.dto.UserRequestDto;
import org.example.service.UserService;
import org.springframework.hateoas.CollectionModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(
        name = "Users",
        description = "API для управления пользователями"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователи успешно получены"
            )
    })
    @GetMapping
    public CollectionModel<UserModel> getAll() {

        logger.info("GET /api/users");

        List<UserModel> users = userService.getAll();

        users.forEach(this::addUserLinks);

        return CollectionModel.of(
                users,
                linkTo(methodOn(UserController.class).getAll())
                        .withSelfRel()
        );
    }

    @Operation(
            summary = "Получить пользователя",
            description = "Возвращает пользователя по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @GetMapping("/{id}")
    public UserModel getById(
            @Parameter(
                    description = "Идентификатор пользователя",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        logger.info(
                "GET /api/users/{}",
                id
        );

        UserModel user = userService.getById(id);

        addUserLinks(user);

        return user;
    }

    @Operation(
            summary = "Создать пользователя",
            description = "Создает нового пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Пользователь успешно создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные"
            )
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserModel create(
            @Valid @RequestBody UserRequestDto request
    ) {
        logger.info(
                "POST /api/users, email={}",
                request.getEmail()
        );

        UserModel user = userService.create(request);

        addUserLinks(user);

        return user;
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Полностью обновляет данные пользователя"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Пользователь успешно обновлен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @PutMapping("/{id}")
    public UserModel update(
            @Parameter(
                    description = "Идентификатор пользователя",
                    example = "1"
            )
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDto request
    ) {
        logger.info(
                "PUT /api/users/{}",
                id
        );

        UserModel user = userService.update(id, request);

        addUserLinks(user);

        return user;
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по идентификатору"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Пользователь успешно удален"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Пользователь не найден"
            )
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @Parameter(
                    description = "Идентификатор пользователя",
                    example = "1"
            )
            @PathVariable Long id
    ) {
        logger.info(
                "DELETE /api/users/{}",
                id
        );

        userService.delete(id);
    }

    private void addUserLinks(UserModel user) {

        user.add(
                linkTo(
                        methodOn(UserController.class)
                                .getById(user.getId())
                ).withSelfRel()
        );

        user.add(
                linkTo(
                        methodOn(UserController.class)
                                .getAll()
                ).withRel("users")
        );
    }

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);
}
