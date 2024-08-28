/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.dao.interfaces;

import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;

import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface PartnerDao {

    public void createPartner(PartnerDTO partnerDto) throws Exception;

    public void updatePartnerFunds(PartnerDTO partnerDto) throws Exception;

    public void updatePartner(PartnerDTO partner) throws Exception;

    public List<InvoiceDTO> getPendingInvoices(long id) throws Exception;

    public void payInvoice(long id) throws Exception;

    public List<InvoiceDTO> getPaidInvoices(long partnerId) throws Exception;

    public PartnerDTO findPartnerById(long partnerId) throws Exception;

    public boolean isVIPSlotAvailable() throws Exception;
    //   obtener Solicitudes VIP Pendientes
    public List<PartnerDTO> getPendingVIPRequests() throws Exception;

    public PartnerDTO findPartnerByUserId(long id) throws Exception;

}
