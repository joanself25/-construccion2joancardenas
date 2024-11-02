/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.service.Interface;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.app.Dto.GuestDTO;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.InvoiceDetailDTO;
import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Dto.UserDTO;
import java.util.List;

public interface Partnerservice {

    public void lowPartner(long partnerId) throws Exception;

    public PartnerDTO findPartnerByUserId(long userId) throws Exception;

    public PartnerDTO findPartnerById(long partnerId) throws Exception;

    public void createInvoicePartner(InvoiceDTO invoiceDto, List<InvoiceDetailDTO> details) throws Exception;

    public void uploadFunds(long userId, double amount) throws Exception;

    public void requestPromotion(long partnerId) throws Exception;

    public void desactivateGuest(long guestId, long partnerId) throws Exception;

    public void activateGuest(long guestId, long partnerId) throws Exception;

    public List<InvoiceDTO> getPartnerInvoices(long partnerId) throws Exception;

    public void payInvoices(long userId) throws Exception;

    public void createGuest(GuestDTO guestDto) throws Exception;

}
