package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private final Connection connection;

    private static final String createUsersQuery = "CREATE TABLE IF NOT EXISTS user (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(45), lastName VARCHAR(45), age INT)";

    public UserDaoJDBCImpl() {
        Util util = new Util();
        connection = util.getConnection();
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createUsersQuery);//todo: на боевом коде ..выносятся из тела метода
            System.out.println("[SERVICE] Таблица user создана");
        } catch (Exception e) {
            System.out.println("[WARNING] Ошибка при создании таблицы" + e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS user");
            System.out.println("[SERVICE] Таблица user удалена");
        } catch (Exception e) {
            System.out.println("[WARNING] Ошибка при удалении таблицы" + e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Statement statement = connection.createStatement()) {
            String commands = String.format("INSERT INTO user (name, lastName, age) VALUES ('%s','%s', %d)", name, lastName, age);
            statement.executeUpdate(commands);
            System.out.println("[SERVICE] User с именем – " + name + " добавлен в базу данных ");
        } catch (Exception e) {
            System.out.println("[WARNING] Ошибка при сохранении" + e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = connection.createStatement()) {
            String commands = String.format("DELETE FROM user WHERE id = %d", id);
            statement.executeUpdate(commands);
            System.out.println("[SERVICE] User с id – " + id + " удален ");
        } catch (Exception e) {
            System.out.println("[WARNING] Ошибка при удалении пользователя по id" + e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> listUser = new ArrayList<>();
        try (ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM user")) {
            while (resultSet.next()) {
                User use = new User(resultSet.getString("name"),
                        resultSet.getString("lastNAme"),
                        resultSet.getByte("age"));
                listUser.add(use);
            }
        } catch (Exception e) {
            System.out.println("[WARNING] Ошибка при получении всех пользователей из таблицы: " + e.getMessage());
        }
        return listUser;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE user");
            System.out.println("[SERVICE] Таблица очищена");
        } catch (Exception e) {
            System.out.println("[WARNING] Ошибка при очистке таблицы: " + e.getMessage());
        }
    }
}
