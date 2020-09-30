/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author Marvin
 */
public class FileUtil {

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
