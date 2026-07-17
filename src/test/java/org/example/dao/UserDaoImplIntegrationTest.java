package org.example.dao;

import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class UserDaoImplIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("testdb")
                    .withUsername("postgres")
                    .withPassword("postgres");

    private UserDaoImpl dao;

    @BeforeAll
    static void beforeAll() {

        Configuration configuration = new Configuration();

        configuration.configure("hibernate.cfg.xml");

        configuration.setProperty(
                "hibernate.connection.url",
                postgres.getJdbcUrl());

        configuration.setProperty(
                "hibernate.connection.username",
                postgres.getUsername());

        configuration.setProperty(
                "hibernate.connection.password",
                postgres.getPassword());

        configuration.setProperty(
                "hibernate.hbm2ddl.auto",
                "create-drop");

        SessionFactory sessionFactory =
                configuration.buildSessionFactory();

        HibernateUtil.setSessionFactory(sessionFactory);
    }

    @BeforeEach
    void setUp() {

        dao = new UserDaoImpl();
    }

    @AfterEach
    void cleanDatabase() {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        session.createMutationQuery("delete from User").executeUpdate();

        transaction.commit();
        session.close();
    }

    @Test
    void save_ShouldSaveUser() {

        User user = new User(
                "Bob",
                "bob@test.com",
                20,
                LocalDateTime.now());

        dao.save(user);

        assertNotNull(user.getId());
    }

    @Test
    void findById_ShouldReturnUser() {

        User user = new User(
                "Alex",
                "alex@test.com",
                25,
                LocalDateTime.now());

        dao.save(user);

        User result = dao.findById(user.getId());

        assertNotNull(result);

        assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void findAll_ShouldReturnUsers() {

        dao.save(new User(
                "One",
                "one@test.com",
                20,
                LocalDateTime.now()));

        dao.save(new User(
                "Two",
                "two@test.com",
                21,
                LocalDateTime.now()));

        assertTrue(dao.findAll().size() >= 2);
    }

    @Test
    void update_ShouldUpdateUser() {

        User user = new User(
                "Bob",
                "bob@test.com",
                20,
                LocalDateTime.now());

        dao.save(user);

        user.setAge(50);

        dao.update(user);

        User updated = dao.findById(user.getId());

        assertEquals(50, updated.getAge());
    }

    @Test
    void delete_ShouldDeleteUser() {

        User user = new User(
                "Bob",
                "delete@test.com",
                20,
                LocalDateTime.now());

        dao.save(user);

        dao.delete(user.getId());

        assertNull(dao.findById(user.getId()));
    }
}
