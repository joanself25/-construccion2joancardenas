/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.InvoiceDetailDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Invoiceservice {

    public void createInvoiceDetails(InvoiceDTO invoiceDto, List<InvoiceDetailDTO> details) throws Exception;


}
