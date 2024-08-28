/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.service.Interface;

import com.mycompany.appcs2.App.Dto.UserDTO;

/**
 *
 * @author CLAUDIA
 */
public interface Loginservice {

    public void login(UserDTO userDto) throws Exception;

    public void logout();

}
