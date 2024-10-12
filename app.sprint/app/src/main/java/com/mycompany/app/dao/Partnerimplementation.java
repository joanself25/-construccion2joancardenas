/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.dao;

import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.Dto.UserDTO;
import com.mycompany.app.dao.interfaces.PartnerDao;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.GuestRepository;
import com.mycompany.app.dao.repositories.InvoiceRepository;
import com.mycompany.app.dao.repositories.PartnerRepository;
import com.mycompany.app.model.Guest;
import com.mycompany.app.model.Invoice;
import com.mycompany.app.model.Partner;
import com.mycompany.app.model.User;
import java.sql.Date;
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

public class Partnerimplementation implements PartnerDao {

    @Autowired
    GuestRepository guestRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    PartnerRepository partnerRepository;

    @Override
    public void createPartner(PartnerDTO partnerDTO, UserDTO userDTO) throws Exception {
        Partner partner = new Partner();
        partner.setUserId(Helpers.parse(userDTO));
        partner.setFundsMoney(50000); // Initial fund for regular partners
        partner.setTypeSuscription("regular");
        partner.setDateCreated(new Date(System.currentTimeMillis()));
        partnerRepository.save(partner);
        partnerDTO.setId(partner.getId());

    }

    @Override
    public void PartnerFunds(PartnerDTO partnerDto) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerDto.getId());
        if (!optionalPartner.isPresent()) {
            throw new Exception("Partner not found with id: " + partnerDto.getId());
        }
        Partner partner = optionalPartner.get();
        partner.setFundsMoney(partnerDto.getfundsMoney());
        partnerRepository.save(partner);
    }

    @Override
    public void updatePartner(PartnerDTO partnerDto) throws Exception {
        if (!partnerRepository.existsById(partnerDto.getId())) {
            throw new Exception("Partner no encontrado con el id : " + partnerDto.getId());
        }

        // Asegurarse de que typeSuscription tenga un valor válido
        if (partnerDto.getTypeSuscription() == null) {
            throw new Exception("El tipo de suscripción no puede estar vacío");
        }

        Partner partner = Helpers.parse(partnerDto);

        // Verificar que todos los campos requeridos estén establecidos
        if (partner.getTypeSuscription() == null || partner.getUserId() == null || partner.getDateCreated() == null) {
            throw new Exception("Todos los campos requeridos deben estar establecidos");
        }

        partnerRepository.save(partner);
    }

    @Override
    public PartnerDTO findPartnerByUserId(long userId) throws Exception {
        System.out.println("  buscando socio con el ID del usuario   " + userId);
        // me busca por medio del userid el id del socio
        //para hacer la baja o soliitar promocion

        Optional<Partner> optionalPartner = partnerRepository.findByUserId_Id(userId);
        if (optionalPartner.isPresent()) {
            System.out.println("Socio encontrado con ID: " + optionalPartner.get().getId());
            return Helpers.parse(optionalPartner.get());
        } else {
            System.out.println("No se encontró socio para el ID de usuario: " + userId);
            return null;
        }
    }

    @Override
    public PartnerDTO findPartnerById(long partnerId) throws Exception {
        // Metodo que realiza en la service el  proceso de las reglas de negocio
        // y verifica si cumple o no cumple  con estas reglas
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (optionalPartner.isPresent()) {
            System.out.println("Socio encontrado con ID: " + partnerId);
            return Helpers.parse(optionalPartner.get());
        } else {
            System.out.println("No se encontró socio con ID: " + partnerId);
            return null;
        }
    }

    @Override
    public void lowPartner(long partnerId) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (!optionalPartner.isPresent()) {
            throw new Exception("Socio no encontrado con id: " + partnerId);
        }

        Partner partner = optionalPartner.get();

        // Aquí puedes agregar lógica adicional si es necesario, como verificar facturas pendientes
        partnerRepository.delete(partner);
        System.out.println("Socio con ID " + partnerId + " ha sido dado de baja exitosamente.");
    }

    //Hay ranuras VIP disponibles
    @Override
    public boolean isVIPSlotAvailable() throws Exception {
        long vipCount = partnerRepository.countByTypeSuscription("VIP");
        return vipCount < 5;
    }
    //obtener Solicitudes VIP Pendientes

    @Override
    public List<PartnerDTO> getPendingVIPRequests() throws Exception {
        List<Partner> pendingPartners = partnerRepository.findByTypeSuscription("vip_pendiente");
        List<PartnerDTO> pendingRequests = new ArrayList<>();

        for (Partner partner : pendingPartners) {
            PartnerDTO partnerDTO = Helpers.parse(partner);

            User user = partner.getUserId();
            if (user != null) {
                UserDTO userDTO = Helpers.parse(user);
                partnerDTO.setUserId(userDTO);
            }
            pendingRequests.add(partnerDTO);
        }

        return pendingRequests;
    }

    /* @Override
    public PartnerDTO findPartnerByUserId(long userId) throws Exception {

        Optional<Partner> optionalPartner = partnerRepository.findByUserId_Id(userId);
        if (optionalPartner.isPresent()) {
            System.out.println("Socio encontrado: " + optionalPartner.get().getId());
            return Helpers.parse(optionalPartner.get());
        } else {

            throw new Exception("Socio no encontrado para el ID de usuario: " + userId);
        }
    }
     */
    @Override
    public void deletePartner(long partnerId) throws Exception {
        if (!partnerRepository.existsById(partnerId)) {
            throw new Exception("Partner no encontrado con ID: " + partnerId);
        }
        partnerRepository.deleteById(partnerId);
    }

    //  obtener facturas pendientes 
    @Override
    public List<InvoiceDTO> getPendingInvoices(long partnerId) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (optionalPartner.isPresent()) {
            Partner partner = optionalPartner.get();
            List<Invoice> pendingInvoices = invoiceRepository.findByPartnerIdAndStatus(partner, false);
            List<InvoiceDTO> pendingInvoiceDTOs = new ArrayList<>();
            for (Invoice invoice : pendingInvoices) {
                InvoiceDTO invoiceDTO = Helpers.parse(invoice);
                pendingInvoiceDTOs.add(invoiceDTO);
            }
            return pendingInvoiceDTOs;
        } else {
            throw new Exception("Partner no encontrado con ID: " + partnerId);
        }
    }

    //Solicitud de promoción VIP
    @Override
    public void requestVIPPromotion(long partnerId) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (optionalPartner.isPresent()) {
            Partner partner = optionalPartner.get();
            partner.setTypeSuscription("vip_pendiente ");
            partner.setDateCreated(new Date(System.currentTimeMillis()));
            partnerRepository.save(partner);
        } else {
            throw new Exception("Partner no encontrado con ID: " + partnerId);
        }
    }

    @Override
    public void uploadFunds(long partnerId, double amount) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (optionalPartner.isPresent()) {
            Partner partner = optionalPartner.get();
            partner.setFundsMoney(partner.getFundsMoney() + amount);
            partnerRepository.save(partner);
        } else {

            throw new Exception("Partner no encontrado con ID: " + partnerId);
        }
    }

    @Override
    public void updatePartnerSubscription(long partnerId, String newType) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (optionalPartner.isPresent()) {
            Partner partner = optionalPartner.get();
            partner.setTypeSuscription(newType);
            partnerRepository.save(partner);
        } else {
            throw new Exception("Partner no encontrado con ID: " + partnerId);
        }
    }

    @Override
    public void payInvoice(long invoiceId) throws Exception {
        Optional<Invoice> optionalInvoice = invoiceRepository.findById(invoiceId);
        if (optionalInvoice.isPresent()) {
            Invoice invoice = optionalInvoice.get();
            invoice.setStatus(true);
            invoiceRepository.save(invoice);
        } else {

            throw new Exception("invoice no encontrado con id: " + invoiceId);
        }
    }

    //Obtener facturas pagadas
    @Override
    public List<InvoiceDTO> getPaidInvoices(long partnerId) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (optionalPartner.isPresent()) {
            Partner partner = optionalPartner.get();
            List<Invoice> paidInvoices = invoiceRepository.findByPartnerIdAndStatus(partner, true);
            List<InvoiceDTO> paidInvoiceDTOs = new ArrayList<>();
            for (Invoice invoice : paidInvoices) {
                InvoiceDTO invoiceDTO = Helpers.parse(invoice);
                paidInvoiceDTOs.add(invoiceDTO);
            }
            return paidInvoiceDTOs;
        } else {
            throw new Exception("Partner no encontrado con ID: " + partnerId);
        }

    }

    @Override
    public void activateGuest(long guestId) throws Exception {
        if (guestId <= 0) {
            throw new Exception("ID de invitado inválido: " + guestId);
        }
    }

    @Override
    public void deactivateGuest(long guestId) throws Exception {
        if (guestId <= 0) {
            throw new Exception("ID de invitado inválido: " + guestId);
        }
    }

    @Override
    //Solicitud de promoción

    public void requestPromotion(long userId) throws Exception {
        Optional<Partner> optionalPartner = partnerRepository.findByUserId_Id(userId);
        if (optionalPartner.isPresent()) {
            Partner partner = optionalPartner.get();
            if ("vip".equalsIgnoreCase(partner.getTypeSuscription())) {
                throw new Exception("El socio ya tiene una suscripción VIP");
            }
            if ("pendiente".equalsIgnoreCase(partner.getTypeSuscription())) {
                throw new Exception("Ya existe una solicitud de promoción pendiente para este socio");
            }
            partner.setTypeSuscription("pendiente");
            partner.setDateCreated(new Date(System.currentTimeMillis()));
            partnerRepository.save(partner);
        } else {

            throw new Exception("No se encontró ningún socio para el ID de usuario:  " + userId);
        }
    }
}
