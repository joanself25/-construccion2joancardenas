package com.mycompany.app.Controller;

import com.mycompany.app.Controller.Validator.UserValidator;
import com.mycompany.app.Controller.Validator.PersonValidator;
import com.mycompany.app.Controller.Validator.PartnerValidator;
import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Dto.UserDTO;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.service.ClubService;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.service.Interface.Adminservice;
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
public class AdminController implements ControllerInterface {

    @Autowired
    private PersonValidator personValidator;
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private Adminservice service;

    @Autowired
    private PartnerValidator partnerValidator;

    @Autowired
    private PartnerController partnerController;

    private static final String MENU = "ingrese la opcion que desea \n 1.para crear socio  \n 2. Revisar solicitudes vip    \n 3. Ver historial de facturas de socios \n 4. cerrar sesion. \n";

    @Override
    public void session() throws Exception {
        boolean session = true;
        while (session) {
            session = menu();
        }

    }

    private boolean menu() {
        try {
            System.out.println("bienvenido " + ClubService.user.getUsername());

            System.out.print(MENU);
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
                this.createPartner();
                return true;
            }

            case "2": {
                this.reviewVIPRequests(); // Revisar solicitudes VIP
                return true;

            }
            case "3": {
                this.viewPartnerInvoiceHistory();  // ver historial de facturas del socio
                return true;

            }

            case "4":
                System.out.println(" se ha cerrado sesion ");
                return false;
            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }
    }

    private void createPartner() throws Exception {
        System.out.print("Ingrese el nombre del socio : ");
        String name = Utils.getReader().nextLine();
        personValidator.validName(name);

        System.out.print("Ingrese la cédula del socio : ");
        long cc = personValidator.validDocument(Utils.getReader().nextLine());

        System.out.print("Ingrese el número de celular del socio :");
        long cel = personValidator.validCelphone(Utils.getReader().nextLine());

        System.out.print("Ingrese el nombre de usuario del socio : ");
        String userName = Utils.getReader().nextLine();
        userValidator.validUserName(userName);

        System.out.print("Ingrese la contraseña :");
        String password = Utils.getReader().nextLine();
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

        // Establecer el usuario actual como el socio recién creado
        ClubService.user = userDTO;

        // Inicializar y ejecutar el PartnerController
      try {
            PartnerController  partnerController = new PartnerController();
            partnerController.session();
        } catch (Exception e) {
            System.out.println("Error al iniciar sesión como invitado: " + e.getMessage());
        }
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
    private void reviewVIPRequests() throws Exception {
        List<PartnerDTO> pendingRequests = service.getPendingVIPRequests();
        if (pendingRequests.isEmpty()) {
            System.out.println("No hay solicitudes VIP pendientes.");
            return;
        }

        for (PartnerDTO partner : pendingRequests) {
            System.out.println("\n=== Solicitud VIP de " + partner.getUserId().getUsername() + " ===");
            System.out.println("Total de facturas pagadas: $" + service.getTotalPaidInvoices(partner.getId()));
            System.out.println("Fondos actuales: $" + partner.getfundsMoney());
            System.out.println("Fecha de afiliación: " + partner.getDateCreated());
            System.out.println("Fecha de solicitud VIP: " + partner.getDateCreated());

            System.out.println("\n¿Desea aprobar la solicitud VIP? (si/no) : ");
            String response = Utils.getReader().nextLine().toLowerCase();

            if (response.equals("si")) {
                service.approveVIPRequest(partner.getId());
                System.out.println("Solicitud VIP aprobada exitosamente.");
            } else {
                service.rejectVIPRequest(partner.getId());
                System.out.println("Solicitud VIP rechazada.");
            }
        }
    }

    //Ver historial de facturas de socios
    private void viewPartnerInvoiceHistory() throws Exception {
        System.out.println("Ingrese el ID del socio:");
        long partnerId = Utils.getReader().nextLong();
        List<InvoiceDTO> paidInvoices = service.getPaidInvoices(partnerId);
        List<InvoiceDTO> pendingInvoices = service.getPendingInvoices(partnerId);

        System.out.println("Facturas pagadas:");
        printInvoices(paidInvoices);

        System.out.println("Facturas pendientes:");
        printInvoices(pendingInvoices);
    }

    //imprimir facturas
    private void printInvoices(List<InvoiceDTO> invoices) {
        if (invoices.isEmpty()) {
            System.out.println("No se encontraron facturas.");
        } else {
            for (InvoiceDTO invoice : invoices) {
                System.out.println("ID Factura: " + invoice.getId());
                System.out.println("Fecha: " + invoice.getCreationDate());
                System.out.println("Monto: $" + invoice.getAmount());
                System.out.println("Estado: " + (invoice.isStatus() ? "Pagada" : "Pendiente"));
                System.out.println("--------------------");
            }
        }
    }

}
