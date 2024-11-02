/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.dao;

/**
 *
 * @author CLAUDIA
 *
 */
import com.mycompany.app.dao.interfaces.GuestDao;
import com.mycompany.app.Dto.GuestDTO;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Helpers.Helpers;
import com.mycompany.app.dao.repositories.GuestRepository;
import com.mycompany.app.dao.repositories.InvoiceRepository;
import com.mycompany.app.model.Guest;
import com.mycompany.app.model.Invoice;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
@Setter
@Getter

public class GuestImplementation implements GuestDao {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public void createGuest(GuestDTO guestDTO) throws Exception {
        if (guestDTO == null) {
            throw new Exception("El GuestDTO no puede ser nulo");
        }
        Guest guest = Helpers.parse(guestDTO);
        guest = guestRepository.save(guest);
        guestDTO.setId(guest.getId());
    }

    @Override
    public List<InvoiceDTO> getGuestPendingInvoices(long guestId) throws Exception {
        if (guestId <= 0) {
            throw new Exception("ID de invitado inválido: " + guestId);
        }

        Optional<Guest> optionalGuest = guestRepository.findById(guestId);
        if (!optionalGuest.isPresent()) {
            throw new Exception("Invitado no encontrado con ID: " + guestId);
        }

        Guest guest = optionalGuest.get();
        if (guest.getUserId() == null || guest.getUserId().getPersonId() == null) {
            throw new Exception("El invitado no tiene información de usuario asociada");
        }

        // Obtener las facturas usando el ID de la persona asociada al usuario
        List<Invoice> pendingInvoices = invoiceRepository.findByPersonId_IdAndStatusFalse(
                guest.getUserId().getPersonId().getId()
        );

        return pendingInvoices.stream()
                .map(Helpers::parse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> getGuestPaidInvoices(long guestId) throws Exception {
        if (guestId <= 0) {
            throw new Exception("ID de invitado inválido: " + guestId);
        }

        Optional<Guest> optionalGuest = guestRepository.findById(guestId);
        if (!optionalGuest.isPresent()) {
            throw new Exception("Invitado no encontrado con ID: " + guestId);
        }

        Guest guest = optionalGuest.get();
        if (guest.getUserId() == null || guest.getUserId().getPersonId() == null) {
            throw new Exception("El invitado no tiene información de usuario asociada");
        }

        // Obtener las facturas pagadas usando el ID de la persona
        List<Invoice> paidInvoices = invoiceRepository.findByPersonId_IdAndStatusTrue(
                guest.getUserId().getPersonId().getId()
        );

        return paidInvoices.stream()
                .map(Helpers::parse)
                .collect(Collectors.toList());
    }

    @Override
    public GuestDTO findGuestById(long guestId) throws Exception {
        if (guestId <= 0) {
            throw new Exception("ID de invitado inválido: " + guestId);
        }

        Optional<Guest> optionalGuest = guestRepository.findById(guestId);
        if (!optionalGuest.isPresent()) {
            throw new Exception("Invitado no encontrado con ID: " + guestId);
        }
        return Helpers.parse(optionalGuest.get());
    }

    @Override
    public void updateGuest(GuestDTO guestDTO) throws Exception {
        if (guestDTO == null || guestDTO.getId() <= 0) {
            throw new Exception("GuestDTO inválido o ID no especificado");
        }

        // Verificar si el guest existe antes de actualizar
        Optional<Guest> existingGuest = guestRepository.findById(guestDTO.getId());
        if (!existingGuest.isPresent()) {
            throw new Exception("Invitado no encontrado con ID: " + guestDTO.getId());
        }
        Guest guest = Helpers.parse(guestDTO);
        guestRepository.save(guest);
    }

    @Override
    public void deleteGuest(long guestId) throws Exception {
        if (guestId <= 0) {
            throw new Exception("ID de invitado inválido: " + guestId);
        }

        Optional<Guest> guest = guestRepository.findById(guestId);
        if (!guest.isPresent()) {
            throw new Exception("Invitado no encontrado con ID: " + guestId);
        }

        guestRepository.deleteById(guestId);
    }

    @Override
    public List<GuestDTO> findGuestsByPartnerId(long partnerId) throws Exception {
        if (partnerId <= 0) {
            throw new Exception("ID de socio inválido: " + partnerId);
        }

        List<Guest> guests = guestRepository.findByPartnerId_Id(partnerId);
        List<GuestDTO> guestDTOs = new ArrayList<>();
        for (Guest guest : guests) {
            guestDTOs.add(Helpers.parse(guest));
        }
        return guestDTOs;
    }

    @Override
    public GuestDTO findGuestByUserId(long userId) throws Exception {
        if (userId <= 0) {
            throw new Exception("ID de usuario inválido: " + userId);
        }

        try {
            Optional<Guest> optionalGuest = guestRepository.findByUserId_Id(userId);
            if (!optionalGuest.isPresent()) {
                throw new Exception("Invitado no encontrado para el usuario con ID: " + userId);
            }
            return Helpers.parse(optionalGuest.get());
        } catch (Exception e) {
            // Log del error para debugging
            throw new Exception( e.getMessage());
        }
    }
}
// select busca 
// insertar
// update buscar
// delete eliminiar 

