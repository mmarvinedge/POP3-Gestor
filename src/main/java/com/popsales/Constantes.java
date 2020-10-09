/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales;

import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

/**
 *
 * @author Renato
 */
public class Constantes {

    //  public static final String URL = "http://localhost:4000";
    public static final String URL = "http://popsales.ddns.net:4000";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(4, TimeUnit.SECONDS)
            .writeTimeout(4, TimeUnit.SECONDS)
            .readTimeout(4, TimeUnit.SECONDS).build();

    public static final String WURL = "http://localhost:3300/whats";

    public static Integer versao = 21;

    public static String green = "#2e8a39";
    public static String lemon = "#96c63d";
}
