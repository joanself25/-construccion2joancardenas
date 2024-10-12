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

/**
 *
 * @author CLAUDIA
 */
@Component
@Getter
@Setter
@NoArgsConstructor
public class PartnerValidator extends CommonsValidator {

    public long valiedId(String id) throws Exception {
        return super.isValidlong("el id del socio  ", id);

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
