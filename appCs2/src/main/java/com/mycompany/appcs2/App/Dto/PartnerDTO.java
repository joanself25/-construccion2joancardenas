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
    private String vipRequestStatus; // Estado de solicitud vip
    private Date vipRequestDate;    // Fecha de solicitud vip
    private String promotionRequestStatus; // Solicitud de promoción Estado
    private Date promotionRequestDate; // Solicitud de promoción Fecha
    private String lowRequestStatus; // Estado de solicitud baja
    private Date lowRequestDate; // fecha de solicitud baja

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
        this.typeSuscription = type;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getVipRequestStatus() {
        return vipRequestStatus;
    }

    public void setVipRequestStatus(String vipRequestStatus) {
        this.vipRequestStatus = vipRequestStatus;
    }

    public Date getVipRequestDate() {
        return vipRequestDate;
    }

    public void setVipRequestDate(Date vipRequestDate) {
        this.vipRequestDate = vipRequestDate;
    }

    public String getPromotionRequestStatus() {
        return promotionRequestStatus;
    }

    public void setPromotionRequestStatus(String promotionRequestStatus) {
        this.promotionRequestStatus = promotionRequestStatus;
    }

    public Date getPromotionRequestDate() {
        return promotionRequestDate;
    }

    public void setPromotionRequestDate(Date promotionRequestDate) {
        this.promotionRequestDate = promotionRequestDate;
    }

    public String getLowRequestStatus() {
        return lowRequestStatus;
    }

    public void setLowRequestStatus(String lowRequestStatus) {
        this.lowRequestStatus = lowRequestStatus;
    }

    public Date getLowRequestDate() {
        return lowRequestDate;
    }

    public void setLowRequestDate(Date lowRequestDate) {
        this.lowRequestDate = lowRequestDate;
    }

}
