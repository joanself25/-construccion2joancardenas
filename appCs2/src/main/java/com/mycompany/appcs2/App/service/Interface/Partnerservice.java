/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.service.Interface;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Dto.GuestDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import java.util.List;

public interface Partnerservice {

    public void managementFunds(PartnerDTO partnerDto, double Amount) throws Exception;

    public void requestVIPSubscription(UserDTO userDto) throws Exception;

    public void guestPartner(GuestDTO guestdto) throws Exception;

    public void lowPartner(GuestDTO guestdto) throws Exception;

    public void createInvoice(InvoiceDTO invoicedto) throws Exception;

  

}
