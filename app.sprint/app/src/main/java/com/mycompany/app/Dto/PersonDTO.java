/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Dto;

/**
 *
 * @author Farley
 */
public class PersonDTO {

    private long id;
    private long document;
    private String name;
    private long celphone;

    public PersonDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDocument() {
        return document;
    }

    public void setDocument(long cedula) {
        this.document = cedula;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCelphone() {
        return celphone;
    }

    public void setCelphone(long celphone) {
        this.celphone = celphone;
    }

}
