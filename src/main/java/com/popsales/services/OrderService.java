/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.popsales.Constantes;
import com.popsales.Sessao;
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
public class OrderService {

    public String pathOrders = "/order";
    
    Gson gson = new GsonBuilder()
   .setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public List<Order> getOrders(String status) {
        //System.out.println("URL:" + Constantes.URL + pathOrders + "/all/" + Sessao.company.getId() + "/" + status);
        List<Order> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + pathOrders + "/all/" + Sessao.company.getId() + "/" + status)
                .get()
                .build();
        try (Response response = Constantes.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Get response body
            String json = response.body().string();
            saida = gson.fromJson(json, new TypeToken<List<Order>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            saida = new ArrayList();
        }
        return saida;
    }

    public Order update(Order order) throws IOException {
        RequestBody body = RequestBody.create(gson.toJson(order), Constantes.JSON); // new
        Request request = new Request.Builder()
                .url(Constantes.URL + "/order/")
                .put(body)
                .build();
        Response response = Constantes.httpClient.newCall(request).execute();
        String json = response.body().string();
        System.out.println("RESPONSE: "+json);
        return gson.fromJson(json, Order.class);
    }
}
