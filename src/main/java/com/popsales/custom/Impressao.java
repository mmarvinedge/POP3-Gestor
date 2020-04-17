/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.custom;

import com.popsales.Sessao;
import com.popsales.Utils;
import com.popsales.model.Item;
import com.popsales.model.Order;
import java.math.BigDecimal;
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

    public static final String LS = "------------------------------------------\n";
    public static final String LD = "==========================================\n";
    public static final String format = "%1$-10s %2$-24s\n";

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
        try {
            System.out.println("IMPRIMINDO PEDIDO");
            Collection<List<Item>> itensByPrinter = or.getProducts().stream().filter(c -> c.getPrinter() != null && !c.getPrinter().equalsIgnoreCase("nao imprimir")).collect(Collectors.groupingBy(f -> f.getPrinter())).values();
            for (List<Item> list : itensByPrinter) {
                StringBuilder sb = new StringBuilder();
                sb.append("-----------------" + list.get(0).getPrinter() + "---------\n\n");
                sb.append("PEDIDO : ").append(or.getNum_order()).append("\n");
                sb.append("CLIENTE : ").append(or.getClientInfo().getName()).append("\n");
                sb.append("TELEFONE: ").append(or.getClientInfo().getPhone()).append("\n");
                if (or.getDelivery()) {
                    sb.append("ENDERECO: ").append(or.getAddress().getStreet()).append("\n");
                    if (!or.getAddress().getSuburb().isEmpty()) {
                        sb.append(or.getAddress().getSuburb()).append("\n");
                    }
                    sb.append(or.getAddress().getAuto()).append(" - ").append(or.getAddress().getCity()).append("\n");;
                } else {
                    sb.append("RETIRADA EM BALCAO").append("\n\n");
                }
                sb.append(LS);
                sb.append("-----------------ITENS--------------------\n\n");
                list.forEach(pp -> {
                    sb.append(String.format(format, pp.getQuantity() + " x ", pp.getName().toUpperCase()));
                    if (pp.getObs().length() > 0) {
                        sb.append(pp.getObs()).append("\n");
                    }
                    sb.append(pp.getAttributesValues().stream().map(m -> "     Adicional: " + m.getName()).collect(Collectors.joining("\n")));
                });

                sb.append("\n\n\n\n\n\n\n\n" + "\n" + (char) 27 + (char) 109);
                System.out.println(sb.toString());
                printDestination(removeAcentos(sb.toString()), Sessao.ini.get("Printers", list.get(0).getPrinter(), String.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void imprimirOrderEntrega(Order or) {

        StringBuilder sb = new StringBuilder();
        sb.append("-----------------ENTREGADOR---------\n\n");
        sb.append("PEDIDO : ").append(or.getNum_order()).append("\n");
        sb.append("CLIENTE : ").append(or.getClientInfo().getName()).append("\n");
        sb.append("TELEFONE: ").append(or.getClientInfo().getPhone()).append("\n");
        sb.append(LS);

        sb.append("-----------------ITENS--------------------\n\n");
        for (Item i : or.getProducts()) {
            sb.append(String.format(format, (i.getQuantity() + " x " + Utils.formataParaMoeda(i.getPrice())), i.getName().toUpperCase()));
            if (i.getObs().length() > 0) {
                sb.append(i.getObs()).append("\n");
            }
            sb.append(i.getAttributesValues().stream().map(m -> "     Adicional: " + m.getName() + " - " + m.getPrice()).collect(Collectors.joining("\n")));
        }
        sb.append(LD);
        sb.append("PRODUTOS: ").append(Utils.formataParaMoeda(or.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add))).append("\n");
        sb.append("TAXA ENTREGA: ").append(Utils.formataParaMoeda(or.getDeliveryCost())).append("\n");
        sb.append("TOTAL: ").append(Utils.formataParaMoeda(or.getTotal())).append("\n");

        sb.append(LD);
        if (or.getDelivery()) {
            sb.append("ENDERECO: ").append(or.getAddress().getStreet()).append("\n");
            if (!or.getAddress().getSuburb().isEmpty()) {
                sb.append(or.getAddress().getSuburb()).append("\n");
            }
            sb.append(or.getAddress().getAuto()).append(" - ").append(or.getAddress().getCity()).append("\n");
        } else {
            sb.append("RETIRADA EM BALCAO").append("\n\n\n");
        }
        if (or.getForma().equalsIgnoreCase("Dinheiro")) {
            if (or.getTroco()) {
                sb.append("\tLEVAR TROCO PARA ").append(Utils.formatToMoney(new BigDecimal(or.getTrocoPara()))).append("\n\n");
            }
        } else {
            sb.append("FORMA DE PAGTO: " + or.getForma());
            sb.append(" \t LEVAR MARQUINA DE CARTAO!").append("\n\n");
        }

        sb.append("\n\n\n\n\n\n" + "\n" + (char) 27 + (char) 109);
        printDestination(removeAcentos(sb.toString()), Sessao.ini.get("Printers", or.getProducts().get(0).getPrinter(), String.class));

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
