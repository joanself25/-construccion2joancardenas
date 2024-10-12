/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.dao.repositories;

import com.mycompany.app.model.Guest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface GuestRepository extends JpaRepository<Guest, Long>{

    public Optional<Guest> findByUserId_Id(long userId);

    public List<Guest> findByPartnerId_Id(long partnerId);
    
    
}
