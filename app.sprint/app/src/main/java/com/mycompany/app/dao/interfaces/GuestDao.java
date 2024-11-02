/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.app.dao.interfaces;

import com.mycompany.app.Dto.GuestDTO;
import com.mycompany.app.Dto.InvoiceDTO;
import com.mycompany.app.Dto.PersonDTO;
import com.mycompany.app.Dto.UserDTO;
import java.util.List;

public interface GuestDao {

    List<InvoiceDTO> getGuestPendingInvoices(long guestId) throws Exception;

    List<InvoiceDTO> getGuestPaidInvoices(long guestId) throws Exception;

    GuestDTO findGuestByUserId(long userId) throws Exception;

    public GuestDTO findGuestById(long guestid) throws Exception;

    public void updateGuest(GuestDTO guest) throws Exception;

    public void deleteGuest(long guestId) throws Exception;

    public void createGuest(GuestDTO guestDTO) throws Exception;

    public List<GuestDTO> findGuestsByPartnerId(long id) throws Exception;

}
