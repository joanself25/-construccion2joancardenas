/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.service;

import java.sql.Date;
import java.sql.SQLException;
import com.mycompany.appcs2.App.dao.Personimplementation;
import com.mycompany.appcs2.App.dao.Userimplementationn;
import com.mycompany.appcs2.App.dao.interfaces.PersonDao;
import com.mycompany.appcs2.App.dao.interfaces.Userdao;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Dto.GuestDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.dao.interfaces.PartnerDao;
import com.mycompany.appcs2.App.service.Interface.Loginservice;
import com.mycompany.appcs2.App.service.Interface.Adminservice;
import com.mycompany.appcs2.App.service.Interface.Partnerservice;

import java.util.List;

public class Service implements Adminservice, Loginservice, Partnerservice {

    private Userdao userdao;
    private PersonDao personDao;
    private PartnerDao partnerDao;

    public static UserDTO user;

    public Service() {
        this.userdao = new Userimplementationn();
        this.personDao = new Personimplementation();
    }

    //Gestión de fondos
    @Override
    public void managementFunds(PartnerDTO partnerDto, double Amount) throws Exception {
        double currentFunds = partnerDto.getfundsMoney();
        double newFunds = currentFunds + Amount;
        double maxFunds = partnerDto.getTypeSuscription().equalsIgnoreCase("vip") ? 5000000 : 1000000;

        if (newFunds > maxFunds) {
            throw new Exception("El monto excede el límite máximo para el tipo de suscripción.");
        }

        partnerDto.setfundsMoney(newFunds);
        partnerDao.updatePartnerFunds(partnerDto);

        // Pago automático de facturas pendientes
        List<InvoiceDTO> pendingInvoices = partnerDao.getPendingInvoices(partnerDto.getId());
        for (InvoiceDTO invoice : pendingInvoices) {
            if (newFunds >= invoice.getAmount()) {
                newFunds -= invoice.getAmount();
                partnerDao.payInvoice(invoice.getId());
                partnerDto.setfundsMoney(newFunds);
                partnerDao.updatePartnerFunds(partnerDto);
            } else {
                break;
            }
        }
    }

    @Override
    public void createPartner(UserDTO userDto) throws Exception {
        this.createUser(userDto);
        PartnerDTO partnerDto = new PartnerDTO();
        partnerDto.setUserId(userDto);
        partnerDto.setfundsMoney(50000); // Fondo inicial para socios regulares
        partnerDto.setTypeSuscription("regular");
        partnerDto.setDateCreated(new Date(System.currentTimeMillis()));
        partnerDao.createPartner(partnerDto);
    }

    @Override
//Solicitud de suscripción VIP
    public void requestVIPSubscription(UserDTO userDto) throws Exception {
        PartnerDTO partnerDto = partnerDao.findPartnerByUserId(userDto.getId());

        // Verificar si ya es VIP
        if (partnerDto.getTypeSuscription().equalsIgnoreCase("vip")) {
            throw new Exception("Ya eres un socio VIP.");
        }

        // Verificar disponibilidad de cupos
        if (!partnerDao.isVIPSlotAvailable()) {
            throw new Exception("No hay cupos VIP disponibles en este momento.");
        }

        // Actualizar PartnerDTO con la solicitud VIP
        partnerDto.setVipRequestDate(new Date(System.currentTimeMillis()));
        partnerDto.setVipRequestStatus("Pendiente");

        // Actualizar el socio en la base de datos
        partnerDao.updatePartner(partnerDto);
    }

    @Override

    // obtener Solicitudes VIP Pendientes
    public List<PartnerDTO> getPendingVIPRequests() throws Exception {
        return partnerDao.getPendingVIPRequests();
    }

    @Override
    // Aprobar solicitud VIP
    public void approveVIPRequest(long partnerId) throws Exception {
        PartnerDTO partner = partnerDao.findPartnerById(partnerId);

        // Verificar si aún hay cupos disponibles
        if (!partnerDao.isVIPSlotAvailable()) {
            throw new Exception("No hay cupos VIP disponibles.");
        }

        partner.setTypeSuscription("vip");
        partner.setVipRequestStatus("Aprobada");
        partnerDao.updatePartner(partner);
    }

    @Override
    //Rechazar solicitud VIP
    public void rejectVIPRequest(long partnerId) throws Exception {
        PartnerDTO partner = partnerDao.findPartnerById(partnerId);
        partner.setVipRequestStatus("Rechazada");
        partnerDao.updatePartner(partner);
    }

    @Override
    // obtener Facturas Totales Pagadas
    public double getTotalPaidInvoices(long id) throws Exception {
        List<InvoiceDTO> paidInvoices = partnerDao.getPaidInvoices(id);
        return paidInvoices.stream().mapToDouble(InvoiceDTO::getAmount).sum();
    }

    // ... otros métodos ...
    @Override
    public void guestPartner(GuestDTO guestdto) throws Exception {

    }

    @Override
    public void lowPartner(GuestDTO guestdto) throws Exception {

    }

    @Override
    public void createInvoice(InvoiceDTO invoicedto) throws Exception {

    }

    @Override
    public void createGuest(UserDTO userDto) throws Exception {
        this.createUser(userDto);
    }

    @Override
    public void login(UserDTO userDto) throws Exception {
        UserDTO validateDto = userdao.findByUserName(userDto);
        if (validateDto == null) {
            throw new Exception("no existe el usuario registrado");
        }
        if (!userDto.getPassword().equals(validateDto.getPassword())) {
            throw new Exception("usuario o contraseña incorrecto");
        }
        userDto.setRol(validateDto.getRol());
        user = validateDto;
    }

    @Override
    public void logout() {
        user = null;
        System.out.println("se ha cerrado sesión");
    }

    private void createUser(UserDTO userDTO) throws Exception {
        this.createPerson(userDTO.getPersonId());
        PersonDTO personDto = personDao.findByDocument(userDTO.getPersonId());
        userDTO.setPersonId(personDto);
        if (this.userdao.existsByUserName(userDTO)) {
            this.personDao.deletePerson(userDTO.getPersonId());
            throw new Exception("ya existe un usuario con ese user name");
        }
        try {
            this.userdao.createUser(userDTO);
        } catch (SQLException e) {
            this.personDao.deletePerson(userDTO.getPersonId());
        }

    }

    private void createPerson(PersonDTO personDto) throws Exception {
        if (this.personDao.existsByDocument(personDto)) {
            throw new Exception("ya existe una persona con ese documento");
        }
        this.personDao.createPerson(personDto);
    }
}
