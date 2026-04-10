package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.swing.plaf.nimbus.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {

    }
    private final Connection connection;
    {
        try {
            connection = Util.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания соединения", e);
        }
    }

    public void createUsersTable() {
        try (Statement st = connection.createStatement()) {
            st.execute("CREATE TABLE IF NOT EXISTS Users (id BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(45) NOT NULL, lastName VARCHAR(45) NOT NULL, age TINYINT NOT NULL, PRIMARY KEY (id))");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Откат операции");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void dropUsersTable() {
        try (Statement st = connection.createStatement()) {
            st.execute("DROP TABLE IF EXISTS Users");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Ошибка при удалении таблицы");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        final String SAVE_USER = "INSERT INTO Users (name, lastName, age) VALUES (?, ?, ?)";
        try (PreparedStatement pr = connection.prepareStatement(SAVE_USER)) {
            pr.setString(1, name);
            pr.setString(2, lastName);
            pr.setByte(3, age);
            pr.executeUpdate();
            System.out.println("User с именем - " + name + " добавлен в базу данных");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Ошибка при добавлении пользователя");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void removeUserById(long id) {
        final String REMOVE_BY_ID = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement pr = connection.prepareStatement(REMOVE_BY_ID)) {
            pr.setLong(1, id);
            pr.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Ошибка при удалении пользователя по ID");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try (Statement st = connection.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT * FROM Users");
            while(rs.next()) {
                User user = new User();
                user.setId(rs.getLong("id"));
                user.setName((rs.getString("name")));
                user.setLastName(rs.getString("lastName"));
                user.setAge(rs.getByte("age"));
                allUsers.add(user);
                connection.commit();
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Ошибка при получении списка пользователей");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        return allUsers;
    }

    public void cleanUsersTable() {
        try (Statement st = connection.createStatement()) {
            st.execute("DELETE FROM Users");
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
                System.out.println("Ошибка при очистке таблицы");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDaoJDBCImpl that = (UserDaoJDBCImpl) o;
        return Objects.equals(connection, connection);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(connection);
    }
}
