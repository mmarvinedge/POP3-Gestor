/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales;

import com.popsales.custom.Impressora;
import com.popsales.model.Company;
import com.popsales.model.User;
import java.util.List;
import java.util.Timer;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import org.ini4j.Ini;

/**
 *
 * @author Renato
 */
public class Sessao {

    public static User user;
    public static Company company;
    public static Timer t;
    public static List<Impressora> impressoras;
    public static List<String> impressorasProdutos;
    public static List<String> impressorasWindows;
    public static Ini ini;
    

}
