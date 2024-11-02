/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.dao.repositories;

import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.model.Guest;
import com.mycompany.app.model.Invoice;
import com.mycompany.app.model.Partner;
import com.mycompany.app.model.Person;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    List<Invoice> findByPartnerIdAndStatus(Partner partnerId, boolean status);

    // Buscar facturas por persona
    List<Invoice> findByPersonId(Person personId);

    // Encuentra facturas creadas después de una fecha determinada
    List<Invoice> findByCreationDateAfter(java.sql.Date date);

    // Buscar facturas con un importe mayor a un valor especificado
    List<Invoice> findByAmount(double amount);

    public List<Invoice> findByPartnerId_Id(long partnerId);

    public List<Invoice> findByPersonId_Id(long guestId);

    public List<Invoice> findByStatus(boolean status);

    public List<Invoice> findByPersonId_IdAndStatusTrue(long id);

    List<Invoice> findByPersonId_IdAndStatusTrue(Long personId);

    List<Invoice> findByPersonId_IdAndStatusFalse(Long personId);

    List<Invoice> findByPersonId_Id(Long personId);

    public List<Invoice> findByPartnerId_IdAndStatusFalse(long partnerId);
    

}
