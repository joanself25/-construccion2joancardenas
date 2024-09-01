/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appcs2.App.Controller.Validator;

import com.mycompany.appcs2.App.Controller.Validator.CommonsValidator;

/**
 *
 * @author CLAUDIA
 */
public class PersonValidator extends CommonsValidator {

    public PersonValidator() {
        super();
    }

    public void validId(String id) throws Exception {
        super.isValidinteger("el id de la persona ", id);
    }

    public void validName(String name) throws Exception {
        super.isValidString("el nombre de la persona ", name);
    }

    public long validDocument(String cedula) throws Exception {
        return super.isValidlong("la cedula de la persona ", cedula);
    }

    public int validAge(String age) throws Exception {
        return super.isValidinteger("la edad de la persona ", age);
    }

    public long validCelphone(String celu) throws Exception {
        return super.isValidlong("este  es el celular de la persona ", celu);
    }

}
