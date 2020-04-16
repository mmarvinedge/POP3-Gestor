/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.popsales.Constantes;
import com.popsales.Sessao;
import com.popsales.model.Order;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author Renato
 */
public class ProductService {

    public String pathOrders = "/product";

    public List<String> getPrinters() {

        List<String> saida = new ArrayList();
        Request request = new Request.Builder()
                .url(Constantes.URL + pathOrders + "/printer")
                .header("company_id", Sessao.company.getId())
                .get()
                .build();
        try (Response response = Constantes.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            // Get response body
            String json = response.body().string();

            saida = new Gson().fromJson(json, new TypeToken<List<String>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            saida = new ArrayList();
        }
        return saida;
    }
}
