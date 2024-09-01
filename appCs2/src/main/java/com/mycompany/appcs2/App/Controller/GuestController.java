package com.mycompany.appcs2.App.Controller;

import com.mycompany.appcs2.App.Controller.Validator.GuestValidator;
import java.sql.Date;
import com.mycompany.appcs2.App.Controller.Validator.PersonValidator;
import com.mycompany.appcs2.App.Controller.Validator.InvoiceDetailValidator;
import com.mycompany.appcs2.App.Controller.Validator.InvoiceValidator;
import com.mycompany.appcs2.App.Controller.Validator.PartnerValidator;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Controller.Validator.PartnerValidator;
import com.mycompany.appcs2.App.Dto.GuestDTO;
import com.mycompany.appcs2.App.service.Service;
import java.util.ArrayList;
import java.util.List;

public class GuestController implements ControllerInterface {

    private PersonValidator personValidator;
    private InvoiceValidator invoiceValidator;
    private InvoiceDetailValidator invoiceDetailValidator;
    private PartnerValidator partnerValidator;
    private GuestValidator guesvalidator;
    private Service service;

    private static final String MENU = "Ingrese la opcion accion que desea hacer 1. para ver historial de facturas .2. ver historial de facturas ."
            + " 3.conversion de invitado a socios.4. cerrar sesion ";

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
                this.createInvoiceGuest();
                return true;
            }
            case "2": {
                this.viewInvoiceHistory();// Ver historial de facturas
                return true;
            }
            case "3": {
                this.converGuestoSocios();
                return true;
            }
            case "4": {
                this.makeConsumption();

                return true;
            }
            case "5":
                System.out.println(" cerrar sesion ");
                return false;
            default: {
                System.out.println("opcion invalida");
                return true;
            }
        }

    }

    private void createInvoiceGuest() throws Exception {

        System.out.println("    Cantidad de items de la factura ");
        int items = invoiceDetailValidator.validNumItems(
                Utils.getReader().nextLine());

        InvoiceDTO invoicedto = new InvoiceDTO();
        invoicedto.setCreationDate(new Date(System.currentTimeMillis()));
        invoicedto.setStatus(true);

        List<InvoiceDetailDTO> detail = new ArrayList<InvoiceDetailDTO>();
        double total = 0;
        double valorI = 0;
        for (int i = 1; i <= items; i++) {
            InvoiceDetailDTO invoicedtaildto = new InvoiceDetailDTO();
            invoicedtaildto.setId(i);
            invoicedtaildto.setInvoiceId(invoicedto);

            System.out.println("   ingrese la descripcion del items:  " + i);
            System.out.println("ingrese el valor de cada items :" + i);
            valorI = invoiceDetailValidator.validValueItems(
                    Utils.getReader().nextLine());

            total += valorI;
            detail.add(invoicedtaildto);

        }
        invoicedto.setAmount(total);

        System.out.println("se creo la factura");
    }

    private void viewInvoiceHistory() throws Exception {
        GuestDTO guestDto = service.findGuestByUserId(Service.user.getId());
        List<InvoiceDTO> invoices = service.getGuestInvoices(guestDto.getId());
        for (InvoiceDTO invoice : invoices) {
            System.out.println("Invoice ID: " + invoice.getId() + ", Amount: " + invoice.getAmount() + ", Date: " + invoice.getCreationDate());
        }
    }

    private void converGuestoSocios() throws Exception {
        System.out.println("¿Está seguro que desea convertirse en socio? (S/N)");
        String confirmation = Utils.getReader().nextLine();
        if (confirmation.equalsIgnoreCase("si")) {
            service.convertGuestToPartner();
            System.out.println("¡Felicidades! Ahora eres un socio del club.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void makeConsumption() throws Exception {
        System.out.println("Bienvenido al área de consumo de la discoteca.");
        System.out.println("Por favor, seleccione dos productos para consumir:");

        System.out.println("1. Cerveza - $5");
        System.out.println("2. Cóctel - $8");
        System.out.println("3. Agua mineral - $2");
        System.out.println("4. Refresco - $3");
        System.out.println("5. Snack - $4");

        System.out.println("Ingrese el número del primer producto:");
        int product1 = Integer.parseInt(Utils.getReader().nextLine());

        System.out.println("Ingrese el número del segundo producto:");
        int product2 = Integer.parseInt(Utils.getReader().nextLine());

        List<InvoiceDetailDTO> details = new ArrayList<>();
        details.add(createInvoiceDetail(1, product1));
        details.add(createInvoiceDetail(2, product2));

        double totalAmount = details.stream().mapToDouble(InvoiceDetailDTO::getAmount).sum();

        System.out.println("El monto total de su consumo es: $" + totalAmount);

        GuestDTO guestDto = service.findGuestByUserId(Service.user.getId());

        InvoiceDTO invoiceDto = new InvoiceDTO();
        invoiceDto.setAmount(totalAmount);
        invoiceDto.setCreationDate(new Date(System.currentTimeMillis()));
        invoiceDto.setGuestid(guestDto);
        invoiceDto.setPartnerId(guestDto.getPartnerId());

        service.createInvoiceWithDetails(invoiceDto, details);
        System.out.println("Consumo registrado exitosamente.");
    }

    private InvoiceDetailDTO createInvoiceDetail(int item, int productNumber) {
        InvoiceDetailDTO detail = new InvoiceDetailDTO();
        detail.setItem(item);
        detail.setDescription(getProductName(productNumber));
        detail.setAmount(getProductPrice(productNumber));
        return detail;
    }

    private double getProductPrice(int productNumber) {
        switch (productNumber) {
            case 1:
                return 5; // Cerveza
            case 2:
                return 8; // Cóctel
            case 3:
                return 2; // Agua mineral
            case 4:
                return 3; // Refresco
            case 5:
                return 4; // Snack
            default:
                return 0;
        }
    }

    private String getProductName(int productNumber) {
        switch (productNumber) {
            case 1:
                return "Cerveza";
            case 2:
                return "Cóctel";
            case 3:
                return "Agua mineral";
            case 4:
                return "Refresco";
            case 5:
                return "Snack";
            default:
                return "Producto desconocido";
        }
    }
}
