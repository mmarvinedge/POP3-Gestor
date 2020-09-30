/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.ws;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.popsales.controller.LoginController;
import java.io.IOException;
import javax.websocket.ClientEndpoint;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 *
 * @author Marvin
 */
@ClientEndpoint
public class Client_API {
    
    @OnOpen
    public void onOpen(Session session) {
        try {
            LoginController.session = session;
            System.out.println("Conectei no Socket da API");
            session.getBasicRemote().sendText("Hello API");
        } catch (IOException ex) {
            ex.getCause();
        }
    }

    @OnClose
    public void onClose(Session session) {
        try {
            LoginController.session = session;
            System.out.println("Fechando conexão com ws da API");
            session.close();
        } catch (IOException ex) {
            ex.getCause();
        }
    }
}
