/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.appcs2.App.Dto;

/**
 *
 * @author Farley
 */
public class GuestDTO {

    private long id;
    private UserDTO userId;
    private PartnerDTO partnerId;
    private boolean status;

    public GuestDTO() {

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

    public PartnerDTO getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(PartnerDTO partnerId) {
        this.partnerId = partnerId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
