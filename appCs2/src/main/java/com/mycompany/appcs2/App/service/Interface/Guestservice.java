/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.service.Interface;

import com.mycompany.appcs2.App.Dto.GuestDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDTO;
import com.mycompany.appcs2.App.Dto.InvoiceDetailDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Guestservice {

    public GuestDTO findGuestByUserId(long userId) throws Exception;

    public void convertGuestToPartner() throws Exception;
    


   
}
