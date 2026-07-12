package org.example.menu;

import org.example.entity.User;
import org.example.exception.ValidationException;
import org.example.service.UserService;
import org.example.service.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Scanner;

public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService = new UserServiceImpl();

    public void start() {

        while (true) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> createUser();
                case 2 -> findUser();
                case 3 -> showAllUsers();
                case 4 -> updateUser();
                case 5 -> deleteUser();
                case 0 -> {
                    return;
                }
                default -> System.out.println("Неверный пункт меню.");
            }
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("========== USER SERVICE ==========");
        System.out.println("1. Создать пользователя");
        System.out.println("2. Найти пользователя");
        System.out.println("3. Показать всех пользователей");
        System.out.println("4. Обновить пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("0. Выход");
        System.out.print("Выберите пункт: ");
    }

    private void createUser() {
        try {
            System.out.print("Имя: ");
            String name = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Возраст: ");
            Integer age = Integer.parseInt(scanner.nextLine());

            User user = new User(
                    name,
                    email,
                    age,
                    LocalDateTime.now()
            );

            userService.createUser(user);
            System.out.println("Пользователь успешно создан.");

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    private void findUser() {

        System.out.print("Введите id: ");
        Long id = Long.parseLong(scanner.nextLine());
        User user = userService.getUserById(id);

        if (user == null) {
            System.out.println("Пользователь не найден.");
        } else {
            System.out.println(user);
        }
    }

    private void showAllUsers() {

        userService
                .getAllUsers()
                .forEach(System.out::println);

    }

    private void updateUser() {

        System.out.print("ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());
        User user = userService.getUserById(id);

        if (user == null) {
            System.out.println("Пользователь не найден.");
            return;
        }

        System.out.print("Новое имя: ");
        user.setName(scanner.nextLine());

        System.out.print("Новый email: ");
        user.setEmail(scanner.nextLine());

        System.out.print("Новый возраст: ");
        user.setAge(Integer.parseInt(scanner.nextLine()));

        userService.updateUser(user);
        System.out.println("Пользователь обновлен.");
    }

    private void deleteUser() {

        System.out.print("ID пользователя: ");
        Long id = Long.parseLong(scanner.nextLine());

        userService.deleteUser(id);
        System.out.println("Пользователь удален.");
    }
}
