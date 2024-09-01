package com.mycompany.appcs2.App.service;

import java.sql.Date;
import java.sql.SQLException;
import com.mycompany.appcs2.App.dao.Personimplementation;
import com.mycompany.appcs2.App.dao.Userimplementationn;
import com.mycompany.appcs2.App.dao.interfaces.PersonDao;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Dto.GuestDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.dao.GuestImplementation;
import com.mycompany.appcs2.App.dao.InvoiceDetailImplementation;
import com.mycompany.appcs2.App.dao.Invoiceimplementation;
import com.mycompany.appcs2.App.dao.Partnerimplementation;
import com.mycompany.appcs2.App.dao.interfaces.GuestDao;
import com.mycompany.appcs2.App.dao.interfaces.InvoiceDao;
import com.mycompany.appcs2.App.dao.interfaces.InvoiceDetailDao;
import com.mycompany.appcs2.App.dao.interfaces.PartnerDao;
import com.mycompany.appcs2.App.service.Interface.Loginservice;
import com.mycompany.appcs2.App.service.Interface.Adminservice;
import com.mycompany.appcs2.App.service.Interface.Partnerservice;
import java.util.ArrayList;

import java.util.List;
// veriificar si esta buena la conexion a la base de datos y se guardena alli 
import com.mycompany.appcs2.App.dao.interfaces.UserDao;
import com.mycompany.appcs2.App.model.Partner;
import com.mycompany.appcs2.App.service.Interface.Guestservice;
import com.mycompany.appcs2.App.service.Interface.InvoiceDetailservice;
import com.mycompany.appcs2.App.service.Interface.Invoiceservice;
import com.mycompany.appcs2.App.service.Interface.Personservice;
import com.mycompany.appcs2.App.service.Interface.UserService;

public class Service implements Adminservice, Loginservice, Partnerservice, Guestservice, Invoiceservice, InvoiceDetailservice, UserService, Personservice {

    private UserDao userDao;
    private PersonDao personDao;
    private PartnerDao partnerDao;
    private InvoiceDao invoiceDao;
    private GuestDao guestDao;
    private InvoiceDetailDao invoiceDetailDao;

    public static UserDTO user;

    public Service() throws SQLException {
        this.userDao = new Userimplementationn();
        this.personDao = new Personimplementation();
        this.partnerDao = new Partnerimplementation();
        this.invoiceDetailDao = new InvoiceDetailImplementation();
        this.invoiceDao = new Invoiceimplementation();
        this.guestDao = new GuestImplementation();

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
        PartnerDTO partnerDto = partnerDao.findPartnerById(partnerId);

        // Verificar si aún hay cupos disponibles
        if (!partnerDao.isVIPSlotAvailable()) {
            throw new Exception("No hay cupos VIP disponibles.");
        }

        partnerDto.setTypeSuscription("vip");
        partnerDto.setVipRequestStatus("Aprobada");
        partnerDao.updatePartner(partnerDto);
    }

    @Override
    //Rechazar solicitud VIP
    public void rejectVIPRequest(long partnerId) throws Exception {
        PartnerDTO partnerDto = partnerDao.findPartnerById(partnerId);
        partnerDto.setVipRequestStatus("Rechazada");
        partnerDao.updatePartner(partnerDto);
    }

    @Override
    // obtener Facturas Totales Pagadas
    public double getTotalPaidInvoices(long id) throws Exception {
        List<InvoiceDTO> paidInvoices = partnerDao.getPaidInvoices(id);
        return paidInvoices.stream().mapToDouble(InvoiceDTO::getAmount).sum();
    }

    @Override
    public List<InvoiceDTO> getPartnerInvoices(long partnerId) throws Exception {
        PartnerDTO partner = partnerDao.findPartnerById(partnerId);
        if (partner == null) {
            throw new Exception("Socio no encontrado");
        }
        return invoiceDao.getInvoicesByPartnerId(partnerId);
    }

    @Override
    public List<InvoiceDTO> getGuestInvoices(long guestId) throws Exception {
        GuestDTO guest = guestDao.findGuestById(guestId);
        if (guest == null) {
            throw new Exception("Invitado no encontrado");
        }
        PartnerDTO partner = guest.getPartnerId();
        if (partner == null) {
            throw new Exception("Este invitado no está asociado a ningún socio");
        }
        return invoiceDao.getInvoicesByGuestId(guestId);
    }

    // Método para crear factura
    @Override
    public void createInvoice(InvoiceDTO invoiceDto) throws Exception {
        if (invoiceDto.getPartnerId() == null && invoiceDto.getGuestid() == null) {
            throw new Exception("La factura debe estar asociada a un socio o un invitado ");
        }
        invoiceDao.createInvoice(invoiceDto);

        // Si la factura es de un invitado, actualizamos el socio responsable
        if (invoiceDto.getGuestid() != null) {
            PartnerDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
            partner.setfundsMoney(partner.getfundsMoney() - invoiceDto.getAmount());
            partnerDao.updatePartnerFunds(partner);
        }
    }

    // Método para pagar factura
    @Override
    public void payInvoice(long invoiceId) throws Exception {
        InvoiceDTO invoiceDto = invoiceDao.findInvoiceById(invoiceId);
        if (invoiceDto == null) {
            throw new Exception("Factura no encontrada");
        }
        if (invoiceDto.isStatus()) {
            throw new Exception("Esta factura ya está pagada");
        }

        PartnerDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
        if (partner.getfundsMoney() < invoiceDto.getAmount()) {
            throw new Exception("Fondos insuficientes para pagar la factura");
        }

        partner.setfundsMoney(partner.getfundsMoney() - invoiceDto.getAmount());
        partnerDao.updatePartnerFunds(partner);

        invoiceDto.setStatus(true);
        invoiceDao.updateInvoice(invoiceDto);
    }

    @Override

    // Facturas pagadas
    public List<InvoiceDTO> PaidInvoices(long partnerId) throws Exception {
        PartnerDTO partner = partnerDao.findPartnerById(partnerId);
        if (partner == null) {
            throw new Exception("Socio no encontrado");
        }
        List<InvoiceDTO> allInvoices = invoiceDao.getInvoicesByPartnerId(partnerId);
        List<InvoiceDTO> paidInvoices = new ArrayList<>();
        for (InvoiceDTO invoiceDto : allInvoices) {
            if (!invoiceDto.isStatus()) {
            } else {
                paidInvoices.add(invoiceDto);
            }
        }
        return paidInvoices;

    }

    @Override
    //obtener facturas pendientes

    public List<InvoiceDTO> getPendingInvoices(long partnerId) throws Exception {
        PartnerDTO partner = partnerDao.findPartnerById(partnerId);
        if (partner == null) {
            throw new Exception("Socio no encontrado");
        }
        List<InvoiceDTO> allInvoices = invoiceDao.getInvoicesByPartnerId(partnerId);
        List<InvoiceDTO> pendingInvoices = new ArrayList<>();
        for (InvoiceDTO invoice : allInvoices) {
            if (!invoice.isStatus()) {
                pendingInvoices.add(invoice);
            }
        }
        return pendingInvoices;
    }

    @Override
    //Obtener facturas pagadas
    public List<InvoiceDTO> getPaidInvoices(long partnerId) throws Exception {

        return PaidInvoices(partnerId);

    }

    // activar invitado
    public void activateGuest(long guestId) throws Exception {
        GuestDTO guestDto = guestDao.findGuestById(guestId);
        if (guestDto == null) {
            throw new Exception("Invitado no encontrado");
        }
        guestDto.setStatus(true);
        guestDao.updateGuest(guestDto);
    }

    // desactivar invitado 
    public void deactivateGuest(long guestId) throws Exception {
        GuestDTO guestDto = guestDao.findGuestById(guestId);
        if (guestDto == null) {
            throw new Exception("Invitado no encontrado");
        }
        guestDto.setStatus(false);
        guestDao.updateGuest(guestDto);
    }

    // baja del socio 
    public void lowPartner(long partnerId) throws Exception {
        PartnerDTO partnerDto = partnerDao.findPartnerById(partnerId);
        if (partnerDto == null) {
            throw new Exception("Socio no encontrado");
        }
        // Aquí podrías agregar lógica adicional, como crear una solicitud de baja en una tabla separada
        partnerDto.setLowRequestStatus("Pendiente");
        partnerDto.setLowRequestDate(new Date(System.currentTimeMillis()));
        partnerDao.updatePartner(partnerDto);
    }

    // Solicitud de promoción
    public void requestPromotion(long partnerId) throws Exception {
        PartnerDTO partnerDto = partnerDao.findPartnerById(partnerId);
        if (partnerDto == null) {
            throw new Exception("Socio no encontrado");
        }
        // Aquí podrías agregar lógica adicional, como crear una solicitud de promoción en una tabla separada
        partnerDto.setPromotionRequestStatus("Pendiente");
        partnerDto.setPromotionRequestDate(new Date(System.currentTimeMillis()));
        partnerDao.updatePartner(partnerDto);
    }

    //Subir fondos
    public void uploadFunds(long partnerId, double amount) throws Exception {
        PartnerDTO partnerDto = partnerDao.findPartnerById(partnerId);
        if (partnerDto == null) {
            throw new Exception("Socio no encontrado");
        }
        double newFunds = partnerDto.getfundsMoney() + amount;
        partnerDto.setfundsMoney(newFunds);
        partnerDao.updatePartnerFunds(partnerDto);
    }

    @Override
    public void createPartner(UserDTO userDTO) throws Exception {

        try {
            // Crear la persona
            PersonDTO personDTO = userDTO.getPersonId();
            if (personDao.existsByDocument(personDTO)) {
                throw new Exception("Ya existe una persona con ese documento");
            }
            personDao.createPerson(personDTO);

            // Crear el usuario
            if (userDao.existsByUserName(userDTO)) {
                personDao.deletePerson(personDTO);
                throw new Exception("Ya existe un usuario con ese nombre de usuario");
            }
            userDao.createUser(userDTO);

            // Crear el socio
            PartnerDTO partnerDTO = new PartnerDTO();
            partnerDTO.setUserId(userDTO);
            partnerDTO.setFundsmoney(50000); // Fondo inicial para socios regulares
            partnerDTO.setTypeSuscription("regular");
            partnerDTO.setDateCreated(new Date(System.currentTimeMillis()));
            partnerDao.createPartne(partnerDTO);
        } catch (Exception e) {
            // En caso de error, intentar revertir las operaciones realizadas
            // Esto debería manejarse idealmente con transacciones
            throw new Exception("Error al crear el socio: " + e.getMessage());
        }

    }

    @Override
    public void createGuest(UserDTO userDTO, long partnerId) throws Exception {
        try {
            // Verificar que el socio existe
            PartnerDTO partnerDTO = partnerDao.findPartnerById(partnerId);
            if (partnerDTO == null) {
                throw new Exception("El socio especificado no existe");
            }

            // Crear la persona
            PersonDTO personDTO = userDTO.getPersonId();
            if (personDao.existsByDocument(personDTO)) {
                throw new Exception("Ya existe una persona con ese documento");
            }
            personDao.createPerson(personDTO);

            // Crear el usuario
            if (userDao.existsByUserName(userDTO)) {
                personDao.deletePerson(personDTO);
                throw new Exception("Ya existe un usuario con ese nombre de usuario");
            }
            userDao.createUser(userDTO);

            // Crear el invitado
            GuestDTO guestDTO = new GuestDTO();
            guestDTO.setUserId(userDTO);
            guestDTO.setPartnerId(partnerDTO);
            guestDTO.setStatus(true); // Asumimos que el invitado se crea activo
            guestDao.createGuest(guestDTO);
        } catch (Exception e) {
            // En caso de error, intentar revertir las operaciones realizadas
            // Esto debería manejarse idealmente con transacciones
            throw new Exception("Error al crear el invitado: " + e.getMessage());
        }
    }

    public PartnerDTO findPartnerByUserId(long userId) throws Exception {
        return partnerDao.findPartnerByUserId(userId);
    }

    public UserDTO createPartn(UserDTO userDTO) throws Exception {
        // First, create the user
        userDao.createUser(userDTO);

        // Then, create the partner
        PartnerDTO partnerDTO = new PartnerDTO();

        partnerDao.createPartne(partnerDTO);

        return userDTO;
    }

    // Buscar invitado por ID de usuario
    @Override
    public GuestDTO findGuestByUserId(long userId) throws Exception {
        return guestDao.findGuestByUserId(userId);
    }

    @Override

    // conversion invitado a socio 
    public void convertGuestToPartner() throws Exception {
        PartnerDTO partnerDto = new PartnerDTO();
        user.setRol("partner");
        partnerDto.setUserId(user);
        partnerDto.setfundsMoney(50000); // Initial fund for regular partners
        partnerDto.setTypeSuscription("regular");
        partnerDto.setDateCreated(new Date(System.currentTimeMillis()));

        partnerDao.createPartne(partnerDto);
        //guestDao.deleteGuest(guestDto.getId());

        userDao.updateUser(user);
    }

    public void createInvoiceWithDetails(InvoiceDTO invoiceDto, List<InvoiceDetailDTO> details) throws Exception {
        if (invoiceDto.getPartnerId() == null && invoiceDto.getGuestid() == null) {
            throw new Exception("La factura debe estar asociada a un socio o un invitado");
        }

        long invoiceId = invoiceDao.createInvoicess(invoiceDto);
        invoiceDto.setId(invoiceId);

        for (InvoiceDetailDTO detail : details) {
            detail.setInvoiceId(invoiceDto);
            invoiceDetailDao.createInvoiceDetail(detail);
        }

        // Si la factura es de un invitado, actualizamos el socio responsable
        if (invoiceDto.getGuestid() != null) {
            PartnerDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
            partner.setfundsMoney(partner.getfundsMoney() - invoiceDto.getAmount());
            partnerDao.updatePartnerFunds(partner);
        }
    }

    @Override
    public void login(UserDTO userDto) throws Exception {
        UserDTO validateDto = userDao.findByUserName(userDto);
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

    @Override

    public void createUser(UserDTO userDTO) throws Exception {
        this.createPerson(userDTO.getPersonId());
        PersonDTO personDto = personDao.findByDocument(userDTO.getPersonId());
        userDTO.setPersonId(personDto);
        if (this.userDao.existsByUserName(userDTO)) {
            this.personDao.deletePerson(userDTO.getPersonId());
            throw new Exception("ya existe un usuario con ese user name");
        }
        try {
            this.userDao.createUser(userDTO);
        } catch (SQLException e) {
            this.personDao.deletePerson(userDTO.getPersonId());
        }

    }

    @Override

    public void createPerson(PersonDTO personDto) throws Exception {
        if (this.personDao.existsByDocument(personDto)) {
            throw new Exception("ya existe una persona con ese documento");
        }
        this.personDao.createPerson(personDto);
    }

}
