package com.mycompany.appcs2.App.Controller;

import com.mycompany.appcs2.App.Controller.Validator.PartnerValidator;
import com.mycompany.appcs2.App.Controller.Validator.GuestValidator;
import com.mycompany.appcs2.App.Controller.Validator.InvoiceDetailValidator;
import com.mycompany.appcs2.App.Controller.Validator.PersonValidator;
import com.mycompany.appcs2.App.Controller.Validator.InvoiceValidator;
import com.mycompany.appcs2.App.Controller.Validator.UserValidator;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Dto.GuestDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.service.Service;
import java.sql.Date;
import java.sql.SQLException;
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
    private UserValidator uservalidator;
    private PartnerDTO currentPartner;
    private Service service;
    private UserDTO currentUser;

    private static final String MENU = "ingrese la opcion que desea ejecutar: 1. crear factura\\n. 2.  gestion de fondos\\n . 3. crear invitados\\n . 4. Cambio de suscripcion\\n . "
            + " 5. baja de socio\\n . 6. Activar invitado\\n .7.Desactivar invitado\\n .8. solicitar Promoción()\\n "
            + ".\\n,9. cargar fondos\\n. 10. Cerrar sesión";

    public partnerController() throws SQLException {
        this.partnervalidator = new PartnerValidator();
        this.guestvalidator = new GuestValidator();
        this.invoicedetailvalidator = new InvoiceDetailValidator();
        this.invoicevalidator = new InvoiceValidator();
        this.personvalidator = new PersonValidator();
        this.uservalidator = new UserValidator();
        this.service = new Service();
    }

    public void startSession(UserDTO user) throws Exception {
        this.currentUser = user;
        System.out.println("Bienvenido al menú de socio, " + user.getUsername());
        session();
    }

    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
            session = partner();
        }

    }

    private boolean partner() {
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
                this.createInvoicePartner();
            }
            case "2": {
                this.managementFunds();
                return true;
            }
            case "3": {
                this.createGuest();
                return true;
            }
            case "4": {
                this.changeSubscription();//service
                return true;
            }

            case "5": {
                this.lowPartner();
                return true;
            }
            case "6": {
                this.activateGuest();
                return true;
            }
            case "7": {
                this.deactivateGuest();
                return true;
            }

            case "8": {
                this.requestPromotion();
                return true;
            }
            case "9":
                this.uploadFunds();
                 {
                    return true;
                }
            case "10": {
                System.out.println("Se ha cerrado sesión");
                return false;

            }

            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }

    }
    PartnerDTO partnerdto = new PartnerDTO();
    GuestDTO guestdto = new GuestDTO();

    private void createInvoicePartner() throws Exception {
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
        this.service.createInvoice(invoiceDto);

        System.out.println("se creo la factura");

    }

    private void managementFunds() throws Exception {
        System.out.println("  cuanta plata quieres agregar ");
        long funds = partnervalidator.funds(Utils.getReader().nextLine());
        PartnerDTO partnerdto = new PartnerDTO();
        partnerdto.setfundsMoney(funds);

    }

    private void createGuest() throws Exception {
        System.out.println("ingrese el nombre del invitado: ");
        String name = Utils.getReader().nextLine();
        personvalidator.validName(name);
        System.out.println("ingrese la cedula del invitado: ");
        long cc = personvalidator.validDocument(Utils.getReader().nextLine());
        System.out.println("ingrese el numero de celular del invitado: ");
        long cel = personvalidator.validCelphone(Utils.getReader().nextLine());
        System.out.println("ingrese el nombre de usuario del invitado: ");
        String userName = Utils.getReader().nextLine();
        uservalidator.validUserName(userName);
        System.out.println("ingrese la contraseña");
        String password = Utils.getReader().nextLine();
        uservalidator.validPassword(password);
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(name);
        personDTO.setCedula(cc);
        personDTO.setCelphone(cel);
        UserDTO userDTO = new UserDTO();
        userDTO.setPersonId(personDTO);
        userDTO.setUsername(userName);
        userDTO.setRol("Invitado");
        System.out.println("Se ha creado el usuario exitosamente.");
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

    private void activateGuest() throws Exception {
        System.out.println("Ingrese el ID del invitado a activar:");
        long guestId = Long.parseLong(Utils.getReader().nextLine());
        Service service = new Service();
        service.activateGuest(guestId);
        System.out.println("Invitado activado exitosamente.");
    }

    private void deactivateGuest() throws Exception {
        System.out.println("Ingrese el ID del invitado a desactivar:");
        long guestId = Long.parseLong(Utils.getReader().nextLine());
        Service service = new Service();
        service.deactivateGuest(guestId);
        System.out.println("Invitado desactivado exitosamente.");
    }

    private void lowPartner() throws Exception {
        System.out.println("¿Está seguro que desea solicitar la baja como socio? (si/no)");
        String response = Utils.getReader().nextLine().toUpperCase();
        if (response.equals("si")) {
            Service service = new Service();
            service.lowPartner(Service.user.getId());
            System.out.println("Solicitud de baja enviada. Un administrador la revisará pronto.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void requestPromotion() throws Exception {
        System.out.println("¿Está seguro que desea solicitar una promoción? (S/N)");
        String response = Utils.getReader().nextLine().toUpperCase();
        if (response.equals("S")) {
            Service service = new Service();
            service.requestPromotion(Service.user.getId());
            System.out.println("Solicitud de promoción enviada. Un administrador la revisará pronto.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void uploadFunds() throws Exception {
        System.out.println("Ingrese la cantidad de fondos a subir:");
        double amount = Double.parseDouble(Utils.getReader().nextLine());
        Service service = new Service();
        service.uploadFunds(Service.user.getId(), amount);
        System.out.println("Fondos subidos exitosamente.");
    }

}
