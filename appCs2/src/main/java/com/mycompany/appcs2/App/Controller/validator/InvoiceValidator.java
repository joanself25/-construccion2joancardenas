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
public class InvoiceValidator extends CommonsValidator {

    public InvoiceValidator() {
        super();

    }

    public long validID(String id) throws Exception {
        return super.isValidlong("id de la factura", id);

    }

    public double validAmount(String amount) throws Exception {
        return super.isValidDouble("el monto de la factura ", amount);
    }

}
