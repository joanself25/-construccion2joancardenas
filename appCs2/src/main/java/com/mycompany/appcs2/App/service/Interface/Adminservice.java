/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.service.Interface;

import com.mycompany.appcs2.App.Dto.PartnerDTO;
import com.mycompany.appcs2.App.Dto.UserDTO;
import java.util.List;

/**
 *
 * @author CLAUDIA
 */
public interface Adminservice {

    public void createPartner(UserDTO userDto) throws Exception;

    public void createGuest(UserDTO userDto) throws Exception;

    public void approveVIPRequest(long id)throws Exception ;

    public void rejectVIPRequest(long id)throws Exception;

    public List<PartnerDTO> getPendingVIPRequests()throws Exception;

    public double getTotalPaidInvoices(long id)throws Exception;

  
    
}
