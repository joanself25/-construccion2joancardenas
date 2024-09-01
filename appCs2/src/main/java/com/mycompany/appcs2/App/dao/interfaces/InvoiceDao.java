/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.dao.interfaces;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import java.util.List;

public interface InvoiceDao {

    public void createInvoice(InvoiceDTO invoiceDto) throws Exception;

    public List<InvoiceDTO> getInvoicesByPartnerId(long partnerId) throws Exception;

    public List<InvoiceDTO> getInvoicesByGuestId(long guestId) throws Exception;

    public InvoiceDTO findInvoiceById(long invoiceId) throws Exception;

    public void updateInvoice(InvoiceDTO invoiceDto) throws Exception;

    public long createInvoicess(InvoiceDTO invoiceDto);

}
