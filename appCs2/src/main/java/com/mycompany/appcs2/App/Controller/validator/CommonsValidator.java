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
public abstract class CommonsValidator {

    public void isValidString(String element, String value) throws Exception {
        if (value.equals("")) {
            throw new Exception(element + " no puede ser un valor vacio: ");
        }
    }

    public int isValidinteger(String element, String value) throws Exception {
        isValidString(element, value);
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            throw new Exception(element + " debe ser un valor valido. ");
        }
    }

    public long isValidlong(String element, String value) throws Exception {
        isValidString(element, value);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new Exception(element + " debe ser un valor valido. ");
        }
    }

    public double isValidDouble(String element, String value) throws Exception {
        isValidString(element, value);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new Exception(element + " debe ser un valor valido. ");
        }
    }

}
