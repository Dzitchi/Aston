package org.example.service;

import org.example.dto.UserModel;
import org.example.dto.UserRequestDto;
import org.example.entity.User;
import org.example.exception.EmailAlreadyExistsException;
import org.example.exception.UserNotFoundException;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserModel create(UserRequestDto request) {

        logger.info(
                "Создание пользователя: name={}, email={}, age={}",
                request.getName(),
                request.getEmail(),
                request.getAge()
        );

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    request.getEmail()
            );
        }

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getAge()
        );

        User savedUser = userRepository.save(user);

        logger.info(
                "Пользователь успешно создан: id={}",
                savedUser.getId()
        );

        return toUserModel(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserModel getById(Long id) {

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

        return toUserModel(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserModel> getAll() {

        logger.info("Получение списка всех пользователей");

        List<UserModel> users = userRepository.findAll()
                .stream()
                .map(this::toUserModel)
                .toList();

        logger.info(
                "Получено пользователей: {}",
                users.size()
        );

        return users;
    }

    @Override
    @Transactional
    public UserModel update(
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

        if (userRepository.existsByEmailAndIdNot(
                request.getEmail(),
                id
        )) {
            logger.warn(
                    "Попытка изменить email на уже существующий: {}",
                    request.getEmail()
            );

            throw new EmailAlreadyExistsException(request.getEmail());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());

        User updatedUser = userRepository.save(user);

        logger.info(
                "Пользователь успешно обновлен: id={}",
                updatedUser.getId()
        );

        return toUserModel(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long id) {

        logger.info(
                "Удаление пользователя: id={}",
                id
        );

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);

        logger.info(
                "Пользователь успешно удален: id={}",
                id
        );
    }

    private UserModel toUserModel(User user) {

        return new UserModel(
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
