/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.dao.interfaces;

import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Dto.PersonDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import com.mycompany.appcs2.App.model.Partner;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface PartnerDao {

    // crear un partner que ya existe 
    public void createPartner(UserDTO userDTO) throws Exception;

    public void createPartne(PartnerDTO partnerDTO) throws Exception;

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

    public void deactivatePartner(long partnerId) throws Exception;

    public void activateGuest(long guestId) throws Exception;

    public void deactivateGuest(long guestId) throws Exception;

    public void requestVIPPromotion(long partnerId) throws Exception;

    public void uploadFunds(long partnerId, double amount) throws Exception;

    public void updatePartnerSubscription(long partnerId, String newType) throws Exception;

    public PartnerDTO mapResultSetToPartnerDTO(ResultSet rs) throws SQLException;

}
