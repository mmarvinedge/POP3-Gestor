/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller;

import com.popsales.Sessao;
import com.popsales.components.Mensagem;
import com.popsales.controller.view.GerenciaView;
import com.popsales.util.Options;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
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
    public Process p = null;
    Thread t = null;

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
                        if (p != null && p.isAlive()) {
                            p.destroy();
                        }
                        if (t != null && t.isAlive()) {
                            t.resume();
                        }
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
        //finalizaNodeJs();
       // runWhatsApp();
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

    @FXML
    private void executarWhatsapp(ActionEvent event) {
        runWhatsApp();
    }

    private void runWhatsApp() {
        try {
            Runtime.getRuntime().exec("taskkill /F /IM node.exe");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (t != null && t.isAlive()) {
            t.resume();
        }
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"C:\\popsales\\bin\" && npm run start");
                    builder.redirectErrorStream(true);
                    p = builder.start();
                    BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
                    String line;
                    while (true) {
                        line = r.readLine();
                        if (line == null) {
                            break;
                        }
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        Mensagem.dialogException(e, region, stage);
                    });
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    @FXML
    private void abrirLink(ActionEvent event) {
        String url = "";
        if (Sessao.company.getNameUrl() == null || Sessao.company.getNameUrl().isEmpty()) {
            url = "http://food.popsales.com.br/popsales/" + Sessao.company.getId();
        } else {
            url = "http://food.popsales.com.br/popsales/rest/" + Sessao.company.getNameUrl();
        }

        try {
            URI uri = new URI(url);

            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finalizaNodeJs() {
        try {
            String serviceName = "node.exe";
            if (Options.isProcessRunning(serviceName)) {
                Options.killProcess(serviceName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
