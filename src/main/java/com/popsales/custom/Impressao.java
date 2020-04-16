/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.custom;

import com.popsales.Sessao;
import com.popsales.model.Item;
import com.popsales.model.Order;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 *
 * @author Renato
 */
public class Impressao {

    private static PrintService findPrintService(String printerName) {
        PrintService output = null;
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().equalsIgnoreCase(printerName)) {
                output = printService;
                break;
            }
        }
        return output;
    }

    private static PrintService DefaultPrintService() {
        return PrintServiceLookup.lookupDefaultPrintService();
    }

    public static void printDestination(String conteudo, String printer) {
        try {
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            PrintService defaultService = findPrintService(printer);
            if (defaultService == null) {
                System.err.println("O Sistema não localizou a impressora " + printer + " (Texto) no Windows.");
                return;
            }
            byte[] bytes;
            bytes = conteudo.getBytes();
            Doc doc = new SimpleDoc(bytes, flavor, null);
            DocPrintJob job = defaultService.createPrintJob();
            job.print(doc, null);
            job = null;
            flavor = null;
            pras = null;
            defaultService = null;
        } catch (Exception e) {
            System.err.println("Erro ao imprimir " + e.getMessage());
            e.printStackTrace();
        }
    }
}
