/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.services;

import com.google.gson.Gson;
import com.popsales.Constantes;
import com.popsales.model.User;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 *
 * @author Renato
 */
public class LoginService {

    public String pathLogin = "/user/login";

    public User Login(User user) throws IOException  {
        User u = new User();
        RequestBody body = RequestBody.create(new Gson().toJson(user), Constantes.JSON); // new
        Request request = new Request.Builder()
                .url(Constantes.URL + pathLogin)
                .post(body)
                .build();
        Response response = Constantes.httpClient.newCall(request).execute();
        System.out.println("RETORNO 1: " + response.message());
        System.out.println("RETORNO 2: " + response.body());
        System.out.println("RETORNO 3: " + response.body().toString());
        if (response.code() == 401) {
            throw new IOException("Usuário ou senha inválida!");
        }
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        u = new Gson().fromJson(response.body().string(), User.class);
        return u;

    }
}
