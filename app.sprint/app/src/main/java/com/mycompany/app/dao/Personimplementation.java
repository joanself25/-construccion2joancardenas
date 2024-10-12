package com.mycompany.app.dao;

import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.interfaces.PersonDao;
import com.mycompany.app.model.Person;
import com.mycompany.app.dao.repositories.PersonRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Setter
@Getter

public class Personimplementation implements PersonDao {

    @Autowired
    PersonRepository personRepository;

    @Override
    public void createPerson(PersonDTO personDto) throws Exception {
        Person person = Helpers.parse(personDto);
        personRepository.save(person);
        personDto.setId(person.getId());
    }

    @Override
    public boolean existsByDocument(PersonDTO personDto) throws Exception {
        return personRepository.existsByDocument(Helpers.parse(personDto).getDocument());

    }

    @Override
    public void deletePerson(PersonDTO personDto) throws Exception {
        Person person = Helpers.parse(personDto);
        personRepository.delete(person);

    }

    @Override
    public PersonDTO findByDocument(PersonDTO personDto) throws Exception {
        Person person = personRepository.findByDocument(personDto.getDocument());
        return Helpers.parse(person);

    }

}
