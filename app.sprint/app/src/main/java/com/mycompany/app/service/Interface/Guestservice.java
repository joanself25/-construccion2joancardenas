/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.GuestDTO;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.InvoiceDetailDTO;
import com.mycompany.app.Dto.UserDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Guestservice {

    public GuestDTO findGuestByUserId(long userId) throws Exception;

    public void convertGuestToPartner(long userId) throws Exception;

    public void createInvoiceGuest(InvoiceDTO invoiceDto, List<InvoiceDetailDTO> details) throws Exception;

    // Verificar si el invitado existe
}
