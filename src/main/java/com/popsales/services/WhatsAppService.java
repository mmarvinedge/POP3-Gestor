/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.popsales.Constantes;
import com.popsales.components.WhatsappException;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Márvin Edge
 */
public class WhatsAppService {

    public void sendMessage(String num, String msg) throws WhatsappException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{\"number\" : \"" + num + "\", \"message\": \"" + msg + "\"}");
        

        //RequestBody body = RequestBody.create("{number : "+ num +", message: "+ msg +"}", MediaType.get("text/plain;chatset=UTF-8"));
        Request request = new Request.Builder()
                .url("http://localhost:3300"+ "/whats")
                .post(body)
                .build();
        Response response;
        try {
            response = Constantes.httpClient.newCall(request).execute();
            String resposta = response.body().string();
            System.out.println(resposta);
        } catch (IOException ex) {
            throw new WhatsappException("Não conectado com whatsapp!");
        }

    }

}
