/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.custom;

import com.popsales.Sessao;
import com.popsales.Utils;
import com.popsales.model.Attribute;
import com.popsales.model.AttributeValue;
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
    public static final String formatQntity = "%1$-3s %2$-24s\n";
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
        Boolean naoImprimir = false;
        try {
            naoImprimir = Sessao.ini.get("Terminal", "ImprimirCozinha", Boolean.class);
        } catch (Exception e) {
            e.printStackTrace();
            naoImprimir = false;
        }
        if (naoImprimir != null && naoImprimir) {
            return;
        }

        try {
            //System.out.println("IMPRIMINDO PEDIDO");
            Collection<List<Item>> itensByPrinter = or.getProducts().stream().filter(c -> c.getPrinter() != null && !c.getPrinter().equalsIgnoreCase("nao imprimir")).collect(Collectors.groupingBy(f -> f.getPrinter())).values();
            for (List<Item> list : itensByPrinter) {
                StringBuilder sb = new StringBuilder();
                sb.append("-----------------" + list.get(0).getPrinter() + "-----------------\n\n");
                sb.append("PEDIDO : ").append(or.getNum_order()).append("\n");
                sb.append("CLIENTE : ").append(or.getClientInfo().getName()).append("\n");
                sb.append("TELEFONE: ").append(or.getClientInfo().getPhone()).append("\n");
                sb.append("DATA HORA: ").append(or.getDtRegister()).append("\n");
                if (or.getDelivery()) {
                    sb.append("ENDERECO: ").append(or.getAddress().getStreet() + " - " + or.getAddress().getStreetNumber()).append("\n");
                    if (!or.getAddress().getSuburb().isEmpty()) {
                        sb.append(or.getAddress().getSuburb()).append("\n");
                    }
                    sb.append(or.getAddress().getAuto()).append(" - ").append(or.getAddress().getCity()).append("\n");;
                } else {
                    sb.append("RETIRADA EM BALCAO").append("\n\n");
                }
                sb.append(LS);
                sb.append("------------------ITENS------------------\n\n");
                list.forEach(pp -> {
                    sb.append(String.format(formatQntity, pp.getQuantity() + " x ", pp.getName().toUpperCase()));
                    if (pp.getFlavors() != null && pp.getFlavors().size() > 0) {
                        sb.append(pp.getFlavors().stream().map(m -> "   1/" + pp.getFlavors().size() + " " + m.getFlavor()).collect(Collectors.joining("\n"))).append("\n");
                    }

                    if (pp.getAttributes() != null) {
                        for (Attribute at : pp.getAttributes()) {
                            sb.append("   ").append(at.getDescription());
                            for (AttributeValue va : at.getValues()) {
                                sb.append("\n      ").append(va.getQuantity()).append(" x ").append(va.getName());
                            }
                            sb.append("\n");
                        }
                    }
                    if (pp.getObs().length() > 0) {
                        sb.append("\t").append(pp.getObs()).append("\n");

                    } 
                    sb.append("\n");
                });

                sb.append("\n\n\n\n\n\n" + "\n" + (char) 27 + (char) 109);
                // System.out.println(sb.toString());
                try {
                    if (!Sessao.ini.get("Printers", list.get(0).getPrinter(), String.class).equalsIgnoreCase("NAO IMPRIMIR")) {
                        printDestination(removeAcentos(sb.toString()), Sessao.ini.get("Printers", list.get(0).getPrinter(), String.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERRO AO IMPRIMIR");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void imprimirOrderControle(Order or) {

        Boolean naoImprimir = false;
        try {
            naoImprimir = Sessao.ini.get("Terminal", "ImprimirControle", Boolean.class);
        } catch (Exception e) {
            naoImprimir = false;
        }
        if (naoImprimir != null && naoImprimir) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("-----------------CONTROLE-----------------\n\n");
        sb.append("PEDIDO : ").append(or.getNum_order()).append("\n");
        sb.append("CLIENTE : ").append(or.getClientInfo().getName()).append("\n");
        sb.append("TELEFONE: ").append(or.getClientInfo().getPhone()).append("\n");
        sb.append("HORA: ").append(Utils.formataData(Utils.getDataByTexto(or.getDtRegister(), "yyyy-MM-dd HH:mm:ss"), "dd/MM/yyyy HH:mm:ss")).append("\n");
        if (or.getCoupon() != null && or.getCoupon() != "") {
            sb.append("CUPOM: ").append(or.getCoupon().toUpperCase()).append("\n");
        }
        sb.append(LS);

        sb.append("------------------ITENS------------------\n\n");
        or.getProducts().forEach(pp -> {
            sb.append(String.format(formatQntity, pp.getQuantity() + " x ", pp.getName().toUpperCase()));
            if (pp.getFlavors() != null && pp.getFlavors().size() > 0) {
                sb.append(pp.getFlavors().stream().map(m -> "   1/" + pp.getFlavors().size() + " " + m.getFlavor()).collect(Collectors.joining("\n"))).append("\n");
            }

            if (pp.getAttributes() != null) {
                for (Attribute at : pp.getAttributes()) {
                    sb.append("   ").append(at.getDescription());
                    for (AttributeValue va : at.getValues()) {
                        sb.append("\n      ").append(va.getQuantity()).append(" x ").append(va.getName());
                    }
                    sb.append("\n");
                }
            }
            if (pp.getObs().length() > 0) {
                sb.append("\t").append(pp.getObs()).append("\n");

            }
            sb.append("\n");
        });
        sb.append(LD);
        sb.append("PRODUTOS: ").append(Utils.formataParaMoeda(or.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add))).append("\n");
        sb.append("TAXA ENTREGA: ").append(Utils.formataParaMoeda(or.getDeliveryCost())).append("\n");
        if (or.getCoupon() != null && or.getCoupon() != "") {
            sb.append("DESCONTO: ").append(Utils.formataParaMoeda(or.getDiscountValue())).append("\n");
        }
        sb.append("TOTAL: ").append(Utils.formataParaMoeda(or.getTotal())).append("\n");

        sb.append(LD);
        if (or.getDelivery()) {
            sb.append("ENDERECO: ").append(or.getAddress().getStreet() + " - " + or.getAddress().getStreetNumber()).append("\n");
            if (!or.getAddress().getSuburb().isEmpty()) {
                sb.append(or.getAddress().getSuburb()).append("\n");
            }
            sb.append(or.getAddress().getAuto()).append(" - ").append(or.getAddress().getCity()).append("\n");
        } else {
            sb.append("RETIRADA EM BALCAO").append("\n\n\n");
        }
        if (or.getForma().equalsIgnoreCase("Dinheiro")) {
            sb.append("FORMA DE PAGTO: " + or.getForma());
            if (or.getTroco()) {
                sb.append("\n\n\tCLIENTE VAI PAGAR: ").append(Utils.formatToMoney(new BigDecimal(or.getTrocoPara()))).append("\n");
                sb.append("\tTROCO: ").append(Utils.formatToMoney(new BigDecimal(or.getTrocoPara()).subtract(or.getTotal()))).append("\n");
            }
        } else {
            sb.append("FORMA DE PAGTO: " + or.getForma());
            sb.append("\n\t LEVAR MARQUINA DE CARTAO!").append("\n\n");
        }

        sb.append("\n\n\n\n\n\n" + "\n" + (char) 27 + (char) 109);
        try {
            if (!Sessao.ini.get("Printers", "Expedicao", String.class).equalsIgnoreCase("NAO IMPRIMIR")) {
                printDestination(removeAcentos(sb.toString()), Sessao.ini.get("Printers", "Expedicao", String.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO AO IMPRIMIR");
        }

    }

    public static void imprimirOrderEntrega(Order or) {

        Boolean naoImprimir = false;
        try {
            naoImprimir = Sessao.ini.get("Terminal", "ImprimirEntregador", Boolean.class);
        } catch (Exception e) {
            naoImprimir = false;
        }
        if (naoImprimir != null && naoImprimir) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("---------------ENTREGADOR----------------\n\n");
        sb.append("PEDIDO : ").append(or.getNum_order()).append("\n");
        sb.append("CLIENTE : ").append(or.getClientInfo().getName()).append("\n");
        sb.append("TELEFONE: ").append(or.getClientInfo().getPhone()).append("\n");
        sb.append("DATA HORA: ").append(or.getDtRegister()).append("\n");
        sb.append(LS);

        sb.append("------------------ITENS------------------\n\n");
        or.getProducts().forEach(pp -> {
            sb.append(String.format(formatQntity, pp.getQuantity() + " x ", pp.getName().toUpperCase()));
            if (pp.getFlavors() != null && pp.getFlavors().size() > 0) {
                sb.append(pp.getFlavors().stream().map(m -> "   1/" + pp.getFlavors().size() + " " + m.getFlavor()).collect(Collectors.joining("\n"))).append("\n");
            }

            if (pp.getAttributes() != null) {
                for (Attribute at : pp.getAttributes()) {
                    sb.append("   ").append(at.getDescription());
                    for (AttributeValue va : at.getValues()) {
                        sb.append("\n      ").append(va.getQuantity()).append(" x ").append(va.getName());
                    }
                    sb.append("\n");
                }
            }
            if (pp.getObs().length() > 0) {
                sb.append("\t").append(pp.getObs()).append("\n");

            }
        });
        sb.append("\n").append(LD);
        sb.append("PRODUTOS: ").append(Utils.formataParaMoeda(or.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add))).append("\n");
        sb.append("TAXA ENTREGA: ").append(Utils.formataParaMoeda(or.getDeliveryCost())).append("\n");
        if (or.getCoupon() != null && or.getCoupon() != "") {
            sb.append("DESCONTO: ").append(Utils.formataParaMoeda(or.getDiscountValue()));
        }
        sb.append("TOTAL: ").append(Utils.formataParaMoeda(or.getTotal())).append("\n");

        sb.append(LD);
        if (or.getDelivery()) {
            sb.append("ENDERECO: ").append(or.getAddress().getStreet() + " - " + or.getAddress().getStreetNumber()).append("\n");
            if (!or.getAddress().getSuburb().isEmpty()) {
                sb.append(or.getAddress().getSuburb()).append("\n");
            }
            sb.append(or.getAddress().getAuto()).append(" - ").append(or.getAddress().getCity()).append("\n");
        } else {
            sb.append("RETIRADA EM BALCAO").append("\n\n\n");
        }
        if (or.getForma().equalsIgnoreCase("Dinheiro")) {
            sb.append("FORMA DE PAGTO: " + or.getForma());
            if (or.getTroco()) {
                sb.append("\n\tLEVAR TROCO PARA ").append(Utils.formatToMoney(new BigDecimal(or.getTrocoPara()))).append("\n\n");
            }
        } else {
            sb.append("FORMA DE PAGTO: " + or.getForma());
            sb.append("\n\t LEVAR MARQUINA DE CARTAO!").append("\n\n");
        }

        sb.append("\n\n\n\n\n\n" + "\n" + (char) 27 + (char) 109);
        try {
            if (!Sessao.ini.get("Printers", "Expedicao", String.class).equalsIgnoreCase("NAO IMPRIMIR")) {
                printDestination(removeAcentos(sb.toString()), Sessao.ini.get("Printers", "Expedicao", String.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERRO AO IMPRIMIR");
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
