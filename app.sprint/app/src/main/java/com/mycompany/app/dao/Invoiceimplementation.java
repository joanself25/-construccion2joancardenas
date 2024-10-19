/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.dao;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.app.model.Invoice;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.dao.interfaces.InvoiceDao;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.InvoiceRepository;
import com.mycompany.app.dao.repositories.PartnerRepository;
import com.mycompany.app.model.Partner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Setter
@Getter
public class Invoiceimplementation implements InvoiceDao {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    PartnerRepository partnerRepository;

    @Override
    public void createInvoice(InvoiceDTO invoiceDto) throws Exception {
        Invoice invoice = Helpers.parse(invoiceDto);
        invoiceRepository.save(invoice);
    }

    @Override
    public long createAllInvoices(InvoiceDTO invoiceDto) throws Exception {
        Invoice invoice = Helpers.parse(invoiceDto);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return savedInvoice.getId();
    }

    @Override
    // el metodo devuelve una lista de objetos  invoiceDTO
    // recibe un  parametro de tipo long partenerid
    
    public List<InvoiceDTO> getInvoicesPartner(long partnerId) throws Exception {
        //creo una variable de tipo lista con un ibjeto invoices donde a esta variable 
        //por medio de un invoices repository me va a buscar las facturas que le pertenecen 
        //al id del socio que se busca
        List<Invoice> invoices = invoiceRepository.findByPartnerId_Id(partnerId);
        List<InvoiceDTO> invoiceDTOs = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceDTOs.add(Helpers.parse(invoice));
        }
        return invoiceDTOs;
    }

    @Override
    public List<InvoiceDTO> getInvoicesByGuestId(long guestId) throws Exception {
        List<Invoice> invoices = invoiceRepository.findByPersonId_Id(guestId);
        List<InvoiceDTO> invoiceDTOs = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceDTOs.add(Helpers.parse(invoice));
        }
        return invoiceDTOs;
    }

    @Override
    public InvoiceDTO findInvoiceById(long invoiceId) throws Exception {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            return Helpers.parse(optionalInvoice.get());
        } else {
            throw new Exception("Invoice no encontrado con ID: " + invoiceId);
        }
    }

    @Override
    //actualizar Factura

    public void updateInvoice(InvoiceDTO invoiceDto) throws Exception {
        if (!invoiceRepository.existsById(invoiceDto.getId())) {
            throw new Exception("Invoice no encontrado con ID: " + invoiceDto.getId());
        }
        Invoice invoice = Helpers.parse(invoiceDto);
        invoiceRepository.save(invoice);
    }

    @Override
    public List<InvoiceDTO> getInvoicesByStatus(boolean status) throws Exception {
        List<Invoice> invoices = invoiceRepository.findByStatus(status);
        List<InvoiceDTO> invoiceDTOs = new ArrayList<>();
        for (Invoice invoice : invoices) {
            invoiceDTOs.add(Helpers.parse(invoice));
        }
        return invoiceDTOs;
    }

    @Override
    public void deleteAllInvoicesByPartnerId(long partnerId) throws Exception {
        List<Invoice> invoices = invoiceRepository.findByPartnerId_Id(partnerId);
        // se implementa  la validación que lanza excepción si no hay facturas
        // Si no hay facturas, simplemente no hacemos nada
        if (!invoices.isEmpty()) {
            invoiceRepository.deleteAll(invoices);
        }
    }

    @Override
    public List<InvoiceDTO> UnpaidInvoicesPartner(long partnerId) throws Exception {
        List<Invoice> unpaidInvoices = invoiceRepository.findByPartnerId_IdAndStatusFalse(partnerId);
        List<InvoiceDTO> unpaidInvoiceDTOs = new ArrayList<>();
        for (Invoice invoice : unpaidInvoices) {
            unpaidInvoiceDTOs.add(Helpers.parse(invoice));
        }
        return unpaidInvoiceDTOs;
    }

    //Facturas no pagadas Invitado
    @Override
    public List<InvoiceDTO> UnpaidInvoicesGuest(long guestId) throws Exception {
        List<Invoice> unpaidInvoices = invoiceRepository.findByPersonId_IdAndStatusFalse(guestId);
        List<InvoiceDTO> unpaidInvoiceDTOs = new ArrayList<>();
        for (Invoice invoice : unpaidInvoices) {
            unpaidInvoiceDTOs.add(Helpers.parse(invoice));
        }
        return unpaidInvoiceDTOs;
    }

    @Override
    public void deleteAllInvoicesByGuestId(long guestId) throws Exception {
        List<Invoice> invoices = invoiceRepository.findByPersonId_Id(guestId);
        if (invoices.isEmpty()) {
            throw new Exception(" factura no encontrado con id:" + guestId);
        }
        invoiceRepository.deleteAll(invoices);
    }

    @Override
    public void payInvoices(long partnerId) throws Exception {
        List<Invoice> unpaidInvoices = invoiceRepository.findByPartnerId_IdAndStatusFalse(partnerId);
        if (unpaidInvoices.isEmpty()) {
            throw new Exception("No hay facturas pendientes de pago para este socio");
        }

        double totalAmount = 0;
        for (Invoice invoice : unpaidInvoices) {
            totalAmount += invoice.getAmount();
        }

        // Obtener el socio y verificar fondos
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (!optionalPartner.isPresent()) {
            throw new Exception("Socio no encontrado");
        }

        Partner partner = optionalPartner.get();
        if (partner.getFundsMoney() < totalAmount) {
            throw new Exception("Fondos insuficientes. Total a pagar: $" + totalAmount
                    + ", Fondos disponibles: $" + partner.getFundsMoney());
        }

        // Actualizar el estado de las facturas y los fondos del socio
        for (Invoice invoice : unpaidInvoices) {
            invoice.setStatus(true);
            invoiceRepository.save(invoice);
        }

        partner.setFundsMoney(partner.getFundsMoney() - totalAmount);
        partnerRepository.save(partner);
    }

    // Helper method to map ResultSet to Invoice object (unchanged)
    /* private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setId(rs.getLong("ID"));

        Person person = new Person();
        person.setId(rs.getLong("PERSONID"));
        invoice.setPersonId(person);

        Partner partner = new Partner();
        partner.setId(rs.getLong("PARTNERID"));
        invoice.setPartnerId(partner);

        invoice.setCreationDate(new Date(System.currentTimeMillis()));
        invoice.setAmount(rs.getDouble("AMOUNT"));
        invoice.setStatus("PAID".equals(rs.getString("STATUS")));

        return invoice;
    
}
     */
}

// select busca 
// insertar
// update buscar
// delete eliminiar 

