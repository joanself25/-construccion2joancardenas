package com.mycompany.appcs2.App.Dto;

import java.util.Date;

/**
 *
 * @author Farley
 */
public class InvoiceDTO {

    private long id;
    private PersonDTO personId;
    private PartnerDTO partnerId;
    private Date creationDate;
    private double amount;
    private boolean status;
    private String Statuses;
    private GuestDTO guestid;

    public GuestDTO getGuestid() {
        return guestid;
    }

    public void setGuestid(GuestDTO guestid) {
        this.guestid = guestid;
    }

    public String getStatuses() {
        return Statuses;
    }

    public void setStatuses(String Statuses) {
        this.Statuses = Statuses;
    }

    public InvoiceDTO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PersonDTO getPersonId() {
        return personId;
    }

    public void setPersonId(PersonDTO personId) {
        this.personId = personId;
    }

    public PartnerDTO getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(PartnerDTO partnerId) {
        this.partnerId = partnerId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
