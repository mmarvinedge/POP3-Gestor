/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller;

import com.popsales.Sessao;
import com.popsales.controller.view.GerenciaView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Renato
 */
public class GerenciaController implements Initializable {

    GerenciaController esta = null;
    public GerenciaView view;
    public Stage stage;

    @FXML
    public VBox boxAguardandoAceite;
    @FXML
    public VBox boxAguardandoProducao;
    @FXML
    public VBox boxAguardandoFinalizacao;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        esta = this;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                view = new GerenciaView(esta);
                boxAguardandoAceite.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        Sessao.t.cancel();
                    }
                });

            }
        });

    }

    @FXML
    private void toConfig(ActionEvent event) {
    }

    @FXML
    private void toExit(ActionEvent event) {
        ((Stage) this.boxAguardandoProducao.getScene().getWindow()).close();
    }

}
