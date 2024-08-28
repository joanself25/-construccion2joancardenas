/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appcs2.App.Controller.validator;

/**
 *
 * @author CLAUDIA
 */
public class PartnerValidator extends CommonsValidator {

    public PartnerValidator() {
        super();
    }

    public long validid(String id) throws Exception {

        return super.isValidlong("id del socio ", id);
    }

    public long funds(String funds) throws Exception {
        return super.isValidlong("plata  agregada del socio ", funds);
    }

    public double validFundsMoney(String fundsMoney) throws Exception {
        return super.isValidinteger("este son los fondos disponibles del socio ", fundsMoney);
    }

    public void validTypeSuscription(String type) throws Exception {
        super.isValidString("este es el tipo de suscripcion del socio", type);
    }

}
