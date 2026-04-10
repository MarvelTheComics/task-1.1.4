package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Property;
import org.hibernate.cfg.Environment ;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private final static String DRIVER = "com.mysql.cj.jdbc.Driver";
    private final static String URL = "jdbc:mysql://localhost:3306/mydb";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "root";
    private static Connection connection;
    private static final SessionFactory sessionFactory = buildSessionFactory();

////      Реализация для работы через @JDBC
    public static Connection getConnection() {

        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Соединение установлено");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Соединение не установлено", e);
        }
        return connection;
    }

////      Реализация для работы через @Hibernate
    public static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();
            Properties settings = new Properties();
            settings.put(Environment.URL, URL);
            settings.put(Environment.DRIVER, DRIVER);
            settings.put(Environment.USER, USERNAME);
            settings.put(Environment.PASS, PASSWORD);
            settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
            settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
            settings.put(Environment.SHOW_SQL, "true");
            settings.put(Environment.HBM2DDL_AUTO, "");

            return configuration.setProperties(settings)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка при создании фабрики сессий", e);
        } finally {
            System.out.println("Соединение установлено");
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение закрыто");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Соединение не закрыто", e);
        }
    }

    public static void closeSessionFactory() {
        try {
            if (sessionFactory != null && !sessionFactory.isClosed()) {
                sessionFactory.close();
                System.out.println("Соединение закрыто");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Соединение не закрыто", e);
        }
    }

}
