package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        Session session = Util.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.createSQLQuery("CREATE TABLE IF NOT EXISTS Users (id BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(45) NOT NULL, lastName VARCHAR(45) NOT NULL, age TINYINT NOT NULL, PRIMARY KEY (id))")
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка создания таблицы", e);
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = Util.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS Users").executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка удаления таблицы", e);
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = Util.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw new HibernateException("Ошибка при добавлении пользователя", e);
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = Util.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw new HibernateException("Ошибка при удалении пользователя по id", e);
        }

    }

    @Override
    public List<User> getAllUsers() {
        Session session = Util.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            List <User> allUsers = session.createQuery("FROM User", User.class)
                    .getResultList();
            session.getTransaction().commit();
            return allUsers;
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw new HibernateException("Ошибка при формировании списка всех пользователей", e);
        }
    }

    @Override
    public void cleanUsersTable() {
        Session session = Util.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE FROM User")
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            session.getTransaction().rollback();
            throw new HibernateException("Ошибка при удалении всех пользователей", e);
        }
    }
}
