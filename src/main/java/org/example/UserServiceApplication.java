package org.example;

import org.example.menu.ConsoleMenu;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication
        implements CommandLineRunner {

    private final ConsoleMenu consoleMenu;

    public UserServiceApplication(
            ConsoleMenu consoleMenu
    ) {
        this.consoleMenu = consoleMenu;
    }

    public static void main(String[] args) {

        SpringApplication.run(
                UserServiceApplication.class,
                args
        );
    }

    @Override
    public void run(String... args) {

        consoleMenu.start();
    }
}
