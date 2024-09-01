/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.service.Interface;

import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Adminservice {

    public void createPartner(UserDTO userDTO) throws Exception;

    public void createGuest(UserDTO userDTO, long partnerId) throws Exception;

    public List<PartnerDTO> getPendingVIPRequests() throws Exception;

    public void approveVIPRequest(long id) throws Exception;

    public void rejectVIPRequest(long id) throws Exception;

    public double getTotalPaidInvoices(long id) throws Exception;

    List<InvoiceDTO> getPartnerInvoices(long partnerId) throws Exception;

    public List<InvoiceDTO> getGuestInvoices(long guestId) throws Exception;

    public void createInvoice(InvoiceDTO invoiceDto) throws Exception;

    public void payInvoice(long invoiceId) throws Exception;

    public List<InvoiceDTO> PaidInvoices(long partnerId) throws Exception;

    public List<InvoiceDTO> getPendingInvoices(long partnerId) throws Exception;

    public List<InvoiceDTO> getPaidInvoices(long partnerId) throws Exception;

    public UserDTO createPartn(UserDTO userDTO) throws Exception;

    
    
}
