package com.mycompany.app;

import com.mycompany.app.Controller.LoginController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppApplication implements CommandLineRunner {
    
  
    // para solicitar promocion no debe de haber facturas pendientes
    // y si  tiene facturas que esten pagadas^^^^{debo pagar  a ver si ya me acepta solicitud de promocion}
    
   // MIRAR SI AL HACER LA BAJA DEL SOCIO ME APARECE EL MENSAJE DE QUE EL PARTNER TIENE FACTURAS
    //PENDIENTES^^^^{debo pagar a ver si ya me elimina el socio}
    
    //Conversion de invitado asocio no se realiza si hay facturas pendientes  
    
    // MODIFICAR LA CREACION DE FACTURAS DEL INVITADO YA QUE  NO ME LO ENCUENTRA 
    // EL INVITADO GENERA CONSUMO Y LA FACTURA LE LLEGA AL SOCIO 
    
    // CREAR UN MENU Y UN METODO  EN PARTNER DONDE ME PAGUE LAS FACTURAS DE ESE MISMO SOCIO
    //Y CON ESE MISMO METODO ME PAGUE LA DEL INIVTADO( YA ESTE VA HACER POR MEDIO DE UN ID )
    @Autowired
    LoginController controller;

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);

    }
 
   @Override
    public void run(String... args) throws Exception {
        
        try {
            System.out.println("Iniciando la aplicación...");

            controller.session();
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());

        }

    }
}
