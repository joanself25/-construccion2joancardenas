/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appcs2.App.Dto;

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
    

    public double getFundsmoney() {
        return fundsmoney;
    }

    public void setFundsmoney(double fundsmoney) {
        this.fundsmoney = fundsmoney;
    }

    

    
    

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
        this.typeSuscription= type;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setVipRequestStatus(String rechazada) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setVipRequestDate(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public String getVipRequestDate() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   
   
}
