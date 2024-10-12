package com.mycompany.app.Controller;

import com.mycompany.app.Controller.Validator.PartnerValidator;
import com.mycompany.app.Controller.Validator.GuestValidator;
import com.mycompany.app.Controller.Validator.InvoiceDetailValidator;
import com.mycompany.app.Controller.Validator.PersonValidator;
import com.mycompany.app.Controller.Validator.InvoiceValidator;
import com.mycompany.app.Controller.Validator.UserValidator;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.InvoiceDetailDTO;
import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Dto.UserDTO;
import com.mycompany.app.service.ClubService;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// Opciones de Pago: Inmediato o pendiente para socios. 
// Pagos Pendientes por Invitados: Deben ser cubiertos por el socio. 
//
//Promoción a VIP: Generación y aprobación de lista de candidatos,notificación manual a los socios sobre el resultado.
@Controller
@Getter
@Setter
@NoArgsConstructor

public class PartnerController implements ControllerInterface {

    @Autowired
    private PersonValidator personvalidator;

    @Autowired
    private PartnerValidator partnervalidator;

    @Autowired
    private GuestValidator guestvalidator;

    @Autowired
    private InvoiceDetailValidator invoicedetailvalidator;

    @Autowired
    private InvoiceValidator invoicevalidator;

    @Autowired
    private UserValidator uservalidator;

    @Autowired
    private ClubService service;
    
    @Autowired
    private GuestController guestController;
    
    @Autowired
    private ClubService clubService;


    private PartnerDTO currentPartner;

    private UserDTO currentUser;
    
    
    

    private static final String MENU = "ingrese la opcion que desea ejecutar:  \n 1. crear invitado \n 2. Activar invitado \n 3. desactivar invitados \n 4. solicitar baja \n 5. solicitar promocion \n 6.crear factura de socio \n 7. pagar facturas pendientes   \n 8. Cerrar sesión.";
//7.gestion de fondos 

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
                this.createGuest();
                return true;
            }
            case "2": {
                this.activateGuest();
                return true;
            }
            case "3": {
                this.desactivateGuest();
                return true;
            }
            case "4": {
                this.lowPartner();
                return true;
            }

            case "5": {
                this.requestPromotion();
                return true;
            }
            case "6": {
                this.createInvoicePartner();
                return true;
            }
            /*case "7": {
                this.managementFunds();
                return true;
            }
             */
            case "7": {
                this.payInvoices();
                return true;
            }

            case "8": {
                System.out.println("Se ha cerrado sesión");
                return false;

            }

            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }

    }

    private void createInvoicePartner() throws Exception {
        try {
            // Verificar que hay un usuario logueado
            if (ClubService.user == null) {
                throw new Exception("No hay un usuario autenticado actualmente.");
            }

            // Obtener el socio actual usando el ID del usuario logueado
            PartnerDTO partnerDto = service.findPartnerByUserId(ClubService.user.getId());
            if (partnerDto == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual.");
            }

            System.out.print("Cantidad de items de la factura :");
            int item = invoicedetailvalidator.validNumItems(Utils.getReader().nextLine());

            // Crear la factura con todos los datos necesarios
            InvoiceDTO invoiceDto = new InvoiceDTO();
            invoiceDto.setCreationDate(new Date(System.currentTimeMillis()));
            invoiceDto.setStatus(false);
            invoiceDto.setPartnerId(partnerDto);
            // Asegurarse de que el PersonDTO está establecido
            invoiceDto.setPersonId(partnerDto.getUserId().getPersonId());

            List<InvoiceDetailDTO> details = new ArrayList<>();
            double total = 0;

            for (int i = 1; i <= item; i++) {
                InvoiceDetailDTO invoiceDetailDto = new InvoiceDetailDTO();
                invoiceDetailDto.setItem(i);

                System.out.print("Ingrese la descripción del item " + i + " :");
                String description = Utils.getReader().nextLine();
                invoiceDetailDto.setDescription(description);

                System.out.print("Ingrese el valor del item   " + i + " :");
                double value = invoicedetailvalidator.validValueItems(Utils.getReader().nextLine());
                invoiceDetailDto.setAmount(value);

                // Establecer la referencia a la factura en el detalle
                invoiceDetailDto.setInvoiceId(invoiceDto);

                total += value;
                details.add(invoiceDetailDto);
            }

            // Establecer el monto total en la factura
            invoiceDto.setAmount(total);

            // Crear la factura con sus detalles
            service.createInvoicePartner(invoiceDto, details);
            System.out.println("Factura creada exitosamente por un total de: $" + total);

        } catch (Exception e) {
            System.out.println("Error al crear la factura: " + e.getMessage());
            throw e;
        }
    }

    private void createGuest() throws Exception {
        System.out.print("Ingrese el nombre del invitado: ");
        String name = Utils.getReader().nextLine();
        personvalidator.validName(name);

        System.out.print(" ingrese la cedula del invitado: ");
        long cc = personvalidator.validDocument(Utils.getReader().nextLine());

        System.out.print("Ingrese el número de celular del invitado: ");
        long cel = personvalidator.validCelphone(Utils.getReader().nextLine());

        System.out.print("Ingrese el nombre de usuario del invitado: ");
        String userName = Utils.getReader().nextLine();
        uservalidator.validUserName(userName);

        System.out.print("Ingrese la contraseña:");
        String password = Utils.getReader().nextLine();
        uservalidator.validPassword(password);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setName(name);
        personDTO.setCelphone(cel);
        personDTO.setDocument(cc);

        UserDTO userDTO = new UserDTO();
        userDTO.setPersonId(personDTO);
        userDTO.setUsername(userName);
        userDTO.setPassword(password);
        userDTO.setRol("invitado");

        if (ClubService.user == null) {
            throw new Exception("No hay un usuario autenticado actualmente.");
        }

        long userId = ClubService.user.getId();

        try {
            this.service.createGuest(userDTO, userId, personDTO);
            System.out.println("Invitado creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al crear el invitado: " + e.getMessage());
        }
         try {
            GuestController guestController = new GuestController();
            guestController.session();
        } catch (Exception e) {
            System.out.println("Error al iniciar sesión como invitado: " + e.getMessage());
        }
    }

    private void activateGuest() throws Exception {
        try {
            System.out.print("Ingrese el ID del invitado a activar :");
            long guestId = guestvalidator.validId(Utils.getReader().nextLine());

            service.activateGuest(guestId);
            System.out.println("Invitado activado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al activar el invitado " );
           
        }
    }

    private void desactivateGuest() throws Exception {
        try {
            System.out.print("Ingrese el ID del invitado a desactivar :");
            long guestId = guestvalidator.validId(Utils.getReader().nextLine());

            service.desactivateGuest(guestId);
            System.out.println("Invitado desactivado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al desactivar el invitado");
            
        }
    }

    private void lowPartner() throws Exception {
        try {
            long userId = ClubService.user.getId();

            PartnerDTO partnerDTO = service.findPartnerByUserId(userId);

            if (partnerDTO == null) {
                throw new Exception("No se encontró un socio asociado al usuario actual.");
            }

            service.lowPartner(userId);

            System.out.println("Se ha dado de baja exitosamente. Todos sus datos han sido eliminados.");

            service.logout();
        } catch (Exception e) {
            System.out.println("Error al darse de baja: " + e.getMessage());
        }
    }
    //Solicitud de promoción

    private void requestPromotion() throws Exception {
        System.out.println("¿Está seguro que desea solicitar una promoción VIP? (si/no): ");
        String response = Utils.getReader().nextLine();
        if (response.equalsIgnoreCase("si")) {
            service.requestPromotion(ClubService.user.getId());
            System.out.println("Solicitud de promoción enviada. Un administrador la revisará pronto.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void payInvoices() throws Exception {
        try {
            // Verificar que hay un usuario logueado
            if (ClubService.user == null) {
                throw new Exception("No hay un usuario autenticado actualmente.");
            }

            // Obtener el ID del usuario actual
            long userId = ClubService.user.getId();

            // Llamar al servicio para procesar el pago
            service.payInvoices(userId);

        } catch (Exception e) {
            System.out.println("Error al procesar el pago: " + e.getMessage());
            throw e;
        }
    }

    private void uploadFunds() throws Exception {
        System.out.println("Ingrese la cantidad de fondos a subir:");
        double amount = Double.parseDouble(Utils.getReader().nextLine());
        ClubService service = new ClubService();
        service.uploadFunds(ClubService.user.getId(), amount);
        System.out.println("Fondos subidos exitosamente.");
    }

    private void managementFunds() throws Exception {
        System.out.println("  cuanta plata quieres agregar ");
        long funds = partnervalidator.funds(Utils.getReader().nextLine());
        PartnerDTO partnerdto = new PartnerDTO();
        partnerdto.setfundsMoney(funds);

    }

}
