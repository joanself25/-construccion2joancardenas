package com.mycompany.app.Controller;

import com.mycompany.app.Controller.Request.CreateInvoicesDetailsRequest;
import com.mycompany.app.Controller.Request.CreateInvoicesRequest;
import com.mycompany.app.Controller.Request.CreateUserRequest;
import com.mycompany.app.Controller.Request.GuestStatusRequest;
import com.mycompany.app.Controller.Request.LowPartnerRequest;
import com.mycompany.app.Controller.Request.ManagementFundsRequest;
import com.mycompany.app.Controller.Request.PayInvoicesRequest;
import com.mycompany.app.Controller.Request.RequestPromotion;
import com.mycompany.app.Controller.Validator.PartnerValidator;
import com.mycompany.app.Controller.Validator.GuestValidator;
import com.mycompany.app.Controller.Validator.InvoiceDetailValidator;
import com.mycompany.app.Controller.Validator.PersonValidator;
import com.mycompany.app.Controller.Validator.InvoiceValidator;
import com.mycompany.app.Controller.Validator.UserValidator;
import com.mycompany.app.Dto.GuestDTO;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.InvoiceDetailDTO;
import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Dto.UserDTO;
import com.mycompany.app.dao.interfaces.UserDao;
import com.mycompany.app.service.ClubService;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

// Opciones de Pago: Inmediato o pendiente para socios. 
// Pagos Pendientes por Invitados: Deben ser cubiertos por el socio. 
//Promoción a VIP: Generación y aprobación de lista de candidatos,notificación manual a los socios sobre el resultado.
@Controller
@Getter
@Setter
@NoArgsConstructor

public class PartnerController implements ControllerInterface {

    @Autowired
    private PersonValidator personvalidator;

    @Autowired
    private PartnerValidator partnervalidator;

    @Autowired
    private GuestValidator guestvalidator;

    @Autowired
    private InvoiceDetailValidator invoicedetailvalidator;

    @Autowired
    private InvoiceValidator invoicevalidator;

    @Autowired
    private UserValidator uservalidator;

    @Autowired
    private ClubService service;

    @Autowired
    private GuestController guestController;

    @Autowired
    private UserDao userDao;

    private PartnerDTO currentPartner;

    private UserDTO currentUser;

    private static final String MENU = "ingrese la opcion que desea ejecutar:  \n 1. crear invitado \n 2. Activar invitado \n 3. desactivar invitados \n 4. solicitar baja \n 5. solicitar promocion \n 6.crear factura de socio \n 7. pagar facturas pendientes   \n 8. Cerrar sesión.";
//7.gestion de fondos 

    @Override
    public void session() throws Exception {

    }

    @PostMapping("/invoicespartner")
    private ResponseEntity<String> createInvoicePartner(@RequestBody CreateInvoicesRequest request) throws Exception {
        try {
            // Validate partnerId
            int partnerId = partnervalidator.isValidinteger("id del socio", request.getPartnerId());

            // Find partner
            PartnerDTO partnerDto = service.findPartnerById(partnerId);
            if (partnerDto == null) {
                return new ResponseEntity<>("No se encontró el socio con el ID proporcionado", HttpStatus.NOT_FOUND);
            }

            // Validate partner has associated user and person
            if (partnerDto.getUserId() == null || partnerDto.getUserId().getPersonId() == null) {
                return new ResponseEntity<>("El socio no tiene datos de usuario o persona asociados", HttpStatus.BAD_REQUEST);
            }

            // Create invoice
            InvoiceDTO invoiceDto = new InvoiceDTO();
            invoiceDto.setCreationDate(new Date(System.currentTimeMillis()));
            invoiceDto.setStatus(false);
            invoiceDto.setPartnerId(partnerDto);
            invoiceDto.setPersonId(partnerDto.getUserId().getPersonId());

            // Process invoice details
            List<InvoiceDetailDTO> details = new ArrayList<>();
            double total = 0.0;

            if (request.getDetails() == null || request.getDetails().isEmpty()) {
                return new ResponseEntity<>("Debe incluir al menos un detalle en la factura", HttpStatus.BAD_REQUEST);
            }

            for (CreateInvoicesDetailsRequest detail : request.getDetails()) {
                try {
                    // Validate item - convert from String to integer
                    int itemNumber = invoicedetailvalidator.isValidinteger("número de item", detail.getItem());

                    // Validate description
                    String description = detail.getDescription();
                    invoicedetailvalidator.validDescripcion(description);

                    // Validate amount - convert from String to double
                    double amount;
                    try {
                        amount = Double.parseDouble(detail.getAmount());
                        if (amount <= 0) {
                            return new ResponseEntity<>("El valor del item debe ser mayor a 0", HttpStatus.BAD_REQUEST);
                        }
                    } catch (NumberFormatException e) {
                        return new ResponseEntity<>("El valor del item '" + detail.getAmount() + "' no es un número válido", HttpStatus.BAD_REQUEST);
                    }

                    InvoiceDetailDTO invoiceDetailDto = new InvoiceDetailDTO();
                    invoiceDetailDto.setItem(itemNumber);
                    invoiceDetailDto.setDescription(description);
                    invoiceDetailDto.setAmount(amount);
                    invoiceDetailDto.setInvoiceId(invoiceDto);

                    total += amount;
                    details.add(invoiceDetailDto);
                } catch (Exception e) {
                    return new ResponseEntity<>("Error validando el item: " + e.getMessage(), HttpStatus.BAD_REQUEST);
                }
            }

            invoiceDto.setAmount(total);

            // Save invoice and details
            service.createInvoicePartner(invoiceDto, details);
            return new ResponseEntity<>("Factura creada exitosamente. Total: " + total, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/guest")
    private ResponseEntity createGuest(@RequestBody CreateUserRequest request) throws Exception {
        try {
            String name = request.getName();
            personvalidator.validName(name);

            long cc = personvalidator.validDocument(request.getDocument());

            long cel = personvalidator.validCelphone(request.getCellphone());

            String userName = request.getUsername();
            uservalidator.validUserName(userName);

            String password = request.getPassword();
            uservalidator.validPassword(password);

            PersonDTO personDTO = new PersonDTO();
            personDTO.setName(name);
            personDTO.setCelphone(cel);
            personDTO.setDocument(cc);

            UserDTO userDTO = new UserDTO();
            userDTO.setPersonId(personDTO);
            userDTO.setUsername(userName);
            userDTO.setPassword(password);
            userDTO.setRol("invitado");

            PartnerDTO partnerDto = new PartnerDTO();
            partnerDto.setId(partnervalidator.isValidinteger("id del socio", request.getPartnerId()));

            GuestDTO guestDto = new GuestDTO();
            guestDto.setUserId(userDTO);
            guestDto.setStatus(false);
            guestDto.setPartnerId(partnerDto);

            this.service.createGuest(guestDto);

            return new ResponseEntity<>("invitado creado exitosamente", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/activateguest")
    private ResponseEntity<String> activateGuest(@RequestBody GuestStatusRequest request) throws Exception {
        try {

            // Validar y obtener el ID del invitado
            long guestId = guestvalidator.validId(request.getGuestId());

            // Obtener el ID del socio del request o del token de autenticación
            long partnerId = request.getPartnerId(); // Asumiendo que agregas este campo al GuestStatusRequest

            service.activateGuest(guestId, partnerId);

            return new ResponseEntity<>("Invitado activado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping("/desactivateguest")
    private ResponseEntity<String> desactivateGuest(@RequestBody GuestStatusRequest request) throws Exception {
        try {

            long guestId = guestvalidator.validId(request.getGuestId());
            long partnerId = request.getPartnerId(); // Asumiendo que agregas este campo al GuestStatusRequest

            service.desactivateGuest(guestId, partnerId);
            return new ResponseEntity<>("Invitado desactivado exitosamente", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

    @PutMapping("/lowpartner")
    private ResponseEntity<String> lowPartner(@RequestBody LowPartnerRequest request) throws Exception {
        try {
            // Validate partnerId
            long userId = partnervalidator.isValidlong("id del socio", request.getUserId());

            // Find partner
            PartnerDTO partnerDTO = service.findPartnerByUserId(userId);

            if (partnerDTO == null) {
                return new ResponseEntity<>("No se encontró un socio con el ID proporcionado", HttpStatus.NOT_FOUND);
            }

            service.lowPartner(userId);

            service.logout();

            return new ResponseEntity<>("Socio dado de baja exitosamente", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/requestpromotion")
    private ResponseEntity<String> requestPromotion(@RequestBody RequestPromotion request) throws Exception {
        try {
            // Validar y obtener el ID del usuario
            long userId = partnervalidator.isValidlong("id del socio", request.getUserId());

            // Llamar al servicio para procesar la solicitud de promoción
            service.requestPromotion(userId);

            return new ResponseEntity<>("Solicitud de promoción enviada. Un administrador la revisará pronto.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/payinvoices")
    private ResponseEntity<String> payInvoices(@RequestBody PayInvoicesRequest request) throws Exception {
        try {
            // Verificar que el request no sea nulo y contenga un ID válido
            if (request == null || request.getUserId() <= 0) {
                throw new Exception("Solicitud inválida o ID de usuario no proporcionado");
            }

            // Llamar al servicio para procesar el pago
            service.payInvoices(request.getUserId());

            return new ResponseEntity<>("Pago procesado exitosamente", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/funds")
    private ResponseEntity<String> managementFunds(@RequestBody ManagementFundsRequest request) {
        try {
            // Validaciones previas
            if (request == null) {
                return new ResponseEntity<>("La solicitud no puede ser nula", HttpStatus.BAD_REQUEST);
            }

            if (request.getUserId() == null) {
                return new ResponseEntity<>("El ID del usuario no puede ser nulo", HttpStatus.BAD_REQUEST);
            }

            if (request.getAmount() == null || request.getAmount() <= 0) {
                return new ResponseEntity<>("El monto debe ser mayor a cero", HttpStatus.BAD_REQUEST);
            }

            // Validar que el usuario existe antes de procesar
            UserDTO user = userDao.findUserById(request.getUserId());
            if (user == null) {
                return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
            }

            // Validar que el usuario tiene un PersonDTO válido
           

            // Llamada al servicio
            service.uploadFunds(request.getUserId(), request.getAmount());

            return new ResponseEntity<>("Fondos incrementados exitosamente por un valor de: " + request.getAmount(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
