/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.popsales.Constantes;
import com.popsales.components.WhatsappException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okio.ByteString;

/**
 *
 * @author Márvin Edge
 */
public class WhatsAppService {

    public void sendMessage(String num, String msg) throws WhatsappException {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), "{\"number\" : \""+ num +"\", \"message\": \""+ msg +"\"}");
        Request request = new Request.Builder()
                .url(Constantes.WURL)
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
