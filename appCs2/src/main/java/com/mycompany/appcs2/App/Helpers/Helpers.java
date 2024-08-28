/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.Helpers;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.model.Person;
import com.mycompany.appcs2.App.model.User;

public abstract class Helpers {

    public static PersonDTO parse(Person person) {
        PersonDTO personDto = new PersonDTO();
        personDto.setId(person.getId());
        personDto.setCedula(person.getCedula());
        personDto.setName(person.getName());
        personDto.setCelphone(person.getCelphone());
        return personDto;
    }

    public static Person parse(PersonDTO personDto) {
        Person person = new Person();
        person.setId(personDto.getId());
        person.setCedula(personDto.getCedula());
        person.setName(personDto.getName());
        person.setCelphone(personDto.getCelphone());
        return person;
    }

    public static UserDTO parse(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        userDto.setPersonId(parse(user.getPersonId()));
        userDto.setRol(user.getRol());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    public static User parse(UserDTO userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setpersondId(parse(userDto.getPersonId()));
        user.setRol(userDto.getRol());
        user.setUsername(userDto.getUsername());
        return user;
    }
}
