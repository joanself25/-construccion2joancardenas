package com.mycompany.appcs2.App.Controller;

import com.mycompany.appcs2.App.Controller.Validator.UserValidator;
import com.mycompany.appcs2.App.Controller.Validator.PersonValidator;
import com.mycompany.appcs2.App.Controller.Validator.PartnerValidator;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.service.Service;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.service.Interface.Adminservice;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class AdminController implements ControllerInterface {

    private PersonValidator PersonValidator;
    private UserValidator userValidator;
    private Adminservice service;
    private PartnerValidator partnerValidator;
    private partnerController partnerController;
    private static final String MENU = "ingrese la opcion que desea \n 1.para crear socio  \n 2. Revisar solicitudes vip    \n 3. Ver historial de facturas de socios \n 4. Ver historial de facturas de invitado \n 5.Ejecutar promoción VIP  \n 6. cerras sesion ";

    public AdminController() throws SQLException {
        this.PersonValidator = new PersonValidator();
        this.userValidator = new UserValidator();
        this.service = new Service();
        this.partnerValidator = new PartnerValidator();
        this.partnerController = new partnerController();
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
            System.out.println("bienvenido " + Service.user.getUsername());

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
            case "4": {
                this.viewGuestInvoiceHistory(); // ver historial de facturas del invitado
                return true;

            }
            case "5": {
                this.executeVIPPromotion(); // ejecutar pomoion vip
                return true;

            }

            case "6":
                System.out.println(" se ha cerrado sesion ");
                return false;
            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }
    }

    private void createPartner() throws Exception {
        System.out.println("ingrese el nombre del socio: ");
        String name = Utils.getReader().nextLine();
        PersonValidator.validName(name);
        System.out.println("ingrese la cedula del socio: ");
        long cc = PersonValidator.validDocument(Utils.getReader().nextLine());
        System.out.println("ingrese el numero de celular del socio");
        long cel = PersonValidator.validCelphone(Utils.getReader().nextLine());
        System.out.println("ingrese el nombre de usuario del socio: ");
        String userName = Utils.getReader().nextLine();
        userValidator.validUserName(userName);
        System.out.println("ingrese la contraseña");
        String password = Utils.getReader().nextLine();
        userValidator.validPassword(password);
        //System.out.println("ingrese la fecha de afiliación del socio: ");

        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(name);
        personDTO.setCedula(cc);
        personDTO.setCelphone(cel);
        UserDTO userDTO = new UserDTO();
        userDTO.setPersonId(personDTO);
        userDTO.setUsername(userName);
        userDTO.setPassword(password);
        userDTO.setRol("Partner");

        // Llamar al método de servicio para crear el partner
        try {
            UserDTO createdUser = service.createPartn(userDTO);
            System.out.println("Se ha creado el usuario exitosamente.");

            // Iniciar la sesión del socio recién creado
            partnerController partnerController = new partnerController();
            partnerController.startSession(createdUser);
        } catch (Exception e) {
            System.out.println("Error al crear el socio: " + e.getMessage());
        }
    }

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
            System.out.println("Solicitud de " + partner.getUserId().getUsername());
            System.out.println("Total de facturas pagadas: $" + service.getTotalPaidInvoices(partner.getId()));
            System.out.println("Fondos actuales: $" + partner.getfundsMoney());
            System.out.println("Fecha de afiliación: " + partner.getDateCreated());
            System.out.println("Fecha de solicitud VIP: " + partner.getVipRequestDate());
            System.out.println("¿Aprobar solicitud? (si/no)");

            String response = Utils.getReader().nextLine().toLowerCase();
            if (response.equals("si")) {
                service.approveVIPRequest(partner.getId());
                System.out.println("Solicitud aprobada.");
            } else {
                service.rejectVIPRequest(partner.getId());
                System.out.println("Solicitud rechazada.");
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
    //Ver historial de facturas de invitado

    private void viewGuestInvoiceHistory() throws Exception {
        System.out.println("Ingrese el ID del invitado:");
        long guestId = Utils.getReader().nextLong();
        // Nota: Necesitamos implementar este método en Service y GuestDao
        List<InvoiceDTO> guestInvoices = service.getGuestInvoices(guestId);

        System.out.println("Facturas del invitado:");
        printInvoices(guestInvoices);
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
    //Ejecutar promoción VIP

    private void executeVIPPromotion() throws Exception {
        // poner solo en la service
        List<PartnerDTO> candidates = service.getPendingVIPRequests();
        if (candidates.isEmpty()) {
            System.out.println("No hay candidatos elegibles para promoción VIP en este momento.");
            return;
        }

        System.out.println("Candidatos para promoción VIP:");
        for (int i = 0; i < candidates.size(); i++) {
            PartnerDTO candidate = candidates.get(i);
            System.out.println((i + 1) + ". " + candidate.getUserId().getUsername());
            System.out.println("   Total facturas pagadas: $" + service.getTotalPaidInvoices(candidate.getId()));
            System.out.println("   Fondos actuales: $" + candidate.getfundsMoney());
            System.out.println("   Fecha de afiliación: " + candidate.getDateCreated());
        }

        System.out.println("Ingrese el número del candidato a promover (0 para cancelar):");
        int selection = Utils.getReader().nextInt();
        if (selection > 0 && selection <= candidates.size()) {
            PartnerDTO selectedCandidate = candidates.get(selection - 1);
            service.approveVIPRequest(selectedCandidate.getId());
            System.out.println("El socio " + selectedCandidate.getUserId().getUsername() + " ha sido promovido a VIP.");
        } else if (selection != 0) {
            System.out.println("Selección inválida.");
        }
    }
}
