/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;


import com.popsales.Constantes;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Márvin Edge
 */
public class WhatsAppService {
    
    public void sendMessage(String num, String msg) throws IOException{
        Request request = new Request.Builder()
                .url(Constantes.WURL + "/" + num + "/" + msg)
                .get()
                .build();
        Response response = Constantes.httpClient.newCall(request).execute();
        System.out.println(response.body().string());
    }
    
}
