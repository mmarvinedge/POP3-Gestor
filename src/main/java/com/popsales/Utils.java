/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author Renato
 */
public class Utils {

    public static String formataData(Date d, String pattern) {
        DateFormat df = new SimpleDateFormat(pattern, new Locale("pt", "BR"));
        if (d != null) {
            return df.format(d);
        } else {
            return "";
        }
    }

    public static BigDecimal formataByMoeda(String total) {
        return new BigDecimal(total.replace("R$", "").replace(".", "").replace(",", ".").trim());
    }

    public static String formataParaMoeda(String total) {
        return new java.text.DecimalFormat("¤ #,##0.00").format(total);
    }

    public static String formatToMoney(BigDecimal total) {
        return NumberFormat.getCurrencyInstance().format(total.doubleValue());
    }

    public static String formataParaMoeda(BigDecimal total) {
        return new java.text.DecimalFormat("¤ #,##0.00").format(total);
    }

    public static String formataParaMoeda(Double total) {
        return new java.text.DecimalFormat("¤ #,##0.00").format(total);
    }

    public static String formataParaMoeda(double total) {
        return new java.text.DecimalFormat("¤ #,##0.00").format(total);
    }

    public static String formataParaMoeda(float total) {
        return new java.text.DecimalFormat("¤ #,##0.00").format(total);
    }

    public static String formataParaMoeda(Integer total) {
        return new java.text.DecimalFormat("¤ #,##0.00").format(total);
    }
}
