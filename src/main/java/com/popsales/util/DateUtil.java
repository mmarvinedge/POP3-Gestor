/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.util;

import java.util.Date;

/**
 *
 * @author Marvin
 */
public class DateUtil {
    
    public static long diferencaEntreDatas(String tipo, Date dataInicio, Date dataFim) {

        Date dataDe = dataInicio;
        Date dataAte = dataFim;
        long diferencaSegundos = (dataAte.getTime() - dataDe.getTime()) / (1000);
        long diferencaMinutos = (dataAte.getTime() - dataDe.getTime()) / (1000 * 60);
        long diferencaHoras = (dataAte.getTime() - dataDe.getTime()) / (1000 * 60 * 60);
        long diferencaDias = (dataAte.getTime() - dataDe.getTime()) / (1000 * 60 * 60 * 24);
        long diferencaMeses = (dataAte.getTime() - dataDe.getTime()) / (1000 * 60 * 60 * 24) / 30;
        long diferencaAnos = ((dataAte.getTime() - dataDe.getTime()) / (1000 * 60 * 60 * 24) / 30) / 12;

        return diferencaDias;
    }
    
}
