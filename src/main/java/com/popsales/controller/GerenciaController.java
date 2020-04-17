/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller;

import com.popsales.Sessao;
import com.popsales.controller.view.GerenciaView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
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
                        System.out.println("EVENTO: " + event);
                        Sessao.t.cancel();
                    }
                });

            }
        });

    }

    @FXML
    private void toConfig(ActionEvent event) {

        try {
            Stage stage = new Stage();
            AnchorPane cena = new FXMLLoader().load(getClass().getResource("/fxml/ConfiguracaoFXML.fxml"));
            Scene cen = new Scene(cena);
            stage.setScene(cen);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toExit(ActionEvent event) {
        Sessao.t.cancel();
        System.exit(0);
    }

}
