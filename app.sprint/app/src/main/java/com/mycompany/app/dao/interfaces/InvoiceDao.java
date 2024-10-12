/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.dao.interfaces;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.app.Dto.InvoiceDTO;
import java.util.List;

public interface InvoiceDao {

    public void createInvoice(InvoiceDTO invoiceDto) throws Exception;

    public List<InvoiceDTO> getInvoicesPartner(long partnerId) throws Exception;

    public void payInvoices(long partnerId) throws Exception;

    public List<InvoiceDTO> getInvoicesByGuestId(long guestId) throws Exception;

    public InvoiceDTO findInvoiceById(long invoiceId) throws Exception;

    public void updateInvoice(InvoiceDTO invoiceDto) throws Exception;

    public List<InvoiceDTO> getInvoicesByStatus(boolean status) throws Exception;

    public void deleteAllInvoicesByPartnerId(long id) throws Exception;

    public List<InvoiceDTO> UnpaidInvoicesPartner(long partnerId) throws Exception;

    public void deleteAllInvoicesByGuestId(long id) throws Exception;

    public List<InvoiceDTO> UnpaidInvoicesGuest(long id) throws Exception;

    public long createAllInvoices(InvoiceDTO invoiceDto) throws Exception;

}
