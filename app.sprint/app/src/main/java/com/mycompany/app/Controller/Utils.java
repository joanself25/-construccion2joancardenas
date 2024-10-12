/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app.Controller;

import java.util.Scanner;

/**
 *
 * @author Farley
 */
public abstract class Utils {

    private static Scanner reader = new Scanner(System.in);

    public static Scanner getReader() {
        return reader;
    }
}
