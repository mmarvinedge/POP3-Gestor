/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.popsales.Sessao;
import com.popsales.components.Mensagem;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renato
 */
public class ConfiguracaoController implements Initializable {

    @FXML
    private AnchorPane conteudo;
    private String action;
    @FXML
    private JFXButton btnPrinter;
    @FXML
    private VBox vboxButtons;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void impressoras(ActionEvent event) {
        action = "printer";
        changeAction();
        conteudo.getChildren().clear();
        VBox printers = new VBox();
        printers.setSpacing(35);
        HBox pane = new HBox();
        pane.setStyle("-fx-background-color:  #cd4c51");
        Label label = new Label("Impressoras");

        label.setStyle("-fx-font-size: 18px; -fx-text-fill: white");;
        pane.getChildren().add(label);
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.setPadding(new Insets(30));
        printers.getChildren().add(pane);

        pane.setHgrow(printers, Priority.ALWAYS);
        for (String ip : Sessao.impressorasProdutos.stream().filter(p -> !p.equalsIgnoreCase("NAO IMPRIMIR")).collect(Collectors.toList())) {
            JFXComboBox<String> printCb = new JFXComboBox<>();
            printCb.setFocusColor(Paint.valueOf("cd4c51"));
            printCb.setLabelFloat(true);
            printCb.setPromptText(ip);
            printCb.setItems(FXCollections.observableList(Sessao.impressorasWindows));
            if (Sessao.ini != null && Sessao.ini.get("Printers", ip, String.class) != null) {
                printCb.setValue(Sessao.ini.get("Printers", ip, String.class));
            }
            printCb.setPadding(new Insets(0, 0, 0, 25));
            printers.getChildren().add(printCb);

        }

        JFXButton button = new JFXButton("Salvar");
        button.setStyle("-fx-background-color: #a03e42; -fx-text-fill: white;");
        button.setOnAction((ActionEvent event1) -> {
            try {
                printers.getChildren().forEach(c -> {
                    if (c instanceof JFXComboBox) {
                        JFXComboBox com = (JFXComboBox) c;
                        Sessao.ini.put("Printers", com.getPromptText(), com.getValue().toString());
                    }
                });
                Sessao.ini.store(new File("C:/popsales/config.ini"));
                
                Mensagem.dialogInformacao("Impressoras salvas com sucesso!", null, null);
            } catch (IOException ex) {
                Logger.getLogger(ConfiguracaoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        HBox bo = new HBox();
        bo.setHgrow(printers, Priority.ALWAYS);
        bo.getChildren().add(button);
        bo.setMargin(printers, new Insets(15, 35, 35, 25));
        printers.getChildren().add(bo);

        conteudo.setTopAnchor(printers, 0.0);
        conteudo.setLeftAnchor(printers, 0.0);
        conteudo.setRightAnchor(printers, 0.0);
        conteudo.setBottomAnchor(printers, 0.0);

        conteudo.getChildren().add(printers);
    }

    @FXML
    private void sair(ActionEvent event) {
        ((Stage) vboxButtons.getScene().getWindow()).close();
    }

    public void changeAction() {

        for (Node n : vboxButtons.getChildren()) {
            if (n instanceof JFXButton) {
                JFXButton b = (JFXButton) n;
                b.setStyle("-fx-background-color:  #cd4c51");
            }
        }
        if (action.equals("printer")) {
            JFXButton bn = (JFXButton) vboxButtons.getChildren().stream().filter(p -> p instanceof JFXButton).collect(Collectors.toList()).get(0);
            bn.setStyle("-fx-background-color: #a03e42;");
        }
    }

}
