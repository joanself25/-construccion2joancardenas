/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Dto;

import java.sql.Date;

/**
 *
 * @author Farley
 */
public class PartnerDTO {

    private long id;
    private UserDTO userId;
    private double fundsmoney;
    private String typeSuscription;
    private Date dateCreated;
   

    public PartnerDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserDTO getUserId() {
        return userId;
    }

    public void setUserId(UserDTO userId) {
        this.userId = userId;
    }

    public double getfundsMoney() {
        return fundsmoney;
    }

    public void setfundsMoney(double money) {
        this.fundsmoney = money;
    }

    public String getTypeSuscription() {
        return typeSuscription;
    }

    public void setTypeSuscription(String type) {
        this.typeSuscription = type;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

  


}
