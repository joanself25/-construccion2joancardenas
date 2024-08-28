package com.mycompany.appcs2.App.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import com.mycompany.appcs2.App.config.MYSQLConnection;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.dao.interfaces.PersonDao;
import com.mycompany.appcs2.App.model.Person;
import com.mycompany.appcs2.App.Helpers.Helpers;

public class Personimplementation implements PersonDao {

    @Override
    public boolean existsByDocument(PersonDTO personDto) throws Exception {
        String query = "SELECT 1 FROM PERSON WHERE DOCUMENT = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, personDto.getCedula());
        ResultSet resulSet = preparedStatement.executeQuery();
        boolean exists = resulSet.next();
        resulSet.close();
        preparedStatement.close();
        return exists;

    }

    @Override
    public void createPerson(PersonDTO personDto) throws Exception {
        Person person = Helpers.parse(personDto);
        String query = "INSERT INTO PERSON(NAME,DOCUMENT,CELPHONE) VALUES (?,?,?) ";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, person.getName());
        preparedStatement.setLong(2, person.getCedula());
        preparedStatement.setLong(3, person.getCelphone());
        preparedStatement.execute();
        preparedStatement.close();
    }

    @Override

    public void deletePerson(PersonDTO personDto) throws Exception {
        Person person = Helpers.parse(personDto);
        String query = "DELETE FROM PERSON WHERE DOCUMENT = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, person.getCelphone());
        preparedStatement.execute();
        preparedStatement.close();

    }

    @Override

    public PersonDTO findByDocument(PersonDTO personDto) throws Exception {
        String query = "SELECT ID,NAME,DOCUMENT,CELPHONE FROM PERSON WHERE DOCUMENT = ?";
        PreparedStatement preparedStatement = MYSQLConnection.getConnection().prepareStatement(query);
        preparedStatement.setLong(1, personDto.getCedula());
        ResultSet resulSet = preparedStatement.executeQuery();
        if (resulSet.next()) {
            Person person = new Person();
            person.setId(resulSet.getLong("ID"));
            person.setName(resulSet.getString("NAME"));
            person.setCedula(resulSet.getLong("DOCUMENT"));
            person.setCelphone(resulSet.getLong("CELPHONE"));
            resulSet.close();
            preparedStatement.close();
            return Helpers.parse(person);
        }
        resulSet.close();
        preparedStatement.close();

        return null;

    }

}
