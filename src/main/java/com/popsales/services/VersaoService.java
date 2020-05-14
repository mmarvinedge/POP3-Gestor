/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.popsales.Constantes;
import com.popsales.custom.Versao;
import com.popsales.model.Order;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Renato
 */
public class VersaoService {

    public Integer getVersao() {
        //System.out.println("URL:" + Constantes.URL + pathOrders + "/all/" + Sessao.company.getId() + "/" + status);
        Request request = new Request.Builder()
                .url("http://metre.ddns.net:88/versao.php")
                .get()
                .build();
        try (Response response = Constantes.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Get response body
            String json = response.body().string();
            Versao v = new Gson().fromJson(json, Versao.class);
            return v.getVersao();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order update(Order order) throws IOException {
        RequestBody body = RequestBody.create(new Gson().toJson(order), Constantes.JSON); // new
        Request request = new Request.Builder()
                .url(Constantes.URL + "/order/")
                .put(body)
                .build();
        Response response = Constantes.httpClient.newCall(request).execute();
        String json = response.body().string();
        return new Gson().fromJson(json, Order.class);
    }
}
