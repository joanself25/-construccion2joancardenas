package com.mycompany.appcs2.App.dao;

import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.Helpers.Helpers;
import com.mycompany.appcs2.App.config.MYSQLConnection;
import com.mycompany.appcs2.App.dao.interfaces.Userdao;
import com.mycompany.appcs2.App.model.Person;
import com.mycompany.appcs2.App.model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Userimplementationn implements Userdao {

    @Override

    public UserDTO findByUserName(UserDTO userDto) throws Exception {
        String query = "SELECT ID,PERSONID,PASSWORD,USERNAME,ROLE FROM USER WHERE USERNAME = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, userDto.getUsername());
        ResultSet resulSet = preparedStatement.executeQuery();
        if (resulSet.next()) {
            User user = new User();
            user.setId(resulSet.getLong("ID"));
            Person person = new Person();
            person.setId(resulSet.getLong("PERSONID"));

            user.setPassword(resulSet.getString("PASSWORD"));
            user.setRol(resulSet.getString("USERNAME"));
            user.setpersondId(person);
            resulSet.close();
            preparedStatement.close();
            return Helpers.parse(user);

        }
        resulSet.close();
        preparedStatement.close();
        return null;

    }

    @Override

    public boolean existsByUserName(UserDTO userDto) throws Exception {
        String query = "SELECT 1 FROM USER WHERE USERNAME = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, userDto.getUsername());
        ResultSet resulSet = preparedStatement.executeQuery();
        boolean exists = resulSet.next();
        resulSet.close();
        preparedStatement.close();
        return exists;

    }

    @Override
    public void createUser(UserDTO userDto) throws Exception {
        User user = Helpers.parse(userDto);
        String query = "INSERT INTO USER(USERNAME,PASSWORD,PERSONID,ROLE) VALUES (?,?,?,?) ";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setLong(3, user.getPersonId().getId());
        preparedStatement.setString(4, user.getRol());
        preparedStatement.execute();
        preparedStatement.close();
    }

}
