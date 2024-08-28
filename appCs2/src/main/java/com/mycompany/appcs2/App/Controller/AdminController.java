package com.mycompany.appcs2.App.Controller;

import com.mycompany.appcs2.App.Controller.validator.UserValidator;
import com.mycompany.appcs2.App.Controller.validator.PersonValidator;
import com.mycompany.appcs2.App.Controller.validator.PartnerValidator;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.service.Service;
import com.mycompany.appcs2.App.service.Interface.Adminservice;
import java.sql.Date;
import java.util.List;

public class AdminController implements ControllerInterface {

    private PersonValidator PersonValidator;
    private UserValidator userValidator;
    private Adminservice service;
    private static final String MENU = "ingrese la opcion que desea \n 1.para crear socio  \n 2. para crear invitado \n 3. para cerrar sesion \n";

    public AdminController() {
        this.PersonValidator = new PersonValidator();
        this.userValidator = new UserValidator();
        this.service = new Service();
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
                this.createGuest();
                return true;
            }
            case "3": {
                this.reviewVIPRequests();
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
        System.out.println("ingrese la fecha de afiliación del socio: ");
        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(name);
        personDTO.setCedula(cc);
        personDTO.setCelphone(cel);
        UserDTO userDTO = new UserDTO();
        userDTO.setPersonId(personDTO);
        userDTO.setUsername(userName);
        userDTO.setPassword(password);
        userDTO.setRol("Partner");
        PartnerDTO partnerDto = new PartnerDTO();
        partnerDto.setUserId(userDTO);
        partnerDto.setfundsMoney(50000);
        partnerDto.setTypeSuscription("regular");
        partnerDto.setDateCreated(new Date(System.currentTimeMillis()));
        System.out.println("Se hace creado el usuario exitosamente.");
    }

    // Opciones de Pago: Inmediato o pendiente para socios. 
// Pagos Pendientes por Invitados: Deben ser cubiertos por el socio. 
    private void createGuest() throws Exception {
        System.out.println("ingrese el nombre del invitado: ");
        String name = Utils.getReader().nextLine();
        PersonValidator.validName(name);
        System.out.println("ingrese la cedula del invitado: ");
        long cc = PersonValidator.validDocument(Utils.getReader().nextLine());
        System.out.println("ingrese el numero de celular del invitado: ");
        long cel = PersonValidator.validCelphone(Utils.getReader().nextLine());
        System.out.println("ingrese el nombre de usuario del invitado: ");
        String userName = Utils.getReader().nextLine();
        userValidator.validUserName(userName);
        System.out.println("ingrese la contraseña");
        String password = Utils.getReader().nextLine();
        userValidator.validPassword(password);
        System.out.println("ingrese el ID del socio que lo invita: ");
        long ID = Utils.getReader().nextLong();
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
}
