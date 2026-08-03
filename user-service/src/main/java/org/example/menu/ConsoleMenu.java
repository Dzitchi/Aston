package org.example.menu;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exception.UserNotFoundException;
import org.example.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@Profile("!test")
public class ConsoleMenu implements CommandLineRunner {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;

    public ConsoleMenu(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        start();
    }

    public void start() {

        while (true) {
            printMenu();

            try {
                int choice = Integer.parseInt(
                        scanner.nextLine()
                );

                switch (choice) {
                    case 1 -> createUser();
                    case 2 -> findUser();
                    case 3 -> showAllUsers();
                    case 4 -> updateUser();
                    case 5 -> deleteUser();
                    default -> System.out.println(
                            "Неверный пункт меню."
                    );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Введите корректное число."
                );
            }

            System.out.println();
        }
    }

    private void printMenu() {

        System.out.println();
        System.out.println(
                "========== USER SERVICE =========="
        );
        System.out.println(
                "1. Создать пользователя"
        );
        System.out.println(
                "2. Найти пользователя"
        );
        System.out.println(
                "3. Показать всех пользователей"
        );
        System.out.println(
                "4. Обновить пользователя"
        );
        System.out.println(
                "5. Удалить пользователя"
        );
        System.out.println(
                "0. Выход"
        );
        System.out.print(
                "Выберите пункт: "
        );
    }

    private void createUser() {

        try {
            System.out.print("Имя: ");
            String name = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Возраст: ");
            Integer age = Integer.parseInt(
                    scanner.nextLine()
            );

            UserRequestDto request =
                    new UserRequestDto(
                            name,
                            email,
                            age
                    );

            UserResponseDto user =
                    userService.create(request);

            System.out.println(
                    "Пользователь успешно создан:"
            );

            System.out.println(user);

        } catch (NumberFormatException e) {

            System.out.println(
                    "Возраст должен быть числом."
            );

        } catch (Exception e) {

            System.out.println(
                    "Ошибка: " + e.getMessage()
            );
        }
    }

    private void findUser() {

        try {
            System.out.print(
                    "Введите id: "
            );

            Long id = Long.parseLong(
                    scanner.nextLine()
            );

            UserResponseDto user =
                    userService.getById(id);

            System.out.println(user);

        } catch (NumberFormatException e) {

            System.out.println(
                    "ID должен быть числом."
            );

        } catch (UserNotFoundException e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }

    private void showAllUsers() {

        List<UserResponseDto> users =
                userService.getAll();

        if (users.isEmpty()) {

            System.out.println(
                    "Пользователи отсутствуют."
            );

            return;
        }

        users.forEach(
                System.out::println
        );
    }

    private void updateUser() {

        try {
            System.out.print(
                    "ID пользователя: "
            );

            Long id = Long.parseLong(
                    scanner.nextLine()
            );

            userService.getById(id);

            System.out.print(
                    "Новое имя: "
            );

            String name =
                    scanner.nextLine();

            System.out.print(
                    "Новый email: "
            );

            String email =
                    scanner.nextLine();

            System.out.print(
                    "Новый возраст: "
            );

            Integer age =
                    Integer.parseInt(
                            scanner.nextLine()
                    );

            UserRequestDto request =
                    new UserRequestDto(
                            name,
                            email,
                            age
                    );

            UserResponseDto updatedUser =
                    userService.update(
                            id,
                            request
                    );

            System.out.println(
                    "Пользователь успешно обновлен:"
            );

            System.out.println(updatedUser);

        } catch (NumberFormatException e) {

            System.out.println(
                    "ID и возраст должны быть числами."
            );

        } catch (UserNotFoundException e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }

    private void deleteUser() {

        try {
            System.out.print(
                    "ID пользователя: "
            );

            Long id = Long.parseLong(
                    scanner.nextLine()
            );

            userService.delete(id);

            System.out.println(
                    "Пользователь успешно удален."
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "ID должен быть числом."
            );

        } catch (UserNotFoundException e) {

            System.out.println(
                    e.getMessage()
            );
        }
    }
}
