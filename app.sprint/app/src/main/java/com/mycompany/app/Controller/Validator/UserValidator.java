/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Controller.Validator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class UserValidator extends CommonsValidator {

    public void validUserName(String userName) throws Exception {
        super.isValidString("el nombre de usuario ", userName);
    }

    public void validPassword(String password) throws Exception {
        super.isValidString("la contraseña de usuario ", password);
    }

    public void validRole(String role) throws Exception {
        super.isValidString("el rol de usuario ", role);
    }

}
