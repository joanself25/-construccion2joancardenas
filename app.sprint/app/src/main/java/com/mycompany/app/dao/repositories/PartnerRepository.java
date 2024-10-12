/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.dao.repositories;

// INTERFAZ DE SPRINTBOT PARA REALIZAR OPERACIONES 
import com.mycompany.app.model.Partner;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
 JPAREPOSITORY
 Simplifica la interacción 
con la base de datos, ya que  permite realizar consultas y operaciones
sin escribir sql directamente
 */
@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    //Partner es el tipo de entidad con la que se esta trabajando y que define unas propiedades
    // Y EL LONG ESPECIFICA EL TIPO DE CLAVE PRIMARIA DE LA ENTIDAD

    List<Partner> findByTypeSuscription(String type);

    public long countByTypeSuscription(String type);

    public Optional<Partner> findByUserId_Id(long userId);

}
