package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.Util;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Ксюша", "Корелова", (byte) 29);
        userService.saveUser("Максюша", "Яцко", (byte) 28);
        userService.saveUser("Андрюша", "Кузин", (byte) 28);
        userService.saveUser("Катюша", "Коробкова", (byte) 30);

        userService.removeUserById(1);
        userService.getAllUsers();
        userService.cleanUsersTable();
        userService.dropUsersTable();
        Util.closeSessionFactory();
    }
}
