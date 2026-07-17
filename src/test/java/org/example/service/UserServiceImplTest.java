package org.example.service;

import org.example.dao.UserDao;
import org.example.entity.User;
import org.example.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userDao);
    }

    @Test
    void createUser_ShouldSaveUser() {

        User user = new User(
                "Bob",
                "bob@example.com",
                20,
                LocalDateTime.now()
        );

        userService.createUser(user);

        verify(userDao).save(user);
    }

    @Test
    void getUserById_ShouldReturnUser() {

        User user = new User(
                "Bob",
                "bob@example.com",
                20,
                LocalDateTime.now()
        );

        when(userDao.findById(1L)).thenReturn(user);

        User result = userService.getUserById(1L);

        assertEquals(user, result);

        verify(userDao).findById(1L);
    }

    @Test
    void getAllUsers_ShouldReturnList() {

        List<User> users = List.of(
                new User("Bob", "bob@example.com", 20, LocalDateTime.now()),
                new User("Alex", "alex@example.com", 30, LocalDateTime.now())
        );

        when(userDao.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(2, result.size());

        verify(userDao).findAll();
    }

    @Test
    void updateUser_ShouldCallDao() {

        User user = new User(
                "Bob",
                "bob@example.com",
                25,
                LocalDateTime.now()
        );

        userService.updateUser(user);

        verify(userDao).update(user);
    }

    @Test
    void deleteUser_ShouldCallDao() {

        userService.deleteUser(1L);

        verify(userDao).delete(1L);
    }

    @Test
    void createUser_ShouldThrowValidationException_WhenEmailInvalid() {

        User user = new User(
                "Bob",
                "wrong_email",
                20,
                LocalDateTime.now()
        );

        assertThrows(
                ValidationException.class,
                () -> userService.createUser(user)
        );

        verify(userDao, never()).save(any());
    }

    @Test
    void createUser_ShouldThrowValidationException_WhenAgeInvalid() {

        User user = new User(
                "Bob",
                "bob@example.com",
                -5,
                LocalDateTime.now()
        );

        assertThrows(
                ValidationException.class,
                () -> userService.createUser(user)
        );

        verify(userDao, never()).save(any());
    }

    @Test
    void createUser_ShouldThrowValidationException_WhenNameEmpty() {

        User user = new User(
                "",
                "bob@example.com",
                20,
                LocalDateTime.now()
        );

        assertThrows(
                ValidationException.class,
                () -> userService.createUser(user)
        );

        verify(userDao, never()).save(any());
    }
}
