package com.mycompany.appcs2.App.Controller;

import com.mycompany.appcs2.App.Controller.validator.PartnerValidator;
import com.mycompany.appcs2.App.Controller.validator.GuestValidator;
import com.mycompany.appcs2.App.Controller.validator.InvoiceDetailValidator;
import com.mycompany.appcs2.App.Controller.validator.PersonValidator;
import com.mycompany.appcs2.App.Controller.validator.InvoiceValidator;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Dto.GuestDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.service.Service;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

// Opciones de Pago: Inmediato o pendiente para socios. 
// Pagos Pendientes por Invitados: Deben ser cubiertos por el socio. 
//
//Promoción a VIP: Generación y aprobación de lista de candidatos,notificación manual a los socios sobre el resultado.
public class partnerController implements ControllerInterface {

    private PartnerValidator partnervalidator;
    private GuestValidator guestvalidator;
    private InvoiceDetailValidator invoicedetailvalidator;
    private PersonValidator personvalidator;
    private InvoiceValidator invoicevalidator;
    private static final String MENU = "ingrese la opcion que desea ejecutar: 1. crear factura . 2.  gestion de fondos . 3. Cambio de Suscripción. 4. Invitados.  5. Baja del Socio. 6. para cerrar sesion.";

    public partnerController() {
        this.partnervalidator = new PartnerValidator();
        this.guestvalidator = new GuestValidator();
        this.invoicedetailvalidator = new InvoiceDetailValidator();
        this.invoicevalidator = new InvoiceValidator();
    }

    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
            session = menu();
        }

    }

    private boolean menu() {
        try {
            System.out.println(MENU);
            String option = Utils.getReader().nextLine();
            return options(option);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    private boolean options(String option) throws Exception {
        switch (option) {

            case "1": {
                this.createInvoice();
            }
            case "2": {
                this.managementFunds();
                return true;
            }
            case "3": {
                this.changeSubscription();
                return true;
            }
            case "4": {
                this.guestPartner();
                return true;
            }
            case "5": {
                this.lowPartner();
                return true;
            }

            case "6": {
                System.out.println("Se ha cerrado sesion");
                return false;
            }
            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }

    }

    private void managementFunds() throws Exception {
        System.out.println("  cuanta plata quieres agregar ");
        long funds = partnervalidator.funds(Utils.getReader().nextLine());
        PartnerDTO partnerdto = new PartnerDTO();
        partnerdto.setfundsMoney(funds);

    }

   private void changeSubscription() throws Exception {
    System.out.println("¿Desea solicitar cambio a suscripción VIP? (S/N)");
    String response = Utils.getReader().nextLine().toUpperCase();
    if (response.equals("S")) {
        try {
            Service service = new Service();  // Asegúrate de que tienes una instancia de Service
            service.requestVIPSubscription(Service.user);
            System.out.println("Solicitud de cambio a VIP enviada. Un administrador la revisará pronto.");
        } catch (Exception e) {
            System.out.println("Error al solicitar cambio de suscripción: " + e.getMessage());
        }
    } else {
        System.out.println("Operación cancelada.");
    }
}

    private void guestPartner() throws Exception {

    }

    private void lowPartner() throws Exception {

    }

    private void createInvoice() throws Exception {
        System.out.println("cantidad de  items de la factura");
        int item = invoicedetailvalidator.validNumItems(
                Utils.getReader().nextLine());

        InvoiceDTO invoiceDto = new InvoiceDTO();
        invoiceDto.setCreationDate(new Date(System.currentTimeMillis()));
        invoiceDto.setStatus(true);
        List<InvoiceDetailDTO> details = new ArrayList<InvoiceDetailDTO>();
        double total = 0;
        double valori = 0;
        for (int i = 1; i <= item; i++) {
            InvoiceDetailDTO invoiceDtailDto = new InvoiceDetailDTO();
            invoiceDtailDto.setId(i);
            invoiceDtailDto.setInvoiceId(invoiceDto);
            System.out.println("ingrese la descripcion del item " + i);

            System.out.println("ingrese el valor del item " + i);
            valori = invoicedetailvalidator.validValueItems(Utils.getReader().nextLine());

            total += valori;
            details.add(invoiceDtailDto);
        }
        invoiceDto.setAmount(total);

        System.out.println("se creo la factura");

    }
    PartnerDTO partnerdto = new PartnerDTO();
    GuestDTO guestdto = new GuestDTO();

}
