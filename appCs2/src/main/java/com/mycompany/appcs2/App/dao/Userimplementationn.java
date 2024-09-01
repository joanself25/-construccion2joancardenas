package com.mycompany.appcs2.App.dao;

import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.Helpers.Helpers;
import com.mycompany.appcs2.App.config.MYSQLConnection;
import com.mycompany.appcs2.App.model.Person;
import com.mycompany.appcs2.App.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.appcs2.App.dao.interfaces.UserDao;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Userimplementationn implements UserDao {

    @Override
    public void createUser(UserDTO userDto) throws Exception {
        String query = "INSERT INTO user (PERSONNID, USERNAME, PASSWORD, ROLE) VALUES (?, ?, ?, ?)";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            // First, create the person
            long personId = createPerson(userDto.getPersonId());

            preparedStatement.setLong(1, personId);
            preparedStatement.setString(2, userDto.getUsername());
            preparedStatement.setString(3, userDto.getPassword());
            preparedStatement.setString(4, userDto.getRol());

            int affected = preparedStatement.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    userDto.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al crear el usuario: " + e.getMessage(), e);
        }
    }

    private long createPerson(Person person) throws SQLException {
        String query = "INSERT INTO person (DOCUMENT, NAME, CELLPHONE) VALUES (?, ?, ?)";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setLong(1, person.getCedula());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setLong(3, person.getCelphone());

            int affected = preparedStatement.executeUpdate();

            if (affected == 0) {
                throw new SQLException("Creating person failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("Creating person failed, no ID obtained.");
                }
            }
        }
    }

    @Override
    public UserDTO findByUserName(UserDTO userDto) throws Exception {
        String query = "SELECT u.ID, u.PERSONNID, u.USERNAME, u.PASSWORD, u.ROLE, "
                + "p.DOCUMENT, p.NAME, p.CELLPHONE "
                + "FROM user u JOIN person p ON u.PERSONNID = p.ID WHERE u.USERNAME = ?";
        try (PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, userDto.getUsername());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al buscar usuario por nombre de usuario", e);
        }
        return null;
    }

    @Override
    public UserDTO findUserById(long userId) throws Exception {
        String sql = "SELECT * FROM user WHERE ID = ?";
        try (Connection conn = MYSQLConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setId(rs.getLong("ID"));
                    userDTO.setUsername(rs.getString("USERNAME"));
                    userDTO.setPassword(rs.getString("PASSWORD")); // Asegúrate de manejar la contraseña con seguridad
                    userDTO.setRol(rs.getString("ROLE"));
                    return userDTO;
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al encontrar usuario por ID: " + e.getMessage(), e);
        }
        return null;
    }

    // Otros métodos del UserDao
    @Override
    public boolean existsByUserName(UserDTO userDto) throws Exception {
        String query = "SELECT 1 FROM user WHERE USERNAME = ?";
        try (PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, userDto.getUsername());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new Exception("Error al verificar la existencia del usuario", e);
        }
    }

    @Override
    public void updateUser(UserDTO userDto) throws Exception {
        String query = "UPDATE user SET USERNAME = ?, PASSWORD = ?, ROLE = ? WHERE ID = ?";
        try (PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, userDto.getUsername());
            preparedStatement.setString(2, userDto.getPassword());
            preparedStatement.setString(3, userDto.getRol());
            preparedStatement.setLong(4, userDto.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al actualizar el usuario", e);
        }
    }

    public void deleteUser(Long userId) throws Exception {
        String query = "DELETE FROM user WHERE ID = ?";
        try (PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Error al eliminar el usuario", e);
        }
    }

    public UserDTO findById(Long userId) throws Exception {
        String query = "SELECT u.ID, u.PERSONNID, u.PASSWORD, u.`USERNAME`, u.`ROLE`, "
                + "p.DOCUMENT, p.NAME, p.CELLPHONE "
                + "FROM user u JOIN person p ON u.PERSONNID = p.ID WHERE u.ID = ?";
        try (PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query)) {
            preparedStatement.setLong(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return extractUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al buscar usuario por ID", e);
        }
        return null;
    }

    private UserDTO extractUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("ID"));
        user.setUsername(resultSet.getString("USERNAME"));
        user.setPassword(resultSet.getString("PASSWORD"));
        user.setRol(resultSet.getString("ROLE"));

        Person person = new Person();
        person.setId(resultSet.getLong("PERSONNID"));
        person.setCedula(resultSet.getLong("DOCUMENT"));
        person.setName(resultSet.getString("NAME"));
        person.setCelphone(resultSet.getLong("CELLPHONE"));

        user.setpersondId(person);
        return Helpers.parse(user);
    }

    private long createPerson(PersonDTO personId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
