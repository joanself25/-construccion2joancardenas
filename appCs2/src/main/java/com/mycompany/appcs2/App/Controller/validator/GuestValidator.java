/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.appcs2.App.Controller.validator;

/**
 *
 * @author CLAUDIA
 */
public class GuestValidator extends CommonsValidator{
        public GuestValidator(){
            super();
            
        }
        public long validId(String id)throws Exception {
            
            return super.isValidlong("id del invitado ", id);
	}
	
	 
    
}
