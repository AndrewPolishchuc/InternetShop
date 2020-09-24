package com.internet.shop.dao.jdbc;

import com.internet.shop.dao.UserDao;
import com.internet.shop.exception.DataProcessingException;
import com.internet.shop.lib.Dao;
import com.internet.shop.model.Role;
import com.internet.shop.model.User;
import com.internet.shop.util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Dao
public class UserDaoJdbcImpl implements UserDao {
    @Override
    public Optional<User> findByLogin(String login) {
        User user = null;
        String query = "SELECT * FROM users WHERE deleted = false AND login = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can not get user from DB with login = " + login, e);
        }
        user.setRoles(getRolesOfUser(user.getId()));
        return Optional.ofNullable(user);
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users(name, login, password) VALUES(?, ?, ?)";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query,
                        Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to create user " + user, e);
        }
        return defineRoles(user);
    }

    @Override
    public Optional<User> getById(Long userId) {
        User user = null;
        String query = "SELECT * FROM users "
                + "INNER JOIN users_roles ON users.user_id = users_roles.user_id "
                + "WHERE users.deleted = false AND users.user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUserFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to create user with ID " + userId, e);
        }
        user.setRoles(getRolesOfUser(user.getId()));
        return Optional.ofNullable(user);
    }

    @Override
    public User update(User user) {
        String query = "UPDATE users SET name = ?, login = ?, password = ? "
                + "WHERE user_id = ? AND deleted = FALSE;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to create user " + user, e);
        }
        deleteRoles(user.getId());
        defineRoles(user);
        user.setRoles(getRolesOfUser(user.getId()));
        return user;
    }

    @Override
    public boolean deleteById(Long userId) {
        String query = "UPDATE users SET deleted = TRUE "
                + "WHERE user_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to delete user( id = " + userId + ")", e);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users "
                + "INNER JOIN users_roles ON users.user_id = users_roles.user_id "
                + "WHERE users.deleted = false;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = getUserFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all user", e);
        }
        for (User user : users) {
            user.setRoles(getRolesOfUser(user.getId()));
        }
        return users;
    }

    private Set<Role> getRolesOfUser(Long userId) {
        String query = "SELECT roles.role_id, role_name FROM roles "
                + "INNER JOIN users_roles ON users_roles.role_id = roles.role_id "
                + "WHERE users_roles.user_id = ?";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            Set<Role> roles = new HashSet<>();
            while (resultSet.next()) {
                roles.add(getRole(resultSet));
            }
            return roles;
        } catch (SQLException e) {
            throw new DataProcessingException("Impossible get roles user with ID -" + userId, e);
        }
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        long userId = resultSet.getLong("user_id");
        String name = resultSet.getString("name");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        return new User(userId, name, login, password);
    }

    private Role getRole(ResultSet resultSet) throws SQLException {
        Long roleId = resultSet.getLong("role_id");
        String roleName = resultSet.getString("role_name");
        Role role = Role.of(roleName);
        role.setId(roleId);
        return role;
    }

    private User defineRoles(User user) {
        String query = "INSERT  INTO users_roles(user_id, role_id) "
                + "VALUES(?,(SELECT role_id "
                + "FROM roles "
                + "WHERE role_name = ?)); ";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            for (Role role : user.getRoles()) {
                statement.setLong(1, user.getId());
                statement.setString(2, role.getRoleName().name());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataProcessingException("User`s - " + user + " roles not updated - ", e);
        }
        user.setRoles(getRolesOfUser(user.getId()));
        return user;
    }

    private boolean deleteRoles(Long userId) {
        String query = "DELETE FROM users_roles "
                + "WHERE user_id = ?;";
        try (Connection connection = ConnectionUtil.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, userId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataProcessingException("Unable to delete user`s( id = " + userId
                    + " roles user with ID = ", e);
        }
    }
}
