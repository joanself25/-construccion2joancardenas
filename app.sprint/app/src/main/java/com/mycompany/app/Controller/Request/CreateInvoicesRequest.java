/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.app.Controller.Request;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author CLAUDIA
 */
@Getter
@Setter
@NoArgsConstructor
public class CreateInvoicesRequest {

    //public String item;
    // public String description;
    private String userId; 
    private String partnerId;
    private List<CreateInvoicesDetailsRequest> details;
}
