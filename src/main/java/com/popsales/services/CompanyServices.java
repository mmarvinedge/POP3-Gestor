/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.popsales.Constantes;
import com.popsales.model.Company;
import java.io.IOException;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * @author Renato
 */
public class CompanyServices {

    public String pathCompanyId = "/company/";

    public Company loadCompany(String idCompany) {

        Company comp = new Company();
        Request request = new Request.Builder()
                .url(Constantes.URL + pathCompanyId + idCompany)
                .get()
                .build();
        try (Response response = Constantes.httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String json = response.body().string();
            System.out.println("Company: " + json);

            comp = new Gson().fromJson(json, Company.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return comp;
    }
}
