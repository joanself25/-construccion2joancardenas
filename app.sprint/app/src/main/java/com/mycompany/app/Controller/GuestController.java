package com.mycompany.app.Controller;

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
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
        boolean session = true;
        while (session) {
            session = guestSessio();
        }

    }

    private boolean guestSessio() {
        try {
            System.out.println(MENU);
            String option = Utils.getReader().nextLine();
            return menu(option);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    private boolean menu(String option) throws Exception {
        switch (option) {
            case "1": {
                this.converGuestoSocios();
                return true;
            }
            /* case "2": {
                this.makeConsumption();// Ver historial de facturas
                return true;
            }*/

            case "2": {
                this.viewInvoiceGuestHistory();
                return true;
            }
            case "3": {
                this.createInvoiceGuest();
                return true;
            }

            case "4": {
                System.out.println("Se ha cerrado sesión");
                return false;

            }

            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }

    }

    private void createInvoiceGuest() throws Exception {
        try {
            // Verificar que hay un usuario logueado y obtener su ID
            if (ClubService.user == null) {
                throw new Exception("No hay un usuario autenticado actualmente.");
            }

            long currentUserId = ClubService.user.getId();

            // Obtener el guest actual usando el ID del usuario logueado
            GuestDTO guestDto = null;
            try {
                guestDto = service.findGuestByUserId(currentUserId);
                if (guestDto == null || guestDto.getUserId() == null) {
                    throw new Exception("No se encontró un invitado válido asociado al usuario actual.");
                }
            } catch (Exception e) {
                throw new Exception("Error al buscar el invitado: " + e.getMessage());
            }

            // Obtener y validar el partner asociado al guest
            PartnerDTO partnerDto = guestDto.getPartnerId();
            if (partnerDto == null) {
                throw new Exception("No se encontró un socio asociado a este invitado.");
            }

            // Validar que el invitado esté activo
            if (!guestDto.isStatus()) {
                throw new Exception("El invitado no está activo actualmente.");
            }

            System.out.print("Cantidad de items de la factura: ");
            int item = invoiceDetailValidator.validNumItems(Utils.getReader().nextLine());

            // Crear la factura con todos los datos necesarios
            InvoiceDTO invoiceDto = new InvoiceDTO();
            invoiceDto.setCreationDate(new Date(System.currentTimeMillis()));
            invoiceDto.setStatus(false);
            invoiceDto.setPartnerId(partnerDto);
            invoiceDto.setGuestid(guestDto);

            // Asegurarse de que el PersonDTO está establecido correctamente
            if (guestDto.getUserId().getPersonId() == null) {
                throw new Exception("Error: No se encontró la información personal del invitado.");
            }
            invoiceDto.setPersonId(guestDto.getUserId().getPersonId());

            List<InvoiceDetailDTO> details = new ArrayList<>();
            double total = 0;

            for (int i = 1; i <= item; i++) {
                InvoiceDetailDTO invoiceDetailDto = new InvoiceDetailDTO();
                invoiceDetailDto.setItem(i);

                System.out.print("Ingrese la descripción del item " + i + ": ");
                String description = Utils.getReader().nextLine();
                invoiceDetailDto.setDescription(description);

                System.out.print("Ingrese el valor del item " + i + ": ");
                double value = invoiceDetailValidator.validValueItems(Utils.getReader().nextLine());
                invoiceDetailDto.setAmount(value);
                invoiceDetailDto.setInvoiceId(invoiceDto);

                total += value;
                details.add(invoiceDetailDto);
            }

            invoiceDto.setAmount(total);

            // Crear la factura con sus detalles
            service.createInvoiceGuest(invoiceDto, details);
            System.out.println("Factura creada exitosamente por un total de: $" + total);

        } catch (Exception e) {
            System.out.println("Error al crear la factura: " + e.getMessage());
            throw e;
        }
    }

    private void viewInvoiceGuestHistory() throws Exception {
        GuestDTO guestDto = service.findGuestByUserId(ClubService.user.getId());
        List<InvoiceDTO> invoices = service.getGuestInvoices(guestDto.getId());
        for (InvoiceDTO invoice : invoices) {
            System.out.println("Invoice ID: " + invoice.getId() + ", Amount: " + invoice.getAmount() + ", Date: " + invoice.getCreationDate());
        }
    }

    private void converGuestoSocios() throws Exception {
        System.out.print("¿Está seguro que desea convertirse en socio? (si/no): ");
        String confirmation = Utils.getReader().nextLine();

        if (confirmation.equalsIgnoreCase("si")) {
            long userId = ClubService.user.getId();
            try {
                this.service.convertGuestToPartner(userId);
                System.out.println("¡Felicidades! Has sido convertido exitosamente a socio.");
                System.out.println("Tipo de suscripción: Regular");
            } catch (Exception e) {
                System.out.println("\nNo se pudo completar la conversión a socio:");
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Operación cancelada.");
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
