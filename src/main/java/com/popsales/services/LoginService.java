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
import java.net.ProtocolException;
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

    public User Login(User user) throws IOException {
        try {
            User u = new User();
            RequestBody body = RequestBody.create(new Gson().toJson(user), Constantes.JSON); // new
            Request request = new Request.Builder()
                    .url(Constantes.URL + pathLogin)
                    .post(body)
                    .build();
            Response response = Constantes.httpClient.newCall(request).execute();
            if (response.code() == 401) {
                throw new IOException("Usuário ou senha inválida!");
            }
            
            // Company com licença expirada
            if (response.code() == 408) {
                User uu = new User();
                uu.setName("expired");
                return uu;
            }
            
            // Company bloqueada pelo financeiro
            if(response.code() == 409) {
                User uu = new User();
                uu.setName("block");
                return uu;
            }
            if (response.code() == 500) {
                return u;
            }
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            u = new Gson().fromJson(response.body().string(), User.class);
            return u;
        } catch (ProtocolException pe) {
            System.out.println("entrei no catch");
            User uu = new User();
            uu.setName("trialexpired");
            return uu;
        }

    }
}
