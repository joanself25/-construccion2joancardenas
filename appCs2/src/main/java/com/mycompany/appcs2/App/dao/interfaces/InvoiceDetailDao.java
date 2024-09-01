/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.dao.interfaces;

import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface InvoiceDetailDao {

    public void createInvoiceDetail(InvoiceDetailDTO detail) throws Exception;

    InvoiceDetailDTO findInvoiceDetailById(long id) throws Exception;

    List<InvoiceDetailDTO> findInvoiceDetailsByInvoiceId(long invoiceId) throws Exception;

    public void updateInvoiceDetail(InvoiceDetailDTO detail) throws Exception;

    public void deleteInvoiceDetail(long id) throws Exception;

}
