/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.Controller;

import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.Controller.Validator.UserValidator;
import com.mycompany.appcs2.App.service.Interface.Loginservice;
import com.mycompany.appcs2.App.service.Service;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

public class LoginController implements ControllerInterface {

    private UserValidator userValidator;
    private Loginservice service;
    private static final String MENU = "ingrese la opcion que desea: \n 1. para iniciar sesion. \n 2. para detener la ejecucion.";
    private Map<String, ControllerInterface> roles;

    public LoginController() throws SQLException { // SE IMPLEMENTO  EL THROWS SQEXCEPTION
        // PARA QUE ME LANCE UNA EXCEPCION DE CONEXION
        // EN CASO DE QUE FALLE
        this.userValidator = new UserValidator();
        this.service = new Service(); // Make sure this is the correct implementation
        ControllerInterface adminController = new AdminController();
        ControllerInterface partnerController = new partnerController();
        ControllerInterface guestController = new GuestController();
        this.roles = new HashMap<>();
        roles.put("admin", adminController);
        roles.put("partner", partnerController);
        roles.put("guest", guestController);
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
            String option;
            option = Utils.getReader().nextLine();
            return options(option);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return true;
        }
    }

    private boolean options(String option) throws Exception {
        switch (option) {
            case "1": {
                this.login();
                return true;
            }
            case "2": {
                System.out.println("se detiene el programa");;
                return false;
            }
            default: {
                System.out.println("ingrese una opcion valida");
                return true;
            }
        }
    }

    private void login() throws Exception {
        System.out.println("ingrese el usuario");
        String userName = Utils.getReader().nextLine();
        userValidator.validUserName(userName);
        System.out.println("ingrese la contraseña");
        String password = Utils.getReader().nextLine();
        userValidator.validPassword(password);
        UserDTO userDto = new UserDTO();
        userDto.setPassword(password);
        userDto.setUsername(userName);
        this.service.login(userDto);
        if (roles.get(userDto.getRol()) == null) {
            throw new Exception("Rol invalido");
        }
        roles.get(userDto.getRol()).session();

    }

}
