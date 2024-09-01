/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.appcs2.App.dao.interfaces;

import com.mycompany.appcs2.App.Dto.GuestDTO;


/**
 *
 * @author CLAUDIA
 */
public interface GuestDao {

    public GuestDTO findGuestById(long guestId) throws Exception;

    public void updateGuest(GuestDTO guest);

    public GuestDTO findGuestByUserId(long id) throws Exception;

    public void deleteGuest(long guestId) throws Exception;

    public void createGuest(GuestDTO guestDTO)throws Exception ;



}
