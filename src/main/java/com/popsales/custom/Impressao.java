/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.custom;

import com.popsales.Sessao;
import com.popsales.model.Item;
import com.popsales.model.Order;
import java.text.Normalizer;
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

    public static final String LS = "------------------------------------------";
    public static final String LD = "==========================================";
    public static final String format = "%1$-6s %2$-25s\n";

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

    public static void imprimirOrder(Order or) {

        Collection<List<Item>> itensByPrinter = or.getProducts().stream().filter(c -> c.getPrinter() != null && !c.getPrinter().equalsIgnoreCase("nao imprimir")).collect(Collectors.groupingBy(f -> f.getPrinter())).values();
        for (List<Item> list : itensByPrinter) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n\n\n\n\")");
            sb.append("-----------------COZINHA-----------------\n\n");
            sb.append("CLIENTE : ").append(or.getClientInfo().getName()).append("\n");
            sb.append("TELEFONE: ").append(or.getClientInfo().getPhone()).append("\n\n");
            sb.append(LS);
            sb.append("-----------------ITENS-----------------\n\n");
            list.forEach(pp -> {
                sb.append(String.format(format, pp.getQuantity(), pp.getName()));
                if (pp.getObs().length() > 0) {
                    sb.append(pp.getObs()).append("\n");
                }
            });
            printDestination(removeAcentos(sb.toString()), Sessao.ini.get("Printers", list.get(0).getPrinter(), String.class));
        }

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

    public static String removeAcentos(String str) {
        CharSequence cs = new StringBuilder(str == null ? "" : str);
        return Normalizer
                .normalize(cs, Normalizer.Form.NFKD)
                .replace("&", "e")
                .replace("ç", "c")
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
