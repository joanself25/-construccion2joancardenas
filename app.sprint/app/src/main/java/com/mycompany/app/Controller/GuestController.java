package com.mycompany.app.Controller;

import com.mycompany.app.Controller.Request.CreateInvoicesDetailsRequest;
import com.mycompany.app.Controller.Request.CreateInvoicesRequest;
import com.mycompany.app.Controller.Request.ConvertGuesToPartnerRequest;
import com.mycompany.app.Controller.Validator.GuestValidator;
import java.sql.Date;
import com.mycompany.app.Controller.Validator.PersonValidator;
import com.mycompany.app.Controller.Validator.InvoiceDetailValidator;
import com.mycompany.app.Controller.Validator.InvoiceValidator;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.InvoiceDetailDTO;
import com.mycompany.app.Controller.Validator.PartnerValidator;
import com.mycompany.app.Dto.GuestDTO;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.service.ClubService;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Getter
@Setter
@NoArgsConstructor
public class GuestController implements ControllerInterface {

    @Autowired
    private PersonValidator personValidator;

    @Autowired
    private InvoiceValidator invoiceValidator;

    @Autowired
    private InvoiceDetailValidator invoiceDetailValidator;

    @Autowired
    private PartnerValidator partnerValidator;

    @Autowired
    private GuestValidator guesvalidator;

    @Autowired
    private ClubService service;

    private static final String MENU = "Ingrese la opcion accion que desea hacer: \n 1. conversion de invitado a socio \n 2.historial factura  \n 3. crear factura de invitado   \n 4.cerrar sesion.";

    @Override
    public void session() throws Exception {

    }

    @PostMapping("/invoicesguest")
    private ResponseEntity<String> createInvoiceGuest(@RequestBody CreateInvoicesRequest request) throws Exception {
        try {
            // Validate guestId
            long userId = guesvalidator.isValidinteger("id del invitado", request.getUserId());

            // Find guest
            GuestDTO guestDto = service.findGuestByUserId(userId);
            if (guestDto == null) {
                throw new Exception("No se encontró ningún invitado con el ID: " + userId);
            }
            // if (!guestDto.isStatus()) {
            //    return new ResponseEntity<>("El invitado no está activo actualmente", HttpStatus.BAD_REQUEST);
            //}

            // Validate guest has associated user and person
            if (guestDto.getUserId() == null || guestDto.getUserId().getPersonId() == null) {
                return new ResponseEntity<>("El invitado no tiene datos de usuario o persona asociados", HttpStatus.BAD_REQUEST);
            }

            // Validate partner association
            PartnerDTO partnerDto = guestDto.getPartnerId();
            if (partnerDto == null) {
                return new ResponseEntity<>("No se encontró un socio asociado a este invitado", HttpStatus.BAD_REQUEST);
            }

            // Create invoice
            InvoiceDTO invoiceDto = new InvoiceDTO();
            invoiceDto.setCreationDate(new Date(System.currentTimeMillis()));
            invoiceDto.setStatus(false);
            invoiceDto.setPartnerId(partnerDto);
            invoiceDto.setGuestid(guestDto);
            invoiceDto.setPersonId(guestDto.getUserId().getPersonId());

            // Process invoice details
            List<InvoiceDetailDTO> details = new ArrayList<>();
            double total = 0.0;

            if (request.getDetails() == null || request.getDetails().isEmpty()) {
                return new ResponseEntity<>("Debe incluir al menos un detalle en la factura", HttpStatus.BAD_REQUEST);
            }

            for (CreateInvoicesDetailsRequest detail : request.getDetails()) {
                try {
                    // Validate item number
                    int itemNumber = invoiceDetailValidator.isValidinteger("número de item", detail.getItem());

                    // Validate description
                    String description = detail.getDescription();
                    invoiceDetailValidator.validDescripcion(description);

                    // Validate amount
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
            service.createInvoiceGuest(invoiceDto, details);
            return new ResponseEntity<>("Factura de invitado creada exitosamente. Total: " + total, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/convert-guest")
    private ResponseEntity<?> convertGuestToPartner(@RequestBody ConvertGuesToPartnerRequest request) {
        try {
            // Validate request
            if (request.getUserId() == null || request.getUserId() <= 0) {
                return new ResponseEntity<>("Se requiere un ID de usuario válido", HttpStatus.BAD_REQUEST);
            }

            // Attempt to convert guest to partner
            service.convertGuestToPartner(request.getUserId());

            // Create success response
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Conversión exitosa de invitado a socio");
            response.put("userId", request.getUserId());
            response.put("status", "SUCCESS");

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Create error response with detailed message
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error en la conversión de invitado a socio");
            errorResponse.put("error", e.getMessage());
            errorResponse.put("userId", request.getUserId());
            errorResponse.put("status", "ERROR");

            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}


/*private void makeConsumption() throws Exception {
        System.out.println("Bienvenido al área de consumo de la discoteca.");
        System.out.println();
        System.out.println("Por favor, seleccione los productos para consumir:");

        System.out.println("1. Cerveza - $5");
        System.out.println("2. Cóctel - $8");
        System.out.println("3. Agua mineral - $2");
        System.out.println("4. Refresco - $3");
        System.out.println("5. Snack - $4");
        System.out.println("0. Finalizar selección");

        List<Integer> selectedProducts = new ArrayList<>();

        while (true) {
            System.out.println("Ingrese el número del producto (0 para finalizar):");
            int productNumber = Integer.parseInt(Utils.getReader().nextLine());

            if (productNumber == 0) {
                break;
            }

            if (productNumber < 1 || productNumber > 5) {
                System.out.println("Producto no válido. Por favor, seleccione un número entre 1 y 5.");
                continue;
            }

            selectedProducts.add(productNumber);
        }

        if (selectedProducts.isEmpty()) {
            System.out.println("No se seleccionaron productos. Operación cancelada.");
            return;
        }

        service.makeConsumption(ClubService.user.getId(), selectedProducts);
    }

}
 */
