package org.example.dao;

import java.util.List;
import java.util.Collections;
import org.example.entity.User;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDaoImpl implements UserDao {

    @Override
    public void save(User user) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.persist(user);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Ошибка при сохранении пользователя", e);
        }
    }

    @Override
    public User findById(Long id) {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(User.class, id);

        } catch (Exception e) {

            logger.error("Ошибка при поиске по id", e);
            return null;
        }
    }

    @Override
    public List<User> findAll() {

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("from User", User.class).list();

        } catch (Exception e) {

            logger.error("Ошибка при поиске всех пользователей", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void update(User user) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.merge(user);

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Ошибка при обновлении пользователя", e);
        }
    }

    @Override
    public void delete(Long id) {

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user != null) {
                session.remove(user);
            }

            transaction.commit();

        } catch (Exception e) {

            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Ошибка при удалении пользователя", e);
        }
    }

    private static final Logger logger =
            LoggerFactory.getLogger(UserDaoImpl.class);
}
