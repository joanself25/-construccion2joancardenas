package com.mycompany.appcs2.App.model;

import java.sql.Date;

public class Partner {

    private long id;
    private User userId;
    private double fundsmoney;
    private String typeSuscription;
    private Date dateCreated;

    public double getFundsmoney() {
        return fundsmoney;
    }

    public void setFundsmoney(double fundsmoney) {
        this.fundsmoney = fundsmoney;
    }

    public String getTypeSuscription() {
        return typeSuscription;
    }

    public void setTypeSuscription(String typeSuscription) {
        this.typeSuscription = typeSuscription;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public double getFundsMoney() {
        return fundsmoney;
    }

    public void setFundsMoney(double money) {
        this.fundsmoney = money;
    }

}
