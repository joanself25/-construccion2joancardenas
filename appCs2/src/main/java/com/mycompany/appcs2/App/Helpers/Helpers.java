/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.Helpers;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.model.InvoiceDetail;
import com.mycompany.appcs2.App.model.Person;
import com.mycompany.appcs2.App.model.User;
import com.mycompany.appcs2.App.Dto.*;
import com.mycompany.appcs2.App.model.*;
import java.util.Date;

public abstract class Helpers {

    public static PersonDTO parse(Person person) {
        PersonDTO personDto = new PersonDTO();
        personDto.setId(person.getId());
        personDto.setCedula(person.getCedula());
        personDto.setName(person.getName());
        personDto.setCelphone(person.getCelphone());
        return personDto;
    }

    public static Person parse(PersonDTO personDto) {
        Person person = new Person();
        person.setId(personDto.getId());
        person.setCedula(personDto.getCedula());
        person.setName(personDto.getName());
        person.setCelphone(personDto.getCelphone());
        return person;
    }

    public static UserDTO parse(User user) {
        UserDTO userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setPassword(user.getPassword());
        userDto.setPersonId(parse(user.getPersonId()));
        userDto.setRol(user.getRol());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    public static User parse(UserDTO userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setPassword(userDto.getPassword());
        user.setpersondId(parse(userDto.getPersonId()));
        user.setRol(userDto.getRol());
        user.setUsername(userDto.getUsername());
        return user;

    }

    public static InvoiceDTO parse(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        InvoiceDTO invoiceDto = new InvoiceDTO();
        invoiceDto.setId(invoice.getId());
        invoiceDto.setPersonId(parse(invoice.getPersonId()));
        invoiceDto.setPartnerId(parse(invoice.getPartnerId()));
        invoiceDto.setCreationDate(new Date(invoice.getCreationDate().getTime()));
        invoiceDto.setAmount(invoice.getAmount());
        invoiceDto.setStatus(invoice.isStatus());
        return invoiceDto;
    }

    public static Invoice parse(InvoiceDTO invoiceDto) {
        if (invoiceDto == null) {
            return null;
        }
        Invoice invoice = new Invoice();
        invoice.setId(invoiceDto.getId());
        invoice.setPersonId(parse(invoiceDto.getPersonId()));
        invoice.setPartnerId(parse(invoiceDto.getPartnerId()));
        invoice.setCreationDate(new java.sql.Date(invoiceDto.getCreationDate().getTime()));
        invoice.setAmount(invoiceDto.getAmount());
        invoice.setStatus(invoiceDto.isStatus());
        return invoice;
    }

    // InvoiceDetail and InvoiceDetailDTO
    public static InvoiceDetailDTO parse(InvoiceDetail invoiceDetail) {
        if (invoiceDetail == null) {
            return null;
        }
        InvoiceDetailDTO detailDto = new InvoiceDetailDTO();
        detailDto.setId(invoiceDetail.getId());
        detailDto.setInvoiceId(parse(invoiceDetail.getInvoiceId()));
        detailDto.setItem(invoiceDetail.getItem());
        detailDto.setDescription(invoiceDetail.getDescription());
        detailDto.setAmount(invoiceDetail.getAmount());
        return detailDto;
    }

    public static InvoiceDetail parse(InvoiceDetailDTO detailDto) {
        if (detailDto == null) {
            return null;
        }
        InvoiceDetail detail = new InvoiceDetail();
        detail.setId(detailDto.getId());
        detail.setInvoiceId(parse(detailDto.getInvoiceId()));
        detail.setItem(detailDto.getItem());
        detail.setDescription(detailDto.getDescription());
        detail.setAmount(detailDto.getAmount());
        return detail;
    }

    // Partner and PartnerDTO
    public static PartnerDTO parse(Partner partner) {
        if (partner == null) {
            return null;
        }
        PartnerDTO partnerDto = new PartnerDTO();
        partnerDto.setId(partner.getId());

        return partnerDto;
    }

    public static Partner parse(PartnerDTO partnerDto) {
        if (partnerDto == null) {
            return null;
        }
        Partner partner = new Partner();
        partner.setId(partnerDto.getId());

        return partner;
    }
}
