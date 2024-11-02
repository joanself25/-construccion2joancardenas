/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.PartnerDTO;
import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Dto.UserDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Adminservice {

    public void createPartner(UserDTO userDTO, PersonDTO personDTO) throws Exception;

    public List<PartnerDTO> getPendingVIPRequests() throws Exception;

    public double getTotalPaidInvoices(long id) throws Exception;

    List<InvoiceDTO> getPartnerInvoices(long partnerId) throws Exception;

    public List<InvoiceDTO> getGuestPendingInvoice(long guestId) throws Exception;

    public List<InvoiceDTO> getGuestPaidInvoices(long guestId) throws Exception;

    public List<InvoiceDTO> getGuestInvoices(long guestId) throws Exception;

    public void requestVIPSubscription(UserDTO userDto) throws Exception;

    public void createInvoice(InvoiceDTO invoiceDto) throws Exception;

    public void approveVIPRequest(long partnerId) throws Exception;

    public void rejectVIPRequest(long partnerId) throws Exception;

    public List<InvoiceDTO> PaidInvoices(long partnerId) throws Exception;

    public List<InvoiceDTO> getPendingInvoices(long partnerId) throws Exception;

    public double getTotalInvoicesAmount(long partnerId) throws Exception;

    public List<InvoiceDTO> getPaidInvoices(long partnerId) throws Exception;

}
