package org.example.service;

import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.entity.User;
import org.example.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl() {
        this(new UserDaoImpl());
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void createUser(User user) {
        validate(user);

        logger.info("Создание пользователя {}", user.getEmail());
        userDao.save(user);
        logger.info("Пользователь успешно создан.");
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Поиск пользователя {}", id);
        return userDao.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("Поиск всех пользователей");
        return userDao.findAll();
    }

    @Override
    public void updateUser(User user) {
        logger.info("Обновление пользователя {}", user.getId());
        userDao.update(user);
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Удаление пользователя {}", id);
        userDao.delete(id);
    }

    private void validate(User user) {

        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("Имя не может быть пустым.");
        }

        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("Email не может быть пустым.");
        }

        if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new ValidationException("Некорректный email.");
        }

        if (user.getAge() == null || user.getAge() < 0 || user.getAge() > 120) {
            throw new ValidationException("Возраст должен быть от 0 до 120.");
        }
    }

    private static final Logger logger =
            LoggerFactory.getLogger(UserServiceImpl.class);
}
