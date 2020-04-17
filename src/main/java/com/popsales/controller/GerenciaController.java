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
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
    @FXML
    public Region region;
    @FXML
    private Label lblQntAguardando;
    @FXML
    private Label lblEmProducao;
    @FXML
    private Label lblAguardandoFinalizar;
    @FXML
    public Text lblNomeEmpresa;
    @FXML
    public Text lblUsuario;

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
        boxAguardandoAceite.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            lblQntAguardando.setText(boxAguardandoAceite.getChildren().size() + "");
        });
        boxAguardandoProducao.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            lblEmProducao.setText(boxAguardandoProducao.getChildren().size() + "");
        });
        boxAguardandoFinalizacao.getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            lblAguardandoFinalizar.setText(boxAguardandoFinalizacao.getChildren().size() + "");
        });
        lblNomeEmpresa.setText(Sessao.company.getName());
        lblUsuario.setText(Sessao.user.getName());
    }

    @FXML
    private void toConfig(ActionEvent event) {

        try {
            Stage stage = new Stage();
            AnchorPane cena = new FXMLLoader().load(getClass().getResource("/fxml/ConfiguracaoFXML.fxml"));
            Scene cen = new Scene(cena);
            stage.setScene(cen);
            stage.setResizable(false);
            region.setVisible(true);
            stage.showAndWait();
            region.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void toExit(ActionEvent event) {
        Sessao.t.cancel();
        System.exit(0);
    }

    @FXML
    private void atualizar(ActionEvent event) {
        view.loadComponents();
        view.loadData();
    }

}
