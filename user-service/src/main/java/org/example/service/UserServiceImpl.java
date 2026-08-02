package org.example.service;

import org.example.dto.UserEvent;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.entity.User;
import org.example.exception.UserNotFoundException;
import org.example.kafka.UserEventProducer;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    public UserServiceImpl(
            UserRepository userRepository,
            UserEventProducer userEventProducer
    ) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    @Override
    @Transactional
    public UserResponseDto create(UserRequestDto request) {

        logger.info(
                "Создание пользователя: name={}, email={}, age={}",
                request.getName(),
                request.getEmail(),
                request.getAge()
        );

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getAge()
        );

        User savedUser = userRepository.save(user);

        UserEvent event = new UserEvent(
                "CREATE",
                savedUser.getEmail()
        );

        userEventProducer.send(event);

        logger.info(
                "Пользователь успешно создан: id={}",
                savedUser.getId()
        );

        return toResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getById(Long id) {

        logger.info("Поиск пользователя по id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id)
                );

        logger.info(
                "Пользователь найден: id={}, email={}",
                user.getId(),
                user.getEmail()
        );

        return toResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAll() {

        logger.info("Получение списка всех пользователей");

        List<UserResponseDto> users = userRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();

        logger.info(
                "Получено пользователей: {}",
                users.size()
        );

        return users;
    }

    @Override
    @Transactional
    public UserResponseDto update(
            Long id,
            UserRequestDto request
    ) {

        logger.info(
                "Обновление пользователя: id={}",
                id
        );

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id)
                );

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());

        User updatedUser = userRepository.save(user);

        logger.info(
                "Пользователь успешно обновлен: id={}",
                updatedUser.getId()
        );

        return toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        logger.info(
                "Удаление пользователя: id={}",
                id
        );

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(id)
                );

        String email = user.getEmail();

        userRepository.delete(user);

        UserEvent event = new UserEvent(
                "DELETE",
                email
        );

        userEventProducer.send(event);

        logger.info(
                "Пользователь успешно удален: id={}",
                id
        );
    }

    private UserResponseDto toResponseDto(User user) {

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);
}
