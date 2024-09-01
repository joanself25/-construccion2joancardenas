/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.appcs2.App.service.Interface;

import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Invoiceservice {

    public void createInvoiceWithDetails(InvoiceDTO invoiceDto, List<InvoiceDetailDTO> details) throws Exception;

}
