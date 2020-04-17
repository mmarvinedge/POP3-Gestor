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
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Márvin Edge
 */
public class WhatsAppService {

    public void sendMessage(String num, String msg) throws WhatsappException {
        Request request = new Request.Builder()
                .url(Constantes.WURL + "/" + num + "/" + msg)
                .get()
                .build();
        Response response;
        try {
            response = Constantes.httpClient.newCall(request).execute();
            String resposta = response.body().string();
        } catch (IOException ex) {
            throw new WhatsappException("Não conectado com whatsapp!");
        }

    }

}
