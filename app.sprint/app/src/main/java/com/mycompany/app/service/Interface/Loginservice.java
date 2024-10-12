/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.service.Interface;

import com.mycompany.app.Dto.UserDTO;

/**
 *
 * @author CLAUDIA
 */
public interface Loginservice {

     public void login(UserDTO userDto) throws Exception;

    public void logout();

}
