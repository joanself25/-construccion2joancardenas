/*

 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.appcs2.app;

/**
 *
 * @author CLAUDIA
 */
import com.mycompany.appcs2.App.Controller.ControllerInterface;
import com.mycompany.appcs2.App.Controller.LoginController;
public class AppCs2 {

    public static void main(String[] args) throws Exception{
            ControllerInterface controller = new LoginController();
		try {
			controller.session();
			//MYSQLConnection.getConnection();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
               
    }
}

