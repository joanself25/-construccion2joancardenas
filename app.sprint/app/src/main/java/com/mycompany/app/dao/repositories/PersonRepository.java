/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


 // INTERFAZ DE SPRINTBOT PARA REALIZAR OPERACIONES 
/*
 JPAREPOSITORY
 Simplifica la interacción 
con la base de datos, ya que  permite realizar consultas y operaciones
sin escribir sql directamente
*/
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    //Person es una entidad que define unas propiedades
    // Y EL LONG ESPECIFICA EL TIPO DE CLAVE PRIMARIA DE LA ENTIDAD

    public Person findByDocument(Long document);

    public boolean existsByDocument(Long document);

}
