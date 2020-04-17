/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.popsales.Sessao;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.ini4j.Ini;

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

        for (String ip : Sessao.impressorasProdutos) {
            JFXComboBox<String> printCb = new JFXComboBox<>();
            printCb.setLabelFloat(true);
            printCb.setPromptText(ip);
            printCb.setItems(FXCollections.observableList(Sessao.impressorasWindows));
            if (Sessao.ini != null && Sessao.ini.get("Printers", ip, String.class) != null) {
                printCb.setValue(Sessao.ini.get("Printers", ip, String.class));
            }
            printers.getChildren().add(printCb);

        }

        JFXButton button = new JFXButton("Salvar");
        button.setStyle("-fx-backgroun-color: #a03e42");
        button.setOnAction((ActionEvent event1) -> {
            try {
                printers.getChildren().forEach(c -> {
                    if (c instanceof JFXComboBox) {
                        JFXComboBox com = (JFXComboBox) c;
                        Sessao.ini.put("Printers", com.getPromptText(), com.getValue().toString());
                    }
                });
                Sessao.ini.store(new File("C:/popsales/config.ini"));
            } catch (IOException ex) {
                Logger.getLogger(ConfiguracaoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        printers.getChildren().add(button);
        conteudo.getChildren().add(printers);
    }

    @FXML
    private void sair(ActionEvent event) {
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
