/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.components;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author renato
 */
public class ConfirmDialog extends AnchorPane {

    private Label msg;
    private Boolean saida = false;
    private Button yes = new Button("Sim");
    private Button cancel = new Button("Cancelar");

    public ConfirmDialog(String mensagem) {
        this.msg.setText(mensagem);
        this.setPrefWidth(500);
        this.setPrefHeight(200);
        this.setStyle("-fx-border-color: white; -fx-background-color: #78909C");
        this.getChildren().add(yes);
        this.getChildren().add(cancel);
        this.getChildren().add(msg);
    }

    public Boolean dialog() {
        Stage stage = new Stage();
        Scene cena = new Scene(this);
        stage.setScene(cena);
        stage.show();
        yes.setOnAction((ActionEvent event) -> {
            saida = true;
            stage.close();
        });
        cancel.setOnAction((ActionEvent event) -> {
            saida = false;
            stage.close();
        });

        return saida;
    }

}
