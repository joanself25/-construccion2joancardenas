package com.mycompany.app.Controller;

import com.mycompany.app.Controller.Request.CreateUserRequest;
import com.mycompany.app.Controller.Request.PrintAll_InvoicesRequest;
import com.mycompany.app.Controller.Request.PrintInvoicesGuestRequest;
import com.mycompany.app.Controller.Request.PrintnvoicesPartnerRequest;
import com.mycompany.app.Controller.Request.PrintInvoicesRequest;
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
import com.mycompany.app.dao.interfaces.GuestDao;
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

    @Autowired
    private GuestDao guestDao;

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

    @GetMapping("/printinvoices")
    private ResponseEntity<?> printInvoices(@RequestBody PrintAll_InvoicesRequest request) {
        try {
            // Validate partnerId
            if (request.getPartnerId() == null || request.getPartnerId() <= 0) {
                return new ResponseEntity<>("Se requiere un ID de socio válido", HttpStatus.BAD_REQUEST);
            }

            // Initialize response structure
            Map<String, Object> response = new HashMap<>();
            List<InvoiceDTO> partnerInvoices = new ArrayList<>();
            List<InvoiceDTO> guestInvoices = new ArrayList<>();

            // Find partner
            try {
                PartnerDTO partner = service.findPartnerById(request.getPartnerId());
                if (partner == null) {
                    return new ResponseEntity<>("Socio no encontrado", HttpStatus.NOT_FOUND);
                }

                // Get partner's invoices
                List<InvoiceDTO> partnerPaidInvoices = service.getPaidInvoices(partner.getId());
                List<InvoiceDTO> partnerPendingInvoices = service.getPendingInvoices(partner.getId());

                // Partner information
                Map<String, Object> partnerInfo = new HashMap<>();
                partnerInfo.put("partnerId", partner.getId());
                if (partner.getUserId() != null && partner.getUserId().getPersonId() != null) {
                    partnerInfo.put("partnerName", partner.getUserId().getPersonId().getName());
                }

                // Add partner's invoices
                if (partnerPaidInvoices != null) {
                    partnerInvoices.addAll(partnerPaidInvoices);
                }
                if (partnerPendingInvoices != null) {
                    partnerInvoices.addAll(partnerPendingInvoices);
                }

                // Calculate partner statistics
                Map<String, Object> partnerStats = calculateStatistics(partnerInvoices);
                partnerInfo.put("statistics", partnerStats);
                partnerInfo.put("invoices", formatInvoices(partnerInvoices));
                response.put("partnerInformation", partnerInfo);

                // Get associated guests and their invoices
                List<GuestDTO> guests = guestDao.findGuestsByPartnerId(partner.getId());
                if (guests != null && !guests.isEmpty()) {
                    List<Map<String, Object>> guestsList = new ArrayList<>();

                    for (GuestDTO guestDTO : guests) {
                        Map<String, Object> guestInfo = new HashMap<>();
                        guestInfo.put("guestId", guestDTO.getId());
                        if (guestDTO.getUserId() != null
                                && guestDTO.getUserId().getPersonId() != null) {
                            guestInfo.put("guestName",
                                    guestDTO.getUserId().getPersonId().getName());
                        }

                        // Get guest invoices
                        List<InvoiceDTO> currentGuestInvoices = new ArrayList<>();
                        List<InvoiceDTO> guestPaidInvoices = service.getGuestPaidInvoices(guestDTO.getId());
                        List<InvoiceDTO> guestPendingInvoices = service.getGuestPendingInvoice(guestDTO.getId());

                        if (guestPaidInvoices != null) {
                            currentGuestInvoices.addAll(guestPaidInvoices);
                        }
                        if (guestPendingInvoices != null) {
                            currentGuestInvoices.addAll(guestPendingInvoices);
                        }

                        guestInvoices.addAll(currentGuestInvoices);

                        // Calculate guest statistics
                        Map<String, Object> guestStats = calculateStatistics(currentGuestInvoices);
                        guestInfo.put("statistics", guestStats);
                        guestInfo.put("invoices", formatInvoices(currentGuestInvoices));
                        guestsList.add(guestInfo);
                    }
                    response.put("guestsInformation", guestsList);
                }

                // Add overall summary
                Map<String, Object> summary = new HashMap<>();
                summary.put("partnerInvoicesCount", partnerInvoices.size());
                summary.put("guestInvoicesCount", guestInvoices.size());
                summary.put("partnerStatistics", calculateStatistics(partnerInvoices));
                summary.put("guestsStatistics", calculateStatistics(guestInvoices));
                response.put("summary", summary);

            } catch (Exception e) {
                return new ResponseEntity<>("Error al buscar el socio: " + e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>("Error al procesar la solicitud: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// Helper method to calculate statistics
    private Map<String, Object> calculateStatistics(List<InvoiceDTO> invoices) {
        double totalAmount = 0;
        double paidAmount = 0;
        double pendingAmount = 0;
        int totalInvoices = invoices.size();
        int paidInvoices = 0;
        int pendingInvoices = 0;

        for (InvoiceDTO invoice : invoices) {
            totalAmount += invoice.getAmount();
            if (invoice.isStatus()) {
                paidInvoices++;
                paidAmount += invoice.getAmount();
            } else {
                pendingInvoices++;
                pendingAmount += invoice.getAmount();
            }
        }

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalInvoices", totalInvoices);
        statistics.put("paidInvoices", paidInvoices);
        statistics.put("pendingInvoices", pendingInvoices);
        statistics.put("totalAmount", String.format("%.2f", totalAmount));
        statistics.put("paidAmount", String.format("%.2f", paidAmount));
        statistics.put("pendingAmount", String.format("%.2f", pendingAmount));

        return statistics;
    }
}
