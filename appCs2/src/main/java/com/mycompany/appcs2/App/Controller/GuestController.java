
package com.mycompany.appcs2.App.Controller;

import java.sql.Date;
import com.mycompany.appcs2.App.Controller.validator.PersonValidator;
import com.mycompany.appcs2.App.Controller.validator.InvoiceDetailValidator;
import com.mycompany.appcs2.App.Controller.validator.InvoiceValidator;
import com.mycompany.appcs2.App.Controller.validator.PartnerValidator;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Controller.validator.PartnerValidator;

import java.util.ArrayList;
import java.util.List;

public class GuestController implements ControllerInterface {

    private PersonValidator personValidator;
    private InvoiceValidator invoiceValidator;
    private InvoiceDetailValidator invoiceDetailValidator;
    private PartnerValidator partnerValidator;
    private static final String MENU = "Ingrese la opcion accion que desea hacer 1. para ver historial de facturas . 2.conversion de invitado a socios.3. cerrar sesion ";

    @Override

    public void session() throws Exception {
        boolean session = true;
        while (session) {
            session = guestSession();
        }

    }

    private boolean guestSession() {
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
                this.converGuestSocios();

                return true;
            }
            case "3":
                System.out.println(" cerrar sesion ");
                return false;
            default: {
                System.out.println("opcion invalida");
                return true;
            }
        }

    }

    private void converGuestSocios() throws Exception {
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

}
