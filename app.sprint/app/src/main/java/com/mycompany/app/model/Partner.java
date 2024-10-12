package com.mycompany.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

import jakarta.persistence.ManyToOne;

import jakarta.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "partner")

public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // cambiar el nombre de las columnas  con la base de datos que el profe compartio 
    @Column(name = "id")
    private long id;
    @ManyToOne
    @JoinColumn(name = "userid")
    private User userId;
    @Column(name = "amount")
    private double fundsmoney;
    @Column(name = "type")
    private String typeSuscription;
    @Column(name = "creationdate")
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
