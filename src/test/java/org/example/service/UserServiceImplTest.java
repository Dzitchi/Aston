package org.example.service;

import org.example.dto.UserModel;
import org.example.dto.UserRequestDto;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserRequestDto request;

    @BeforeEach
    void setUp() {

        user = new User(
                "Bob",
                "bob@test.com",
                20
        );

        request = new UserRequestDto(
                "Bob",
                "bob@test.com",
                20
        );
    }

    @Test
    void create_ShouldCreateUser() {

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserModel result =
                userService.create(request);

        assertNotNull(result);
        assertEquals("Bob", result.getName());
        assertEquals("bob@test.com", result.getEmail());
        assertEquals(20, result.getAge());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void getById_ShouldReturnUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserModel result =
                userService.getById(1L);

        assertNotNull(result);
        assertEquals("Bob", result.getName());

        verify(userRepository).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getById(1L)
        );

        verify(userRepository).findById(1L);
    }

    @Test
    void getAll_ShouldReturnUsers() {

        User secondUser = new User(
                "Alex",
                "alex@test.com",
                25
        );

        when(userRepository.findAll())
                .thenReturn(List.of(user, secondUser));

        List<UserModel> result =
                userService.getAll();

        assertEquals(2, result.size());

        assertEquals(
                "Bob",
                result.get(0).getName()
        );

        assertEquals(
                "Alex",
                result.get(1).getName()
        );

        verify(userRepository).findAll();
    }

    @Test
    void update_ShouldUpdateUser() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(userRepository.save(user))
                .thenReturn(user);

        UserRequestDto updateRequest =
                new UserRequestDto(
                        "Updated Bob",
                        "updated@test.com",
                        30
                );

        UserModel result =
                userService.update(
                        1L,
                        updateRequest
                );

        assertEquals(
                "Updated Bob",
                result.getName()
        );

        assertEquals(
                "updated@test.com",
                result.getEmail()
        );

        assertEquals(
                30,
                result.getAge()
        );

        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void update_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.update(1L, request)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void delete_ShouldDeleteUser() {

        when(userRepository.existsById(1L))
                .thenReturn(true);

        userService.delete(1L);

        verify(userRepository)
                .existsById(1L);

        verify(userRepository)
                .deleteById(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.existsById(1L))
                .thenReturn(false);

        assertThrows(
                UserNotFoundException.class,
                () -> userService.delete(1L)
        );

        verify(userRepository, never())
                .deleteById(anyLong());
    }
}
