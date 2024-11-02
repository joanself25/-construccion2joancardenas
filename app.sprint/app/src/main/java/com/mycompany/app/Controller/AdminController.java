package com.mycompany.app.Controller;

import com.mycompany.app.Controller.Request.CreateUserRequest;
import com.mycompany.app.Controller.Request.PrintInvoicesGuestRequest;
import com.mycompany.app.Controller.Request.PrintnvoicesPartnerRequest;
import com.mycompany.app.Controller.Request.PrintInvoicesRequest;
import com.mycompany.app.Controller.Request.RequestPromotion;
import com.mycompany.app.Controller.Request.ReviewVIPRequest;
import com.mycompany.app.Controller.Validator.UserValidator;
import com.mycompany.app.Controller.Validator.PersonValidator;
import com.mycompany.app.Controller.Validator.PartnerValidator;
import com.mycompany.app.Dto.GuestDTO;
import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Dto.UserDTO;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.service.ClubService;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.service.Interface.Adminservice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Getter
@Setter
@NoArgsConstructor
public class AdminController implements ControllerInterface {

    @Autowired
    private PersonValidator personValidator;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private ClubService service;

    @Autowired
    private PartnerValidator partnerValidator;

    @Autowired
    private PartnerController partnerController;

    private static final String MENU = "ingrese la opcion que desea \n 1.para crear socio  \n 2. Revisar solicitudes vip    \n 3. Ver historial de facturas de socios \n 4. cerrar sesion. \n";

    @Override
    public void session() throws Exception {

    }

    @PostMapping("/partner")
    private ResponseEntity createPartner(@RequestBody CreateUserRequest request) throws Exception {
        try {
            String name = request.getName();
            personValidator.validName(name);

            long cc = personValidator.validDocument(request.getDocument());

            long cel = personValidator.validCelphone(request.getCellphone());

            String userName = request.getUsername();
            userValidator.validUserName(userName);

            String password = request.getPassword();
            userValidator.validPassword(password);

            PersonDTO personDTO = new PersonDTO();
            personDTO.setName(name);
            personDTO.setDocument(cc);
            personDTO.setCelphone(cel);

            UserDTO userDTO = new UserDTO();
            userDTO.setPersonId(personDTO);
            userDTO.setUsername(userName);
            userDTO.setPassword(password);
            userDTO.setRol("Partner");

            this.service.createPartner(userDTO, personDTO);

            return new ResponseEntity<>("socio creado exitosamente", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/")
    public String vive() {
        return "vive";

    }

// Llamar al método de servicio para crear el partner
// private void createGuest() throws Exception {
//    System.out.println("ingrese el nombre del invitado: ");
//  String name = Utils.getReader().nextLine();
//  PersonValidator.validName(name);
//  System.out.println("ingrese la cedula del invitado: ");
//  long cc = PersonValidator.validDocument(Utils.getReader().nextLine());
//  System.out.println("ingrese el numero de celular del invitado: ");
//  long cel = PersonValidator.validCelphone(Utils.getReader().nextLine());
//  System.out.println("ingrese el nombre de usuario del invitado: ");
//  String userName = Utils.getReader().nextLine();
//  userValidator.validUserName(userName);
//  System.out.println("ingrese la contraseña");
//  String password = Utils.getReader().nextLine();
//  userValidator.validPassword(password);
//  System.out.println("ingrese el ID del socio que lo invita: ");
//  long ID = partnerValidator.valiedId(Utils.getReader().nextLine());
//  PersonDTO personDTO = new PersonDTO();
//  personDTO.setName(name);
//  personDTO.setCedula(cc);
//  personDTO.setCelphone(cel);
//  PartnerDTO partnerdto = new PartnerDTO();
//  partnerdto.setId(ID);
//  UserDTO userDTO = new UserDTO();
//  userDTO.setPersonId(personDTO);
//  userDTO.setUsername(userName);
//  userDTO.setRol("Invitado");
//  System.out.println("Se ha creado el usuario exitosamente.");
//}
// Revisar solicitudes VIP
    @PutMapping("/reviewvip")
    private ResponseEntity<String> reviewVIPRequests(@RequestBody ReviewVIPRequest request) throws Exception {
        try {
            // Validar el ID del socio
            long partnerId = request.getPartnerId();
            if (partnerId <= 0) {
                return new ResponseEntity<>("ID de socio inválido", HttpStatus.BAD_REQUEST);
            }

            // Buscar el socio
            PartnerDTO partnerDTO = service.findPartnerById(partnerId);
            if (partnerDTO == null) {
                return new ResponseEntity<>("No se encontró un socio con el ID proporcionado", HttpStatus.NOT_FOUND);
            }

            // Verificar si tiene una solicitud VIP pendiente
            if (!"vip_pendiente".equalsIgnoreCase(partnerDTO.getTypeSuscription())) {
                return new ResponseEntity<>("El socio no tiene una solicitud de promoción pendiente", HttpStatus.BAD_REQUEST);
            }

            // Verificar disponibilidad de cupos VIP
            // Aprobar la solicitud
            service.approveVIPRequest(partnerId);
            return new ResponseEntity<>("Solicitud VIP aprobada exitosamente", HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/printPartner/invoices")
    private ResponseEntity<?> printInvoicesPartner(@RequestBody PrintnvoicesPartnerRequest request) {
        try {
            // Validate partnerId
            if (request.getPartnerId() == null || request.getPartnerId() <= 0) {
                return new ResponseEntity<>("ID de socio inválido", HttpStatus.BAD_REQUEST);
            }

            // Get all partner invoices
            List<InvoiceDTO> paidInvoices = service.getPaidInvoices(request.getPartnerId());
            List<InvoiceDTO> pendingInvoices = service.getPendingInvoices(request.getPartnerId());

            List<InvoiceDTO> allInvoices = new ArrayList<>();
            allInvoices.addAll(paidInvoices);
            allInvoices.addAll(pendingInvoices);

            // Create response structure
            Map<String, Object> response = new HashMap<>();
            response.put("partnerId", request.getPartnerId());
            response.put("invoiceCount", allInvoices.size());
            response.put("invoices", formatInvoices(allInvoices));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/printGuest/invoices")
    private ResponseEntity<?> printInvoicesGuest(@RequestBody PrintInvoicesGuestRequest request) {
        try {
            // Validar parámetros de entrada
            if (request.getUserId() == null || request.getUserId() <= 0) {
                return new ResponseEntity<>("Se requiere un ID de invitado válido", HttpStatus.BAD_REQUEST);
            }

            // Buscar el guest por userId
            GuestDTO guest = service.findGuestByUserId(request.getUserId());
            if (guest == null) {
                return new ResponseEntity<>("No se encontró el invitado con el ID proporcionado", HttpStatus.NOT_FOUND);
            }

            // Log para debugging
            System.out.println("Guest encontrado: " + guest.getId());
            System.out.println("PersonId: " + guest.getUserId().getPersonId().getId());

            // Obtener las facturas
            List<InvoiceDTO> pendingInvoices = service.getGuestPendingInvoice(guest.getId());
            List<InvoiceDTO> paidInvoices = service.getGuestPaidInvoices(guest.getId());

            // Log para debugging
            System.out.println("Facturas pendientes encontradas: "
                    + (pendingInvoices != null ? pendingInvoices.size() : 0));
            System.out.println("Facturas pagadas encontradas: "
                    + (paidInvoices != null ? paidInvoices.size() : 0));

            // Combinar todas las facturas
            List<InvoiceDTO> allInvoices = new ArrayList<>();
            if (pendingInvoices != null) {
                allInvoices.addAll(pendingInvoices);
            }
            if (paidInvoices != null) {
                allInvoices.addAll(paidInvoices);
            }

            // Crear estructura de respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("guestId", guest.getId());
            response.put("userId", request.getUserId());
            response.put("personId", guest.getUserId().getPersonId().getId());
            response.put("guestName", guest.getUserId().getPersonId().getName());
            response.put("invoiceCount", allInvoices.size());
            response.put("invoices", formatInvoices(allInvoices));

            // Log de la respuesta
            System.out.println("Respuesta generada: " + response);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace(); // Para debugging
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// 4. Asegurarse de que el formatInvoices maneje correctamente los datos
    private List<Map<String, Object>> formatInvoices(List<InvoiceDTO> invoices) {
        List<Map<String, Object>> formattedInvoices = new ArrayList<>();

        if (invoices != null && !invoices.isEmpty()) {
            for (InvoiceDTO invoice : invoices) {
                Map<String, Object> invoiceMap = new HashMap<>();
                invoiceMap.put("id", invoice.getId());
                invoiceMap.put("creationDate", invoice.getCreationDate());
                invoiceMap.put("amount", invoice.getAmount());
                invoiceMap.put("status", invoice.isStatus() ? "PAGADA" : "PENDIENTE");

                if (invoice.getPersonId() != null) {
                    invoiceMap.put("personId", invoice.getPersonId().getId());
                    invoiceMap.put("personName", invoice.getPersonId().getName());
                }

                formattedInvoices.add(invoiceMap);
            }
     
        }

        return formattedInvoices;
    }

    //imprimir facturas
    @GetMapping("/printinvoices")
    private ResponseEntity<?> printInvoices(@RequestBody PrintInvoicesRequest request) {
        try {
            // Validate request
            if (request.getPartnerId() == null && request.getGuestId() == null) {
                return new ResponseEntity<>("Se requiere un ID de socio o invitado", HttpStatus.BAD_REQUEST);
            }

            Map<String, Object> response = new HashMap<>();
            List<InvoiceDTO> allInvoices = new ArrayList<>();
            double totalAmount = 0;

            // If partnerId is provided
            if (request.getPartnerId() != null && request.getPartnerId() > 0) {
                // Get partner's total amount including guest invoices
                totalAmount = service.getTotalInvoicesAmount(request.getPartnerId());

                // Get partner's invoices
                List<InvoiceDTO> paidInvoices = service.getPaidInvoices(request.getPartnerId());
                List<InvoiceDTO> pendingInvoices = service.getPendingInvoices(request.getPartnerId());

                if (paidInvoices != null) {
                    allInvoices.addAll(paidInvoices);
                }
                if (pendingInvoices != null) {
                    allInvoices.addAll(pendingInvoices);
                }

                // Get partner's guests' invoices
                PartnerDTO partner = service.findPartnerById(request.getPartnerId());
                response.put("partnerName", partner.getUserId().getPersonId().getName());
                response.put("partnerId", request.getPartnerId());
            } // If guestId is provided
            else if (request.getGuestId() != null && request.getGuestId() > 0) {
                GuestDTO guest = service.findGuestByUserId(request.getGuestId());
                if (guest == null) {
                    return new ResponseEntity<>("No se encontró el invitado con el ID proporcionado", HttpStatus.NOT_FOUND);
                }

                List<InvoiceDTO> guestPendingInvoices = service.getGuestPendingInvoice(guest.getId());
                List<InvoiceDTO> guestPaidInvoices = service.getGuestPaidInvoices(guest.getId());

                if (guestPendingInvoices != null) {
                    allInvoices.addAll(guestPendingInvoices);
                }
                if (guestPaidInvoices != null) {
                    allInvoices.addAll(guestPaidInvoices);
                }

                for (InvoiceDTO invoice : allInvoices) {
                    totalAmount += invoice.getAmount();
                }

                response.put("guestName", guest.getUserId().getPersonId().getName());
                response.put("guestId", request.getGuestId());
            }

            // Add common response data
            response.put("invoiceCount", allInvoices.size());
            response.put("totalAmount", totalAmount);
            response.put("invoices", formatInvoices(allInvoices));

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
