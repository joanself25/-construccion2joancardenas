package com.mycompany.app.service;

import java.sql.Date;

import com.mycompany.app.dao.interfaces.PersonDao;
import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Dto.UserDTO;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.Dto.GuestDTO;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.InvoiceDetailDTO;

import com.mycompany.app.dao.interfaces.GuestDao;
import com.mycompany.app.dao.interfaces.InvoiceDao;
import com.mycompany.app.dao.interfaces.InvoiceDetailDao;
import com.mycompany.app.dao.interfaces.PartnerDao;
import com.mycompany.app.service.Interface.Loginservice;
import com.mycompany.app.service.Interface.Adminservice;
import com.mycompany.app.service.Interface.Partnerservice;
import java.util.ArrayList;

import java.util.List;
// veriificar si esta buena la conexion a la base de datos y se guardena alli 
import com.mycompany.app.dao.interfaces.UserDao;

import com.mycompany.app.service.Interface.Guestservice;
import com.mycompany.app.service.Interface.InvoiceDetailservice;
import com.mycompany.app.service.Interface.Invoiceservice;
import com.mycompany.app.service.Interface.Personservice;
import com.mycompany.app.service.Interface.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@NoArgsConstructor
public class ClubService implements Adminservice, Loginservice, Partnerservice, Guestservice, Invoiceservice, InvoiceDetailservice, UserService, Personservice {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PersonDao personDao;

    @Autowired
    private PartnerDao partnerDao;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private GuestDao guestDao;

    @Autowired
    private InvoiceDetailDao invoiceDetailDao;

    public static UserDTO user;

    //Gestión de fondos
   

    @Override
//Solicitud de suscripción VIP
    public void requestVIPSubscription(UserDTO userDto) throws Exception {
        PartnerDTO partnerDto = partnerDao.findPartnerById(userDto.getId());

        // Verificar si ya es VIP
        if (partnerDto.getTypeSuscription().equalsIgnoreCase("vip")) {
            throw new Exception("Ya eres un socio VIP.");
        }

        // Verificar disponibilidad de cupos
        if (!partnerDao.isVIPSlotAvailable()) {
            throw new Exception("No hay cupos VIP disponibles en este momento.");
        }

        // Actualizar PartnerDTO con la solicitud VIP
        partnerDto.setDateCreated(new Date(System.currentTimeMillis()));

        // Actualizar el socio en la base de datos
        partnerDao.updatePartner(partnerDto);
    }

    @Override

    // obtener Solicitudes VIP Pendientes
    public List<PartnerDTO> getPendingVIPRequests() throws Exception {
        try {
            return partnerDao.getPendingVIPRequests();
        } catch (Exception e) {
            throw new Exception(" erro, no se encontraron solicitudes  vip pendientes");
        }
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
        System.out.println(" aprobada ");

        partnerDao.updatePartner(partnerDto);
    }

    //Rechazar solicitud VIP
    @Override
    public void rejectVIPRequest(long partnerId) throws Exception {
        PartnerDTO partnerDto = partnerDao.findPartnerById(partnerId);

        if (partnerDto == null) {
            throw new Exception("No se encontró el socio.");
        }

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
        return invoiceDao.getInvoicesPartner(partnerId);
    }

    @Override
    public List<InvoiceDTO> getGuestInvoices(long guestId) throws Exception {
        GuestDTO guest = guestDao.findGuestById(guestId);
        if (guest == null) {
            throw new Exception("invitado no encontrado");
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
        invoiceDao.createAllInvoices(invoiceDto);

        // Si la factura es de un invitado, actualizamos el socio responsable
        if (invoiceDto.getGuestid() != null) {
            PartnerDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
            partner.setfundsMoney(partner.getfundsMoney() - invoiceDto.getAmount());
            partnerDao.PartnerFunds(partner);
        }
    }

    @Override
    // Método para pagar factura
    public void payInvoices(long userId) throws Exception {
        try {
            // Obtener el socio basado en el ID de usuario
            PartnerDTO partnerDTO = partnerDao.findPartnerByUserId(userId);
            if (partnerDTO == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual");
            }

            // Obtener las facturas pendientes
            List<InvoiceDTO> unpaidInvoices = invoiceDao.UnpaidInvoicesPartner(partnerDTO.getId());
            if (unpaidInvoices.isEmpty()) {
                throw new Exception("No hay facturas pendientes de pago");
            }

            // Calcular el monto total a pagar
            double totalAmount = 0;
            for (InvoiceDTO invoice : unpaidInvoices) {
                totalAmount += invoice.getAmount();
            }

            // Verificar si hay fondos suficientes
            if (partnerDTO.getfundsMoney() < totalAmount) {
                throw new Exception("Fondos insuficientes. Total a pagar: $ " + totalAmount + "\n"
                        + ", Fondos disponibles: $" + partnerDTO.getfundsMoney());
            }

            // Procesar el pago de todas las facturas
            for (InvoiceDTO invoice : unpaidInvoices) {
                invoice.setStatus(true);
                invoiceDao.updateInvoice(invoice);
            }

            // Actualizar los fondos del socio
            double newFunds = partnerDTO.getfundsMoney() - totalAmount;
            partnerDTO.setfundsMoney(newFunds);
            partnerDao.PartnerFunds(partnerDTO);

            System.out.println("Pago exitoso. Se pagaron " + unpaidInvoices.size()
                    + " facturas por un total de $" + totalAmount);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override

    // Facturas pagadas
    public List<InvoiceDTO> PaidInvoices(long partnerId) throws Exception {
        PartnerDTO partner = partnerDao.findPartnerById(partnerId);
        if (partner == null) {
            throw new Exception("Socio no encontrado");
        }
        List<InvoiceDTO> allInvoices = invoiceDao.getInvoicesPartner(partnerId);
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
        List<InvoiceDTO> allInvoices = invoiceDao.getInvoicesPartner(partnerId);
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
    @Override
    public void activateGuest(long guestId) throws Exception {
        try {
            // Verificar sesión de usuario
            if (user == null) {
                throw new Exception("No hay un usuario autenticado actualmente.");
            }

            // Obtener el socio actual
            PartnerDTO currentPartner = partnerDao.findPartnerByUserId(user.getId());
            if (currentPartner == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual.");
            }

            // Obtener el invitado
            GuestDTO guestDTO = guestDao.findGuestById(guestId);
            if (guestDTO == null) {
                throw new Exception("No se encontró el invitado con ID: " + guestId);
            }

            // Verificar que el invitado pertenece al socio actual
            if (guestDTO.getPartnerId() == null) {
                throw new Exception("El invitado no está asociado a su cuenta.");
            }

            // Activar el invitado
            guestDTO.setStatus(true);
            guestDao.updateGuest(guestDTO);

        } catch (Exception e) {
            throw new Exception("Error al activar el invitado: " + e.getMessage());
        }
    }

    @Override
    public void desactivateGuest(long guestId) throws Exception {
        try {
            // Verificar sesión de usuario
            if (user == null) {
                throw new Exception("No hay un usuario autenticado actualmente.");
            }

            // Obtener el socio actual
            PartnerDTO currentPartner = partnerDao.findPartnerByUserId(user.getId());
            if (currentPartner == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual.");
            }

            // Obtener el invitado
            GuestDTO guestDTO = guestDao.findGuestById(guestId);
            if (guestDTO == null) {
                throw new Exception("No se encontró el invitado con ID: " + guestId);
            }

            // Verificar que el invitado pertenece al socio actual
            if (guestDTO.getPartnerId() == null) {
                throw new Exception("El invitado no está asociado a su cuenta.");
            }

            // Desactivar el invitado
            guestDTO.setStatus(false);
            guestDao.updateGuest(guestDTO);

        } catch (Exception e) {
            throw new Exception("Error al desactivar el invitado: " + e.getMessage());
        }
    }


    // baja del socio 
    @Override
    public void lowPartner(long userId) throws Exception {

        PartnerDTO partner = partnerDao.findPartnerByUserId(userId);
        if (partner == null) {
            throw new Exception("--No se encontró socio para el ID de usuario proporcionado: " + userId + "--");
        }

        long partnerId = partner.getId();

        // Verificar facturas sin pagar
        List<InvoiceDTO> unpaidInvoices = invoiceDao.UnpaidInvoicesPartner(partnerId);
        if (!unpaidInvoices.isEmpty()) {
            throw new Exception("""
                                === El socio tiene facturas pendientes de pago. ===""");

        }

        try {
            // Eliminar todas las facturas pagadas (si existen)
            try {
                invoiceDao.deleteAllInvoicesByPartnerId(partnerId);
            } catch (Exception e) {
                System.out.println("No se encontraron facturas asociadas al socio.");
            }

            // Eliminar todos los invitados
            List<GuestDTO> guests = guestDao.findGuestsByPartnerId(partnerId);
            for (GuestDTO guest : guests) {
                try {
                    invoiceDao.deleteAllInvoicesByGuestId(guest.getId());
                } catch (Exception e) {
                    System.out.println("No se encontraron facturas asociadas al invitado: " + guest.getId());
                }
                guestDao.deleteGuest(guest.getId());
                userDao.deleteUser(guest.getUserId().getId());
            }

            // Eliminar el socio
            partnerDao.deletePartner(partnerId);

            // Eliminar el usuario asociado al socio
            userDao.deleteUser(userId);

            // Eliminar la persona asociada al usuario
            PersonDTO personToDelete = partner.getUserId().getPersonId();
            personDao.deletePerson(personToDelete);

            System.out.println("La cuenta del socio con ID " + " --" + partnerId + "--"
                    + " se eliminó correctamente.");
        } catch (Exception e) {
            throw new Exception("==Se produjo un error al procesar la eliminación de la cuenta del socio con ID "
                    + partnerId + ": " + e.getMessage() + " ==");
        }
    }

    @Override
    public void requestPromotion(long userId) throws Exception {
        try {
            // Encontrar el socio basado en el ID de usuario
            PartnerDTO partnerDTO = partnerDao.findPartnerByUserId(userId);
            if (partnerDTO == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual. ID de usuario: " + userId);
            }

            // Verificar si el socio ya es VIP
            if ("vip".equalsIgnoreCase(partnerDTO.getTypeSuscription())) {
                throw new Exception("El socio ya tiene una suscripción VIP");
            }

            // Verificar si hay una solicitud VIP pendiente
            if ("vip_pendiente".equalsIgnoreCase(partnerDTO.getTypeSuscription())) {
                throw new Exception("Ya existe una solicitud de promoción pendiente para este socio");
            }

            // Verificar facturas pendientes
            List<InvoiceDTO> unpaidInvoices = getPendingInvoices(partnerDTO.getId());
            if (!unpaidInvoices.isEmpty()) {
                double totalPending = 0;
                for (InvoiceDTO invoice : unpaidInvoices) {
                    totalPending += invoice.getAmount();
                }
                throw new Exception("No se puede procesar la solicitud VIP. \n El socio tiene " + unpaidInvoices.size() + " facturas pendientes.\n por un monto total de $" + totalPending + ". \n"
                        + "Debe pagar todas las facturas pendientes antes de solicitar la promoción.");
            }

            // Crear la solicitud de promoción
            partnerDTO.setTypeSuscription("vip_pendiente");
            partnerDTO.setDateCreated(new Date(System.currentTimeMillis()));
            partnerDao.updatePartner(partnerDTO);

            System.out.println("Solicitud de promoción creada exitosamente. Estado: Pendiente");
        } catch (Exception e) {
            throw new Exception("Error al procesar la solicitud de promoción: \n" + e.getMessage());
        }
    }

    @Override
    public PartnerDTO findPartnerById(long partnerId) throws Exception {
        // busca directamente por el ID de socio.
        //en algunos metodos que cumplen reglas de negocio del socio 

        try {
            return partnerDao.findPartnerById(partnerId);
        } catch (Exception e) {
            throw new Exception("Error al buscar el socio: " + e.getMessage());
        }
    }

// Nuevo método para encontrar un socio por ID de usuario
    @Override
    public PartnerDTO findPartnerByUserId(long userId) throws Exception {
        //con este metodo busco  por medio del ID del usuario,  el ID del  socio y 
        // este despues llevarlo a los metodos para  realizar los procesos de negocios
        try {
            return partnerDao.findPartnerByUserId(userId);
        } catch (Exception e) {
            throw new Exception("Error al buscar el socio por ID de usuario: " + e.getMessage());
        }
    }

    //Subir fondos
    @Override
    public void uploadFunds(long userId, double amount) throws Exception {
        PartnerDTO partnerDto = partnerDao.findPartnerByUserId(userId);
        if (partnerDto == null) {
            throw new Exception("Socio no encontrado");
        }
        double newFunds = partnerDto.getfundsMoney() + amount;
        partnerDto.setfundsMoney(newFunds);
        partnerDao.PartnerFunds(partnerDto);
    }

    @Override
    public void createPartner(UserDTO userDTO, PersonDTO personDTO) throws Exception {
        try {
            // Create the person

            if (personDao.existsByDocument(personDTO)) {
                throw new Exception("Ya existe una persona con ese documento");
            }
            personDao.createPerson(personDTO);

            // Create the user
            if (userDao.existsByUserName(userDTO)) {
                personDao.deletePerson(personDTO);
                throw new Exception("Ya existe un usuario con ese nombre de usuario");
            }
            userDao.createUser(userDTO);

            // Create the partner
            PartnerDTO partnerDTO = new PartnerDTO();
            partnerDTO.setUserId(userDTO);
            partnerDTO.setfundsMoney(50000); // Initial fund for regular partners
            partnerDTO.setTypeSuscription("regular");
            partnerDTO.setDateCreated(new Date(System.currentTimeMillis()));
            partnerDao.createPartner(partnerDTO, userDTO);
        } catch (Exception e) {

            throw new Exception("Error al crear el socio: " + e.getMessage());
        }
    }

    /*private InvoiceDetailDTO createInvoiceDetail(int id, int productNumber) throws Exception {
        InvoiceDetailDTO detail = new InvoiceDetailDTO();
        detail.setId(id);

        switch (productNumber) {
            case 1:
                detail.setDescription("Cerveza");
                detail.setAmount(5);
                break;
            case 2:
                detail.setDescription("Cóctel");
                detail.setAmount(8);
                break;
            case 3:
                detail.setDescription("Agua mineral");
                detail.setAmount(2);
                break;
            case 4:
                detail.setDescription("Refresco");
                detail.setAmount(3);
                break;
            case 5:
                detail.setDescription("Snack");
                detail.setAmount(4);
                break;
            default:
                throw new Exception("Producto no válido: " + productNumber);
        }

        return detail;
    }
     */
    @Override
    public void createInvoicePartner(InvoiceDTO invoiceDto, List<InvoiceDetailDTO> details) throws Exception {
        // Validación explícita del socio
        if (invoiceDto.getPartnerId() == null) {
            throw new Exception("La factura debe estar asociada a un socio");
        }

        // Verificar que el socio existe en la base de datos
        PartnerDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
        if (partner == null) {
            throw new Exception("El socio asociado a la factura no existe");
        }

        try {
            // Calcular el total de la factura
            double total = 0;
            for (InvoiceDetailDTO detail : details) {
                total += detail.getAmount();
            }
            invoiceDto.setAmount(total);

            // Verificar fondos suficientes
            if (partner.getfundsMoney() < total) {
                throw new Exception("Fondos insuficientes para crear la factura. Total: " + total + ", Fondos disponibles: " + partner.getfundsMoney());
            }

            // Crear la factura
            long invoiceId = invoiceDao.createAllInvoices(invoiceDto);
            invoiceDto.setId(invoiceId);

            // Crear los detalles de la factura
            for (InvoiceDetailDTO detail : details) {
                detail.setInvoiceId(invoiceDto);
                invoiceDetailDao.createInvoiceDetail(detail);
            }

            // Actualizar los fondos del socio
            double newFunds = partner.getfundsMoney() - total;
            partner.setfundsMoney(newFunds);
            partnerDao.PartnerFunds(partner);

            System.out.println("Factura creada exitosamente con ID: " + invoiceId + " y monto total: " + total);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Override
    public void createInvoiceGuest(InvoiceDTO invoiceDto, List<InvoiceDetailDTO> details) throws Exception {
        // Validación explícita del invitado y socio
        if (invoiceDto.getGuestid() == null) {
            throw new Exception("La factura debe estar asociada a un invitado");
        }

        if (invoiceDto.getPartnerId() == null) {
            throw new Exception("La factura debe estar asociada a un socio responsable");
        }

        // Verificar que el invitado existe en la base de datos
        GuestDTO guest = guestDao.findGuestById(invoiceDto.getGuestid().getId());
        if (guest == null) {
            throw new Exception("El invitado asociado a la factura no existe");
        }

        // Verificar que el socio existe y es el responsable del invitado
        PartnerDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
        if (partner == null) {
            throw new Exception("El socio asociado a la factura no existe");
        }

        // Validación segura de la relación entre el invitado y el socio
        if (guest.getPartnerId() == null) {
            throw new Exception("El invitado no tiene un socio responsable asignado");
        }

        Long guestPartnerId = guest.getPartnerId().getId();

        if (guestPartnerId == null) {
            throw new Exception("El socio no es el responsable de este invitado");
        }

        try {
            // Calcular el total de la factura
            double total = 0;
            for (InvoiceDetailDTO detail : details) {
                total += detail.getAmount();
            }
            invoiceDto.setAmount(total);

            // Verificar fondos suficientes del socio responsable
            if (partner.getfundsMoney() < total) {
                throw new Exception("Fondos insuficientes del socio responsable para crear la factura. Total: " + total + ", Fondos disponibles: " + partner.getfundsMoney());
            }

            // Crear la factura
            long invoiceId = invoiceDao.createAllInvoices(invoiceDto);
            invoiceDto.setId(invoiceId);

            // Crear los detalles de la factura
            for (InvoiceDetailDTO detail : details) {
                detail.setInvoiceId(invoiceDto);
                invoiceDetailDao.createInvoiceDetail(detail);
            }

            // Actualizar los fondos del socio responsable
            double newFunds = partner.getfundsMoney() - total;
            partner.setfundsMoney(newFunds);
            partnerDao.PartnerFunds(partner);

            System.out.println("Factura del invitado creada exitosamente con ID: " + invoiceId + " y monto total: " + total);
        } catch (Exception e) {
            throw new Exception("Error al crear la factura del invitado: " + e.getMessage());
        }
    }

    @Override
    public void createGuest(UserDTO userDTO, long userId, PersonDTO personDTO) throws Exception {
        try {
            // Encontrar el socio basado en el ID de usuario
            PartnerDTO partnerDTO = partnerDao.findPartnerByUserId(userId);
            if (partnerDTO == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual. ID de usuario: " + userId);
            }

            // Crear la persona
            if (personDTO == null) {
                throw new Exception("Falta PersonDTO en UserDTO");
            }

            if (personDao.existsByDocument(personDTO)) {
                throw new Exception(" ya existe una persona con ese documento ");
            }

            // Crear el usuario
            if (userDao.existsByUserName(userDTO)) {
                personDao.deletePerson(personDTO);
                throw new Exception("Ya existe un usuario con ese nombre de usuario");
            }

            personDao.createPerson(personDTO);

            userDTO.setRol("invitado");
            userDao.createUser(userDTO);

            // Crear el invitado
            GuestDTO guestDTO = new GuestDTO();
            guestDTO.setUserId(userDTO);
            guestDTO.setPartnerId(partnerDTO);
            guestDTO.setStatus(true);
            guestDao.createGuest(guestDTO);

            System.out.println("Invitado creado exitosamente.");
        } catch (Exception e) {
            // En caso de error, intentar revertir las operaciones realizadas
            throw new Exception("Error al crear el invitado: " + e.getMessage());
        }
    }

// conversion invitado a socio 
    @Override
    public void convertGuestToPartner(long userId) throws Exception {
        System.out.println("Iniciando proceso de conversión de invitado a socio. ID del usuario: " + userId);

        // 1. Buscar y validar el invitado
        GuestDTO guestDTO;
        try {
            guestDTO = guestDao.findGuestByUserId(userId);
            if (guestDTO == null) {
                throw new Exception("No se encontró el invitado con ID de usuario: " + userId);
            }
            System.out.println("Invitado encontrado correctamente.");
        } catch (Exception e) {
            throw new Exception("Error al buscar el invitado: " + e.getMessage());
        }

        // 2. Obtener y validar el socio que lo creó
        PartnerDTO sponsorPartner = guestDTO.getPartnerId();
        if (sponsorPartner == null) {
            throw new Exception("No se encontró el socio que creó al invitado");
        }
        System.out.println("Socio responsable encontrado - ID: " + sponsorPartner.getId());

        // 3. Verificar facturas pendientes del invitado
        try {
            List<InvoiceDTO> unpaidGuestInvoices = invoiceDao.UnpaidInvoicesGuest(guestDTO.getId());
            if (!unpaidGuestInvoices.isEmpty()) {
                double totalGuestPending = 0;
                for (InvoiceDTO invoice : unpaidGuestInvoices) {
                    totalGuestPending += invoice.getAmount();
                }
                throw new Exception("El invitado tiene " + unpaidGuestInvoices.size() + "\n"
                        + " factura(s) pendiente(s) por un total de: $" + totalGuestPending + "\n"
                        + "\nDebe pagar todas las facturas antes de convertirse en socio.");
            }
            System.out.println("Verificación de facturas del invitado: Sin facturas pendientes");
        } catch (Exception e) {
            throw new Exception("Error al verificar facturas del invitado: " + e.getMessage());
        }

        // 4. Verificar facturas pendientes del socio responsable
        try {
            List<InvoiceDTO> unpaidSponsorInvoices = invoiceDao.UnpaidInvoicesPartner(sponsorPartner.getId());
            if (!unpaidSponsorInvoices.isEmpty()) {
                double totalSponsorPending = 0;
                for (InvoiceDTO invoice : unpaidSponsorInvoices) {
                    totalSponsorPending += invoice.getAmount();
                }
                throw new Exception("El socio responsable tiene facturas pendientes por $\n"
                        + totalSponsorPending + " que deben ser pagadas antes de la conversión.");
            }

        } catch (Exception e) {
            throw new Exception("Error al verificar facturas del socio responsable: \n " + e.getMessage());
        }

        try {
            // 5. Crear nuevo PartnerDTO
            UserDTO userDTO = userDao.findUserById(userId);
            PartnerDTO newPartnerDTO = new PartnerDTO();
            newPartnerDTO.setUserId(userDTO);
            newPartnerDTO.setfundsMoney(50000); // Fondo inicial para socios regulares
            newPartnerDTO.setTypeSuscription("regular");
            newPartnerDTO.setDateCreated(new Date(System.currentTimeMillis()));

            // 6. Realizar la conversión
            partnerDao.createPartner(newPartnerDTO, userDTO);
            userDTO.setRol("Partner");
            userDao.updateUser(userDTO);
            guestDao.deleteGuest(guestDTO.getId());

            System.out.println("Conversión completada exitosamente:");
            System.out.println("- Nuevo socio creado con ID: " + newPartnerDTO.getId());
            System.out.println("- Fondos iniciales: $50,000");
            System.out.println("- Tipo de suscripción: Regular");
        } catch (Exception e) {
            throw new Exception("Error en el proceso de conversión: " + e.getMessage());
        }
    }

    @Override
    public GuestDTO findGuestByUserId(long userId) throws Exception {
        try {
            return guestDao.findGuestByUserId(userId);
        } catch (Exception e) {
            throw new Exception("Error al buscar el socio por ID de usuario: " + e.getMessage());
        }

    }

    @Override
    public void createInvoiceDetails(InvoiceDTO invoiceDto, List<InvoiceDetailDTO> details) throws Exception {
        if (invoiceDto.getPartnerId() == null && invoiceDto.getGuestid() == null) {
            throw new Exception("La factura debe estar asociada a un socio o un invitado");
        }

        long invoiceId = invoiceDao.createAllInvoices(invoiceDto);
        invoiceDto.setId(invoiceId);

        for (InvoiceDetailDTO detail : details) {
            detail.setInvoiceId(invoiceDto);
            invoiceDetailDao.createInvoiceDetail(detail);
        }

        // Si la factura es de un invitado, actualizamos el socio responsable
        if (invoiceDto.getGuestid() != null) {
            PartnerDTO partner = partnerDao.findPartnerById(invoiceDto.getPartnerId().getId());
            partner.setfundsMoney(partner.getfundsMoney() - invoiceDto.getAmount());
            partnerDao.PartnerFunds(partner);
        }
    }

    @Override
    public void login(UserDTO userDto) throws Exception {
        UserDTO validateDto = userDao.findByUserName(userDto);
        if (validateDto == null) {
            throw new Exception("no existe usuario registrado");
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
        if (this.userDao.existsByUserName(userDTO)) {
            this.personDao.deletePerson(userDTO.getPersonId());
            throw new Exception("ya existe un usuario con ese user name");
        }
        try {
            this.userDao.createUser(userDTO);
        } catch (Exception e) {
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
