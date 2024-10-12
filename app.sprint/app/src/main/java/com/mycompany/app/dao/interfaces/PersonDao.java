package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.PersonDTO;

public interface PersonDao {

    public boolean existsByDocument(PersonDTO personDto) throws Exception;

    public void createPerson(PersonDTO personDTo) throws Exception;

    public void deletePerson(PersonDTO personDto) throws Exception;

    public PersonDTO findByDocument(PersonDTO personDto) throws Exception;

  
   


   

   

  
}
