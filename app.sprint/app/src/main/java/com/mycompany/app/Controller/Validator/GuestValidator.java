/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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

public class GuestValidator extends CommonsValidator {

    public long validId(String id) throws Exception {

        return super.isValidlong("id del invitado ", id);
    }

}
