/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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

    public static String removeAcentos(String str) {
        CharSequence cs = new StringBuilder(str == null ? "" : str);
        return Normalizer
                .normalize(cs, Normalizer.Form.NFKD)
                .replace("&", "e")
                .replace("ç", "c")
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public static Date getDataByTexto(String data, String formato) {
        try {
            Date date = null;
            DateFormat formatter = new SimpleDateFormat(formato);
            date = (java.util.Date) formatter.parse(data);
            return date;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static void extractZipFiles(String filename, String destinationname) {
        try {
            // destination folder to extract the contents         

            byte[] buf = new byte[1024];
            ZipInputStream zipinputstream = null;
            ZipEntry zipentry;
            zipinputstream = new ZipInputStream(new FileInputStream(new File(filename)));
            zipentry = zipinputstream.getNextEntry();

            while (zipentry != null) {

                // for each entry to be extracted
                String entryName = zipentry.getName();

                int n;
                FileOutputStream fileoutputstream;
                File newFile = new File(entryName);

                String directory = newFile.getParent();

                // to creating the parent directories
                if (directory == null) {
                    if (newFile.isDirectory()) {
                        break;
                    }
                } else {
                    System.out.println("DEST: " + destinationname + directory);
                    new File(destinationname + directory).mkdirs();
                }
                
                if (!zipentry.isDirectory()) {
                    System.out.println("File to be extracted....." + entryName);
                    fileoutputstream = new FileOutputStream(destinationname + entryName);
                    while ((n = zipinputstream.read(buf, 0, 1024)) > -1) {
                        fileoutputstream.write(buf, 0, n);
                    }
                    fileoutputstream.close();
                }

                zipinputstream.closeEntry();
                zipentry = zipinputstream.getNextEntry();
            }// while

            zipinputstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
