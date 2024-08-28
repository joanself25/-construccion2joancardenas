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
public class InvoiceDetailDTO {

    private long id;
    private InvoiceDTO invoiceId;
    private int item;
    private String description;
    private double amount;

    public InvoiceDetailDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public InvoiceDTO getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(InvoiceDTO invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
