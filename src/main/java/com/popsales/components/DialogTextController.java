/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renato
 */
public class DialogTextController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private JFXButton btnConfirmar;
    private String titulo;

    private String output;
    private Stage dialogStage;
    public Region region;
    @FXML
    private JFXTextArea textPut;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(textPut::requestFocus);
        btnConfirmar.disableProperty().bind(Bindings.isEmpty(textPut.textProperty()));
        root.setOpacity(1);
    }

    @FXML
    private void sair(ActionEvent event) {
        output = "";
        dialogStage.close();
        region.setVisible(false);
    }

    @FXML
    private void confirmar(ActionEvent event) {
        output = textPut.getText();
        dialogStage.close();
        region.setVisible(false);
    }

    public void setTitulo(String titulo) {
        lblTitle.setText(titulo);
    }

    public void setAssunto(String assunto) {
        textPut.setPromptText(assunto);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public String getOutput() {
        return output;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    private void confirmEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            confirmar(new ActionEvent());
        } else if (event.getCode() == KeyCode.ESCAPE) {
            sair(new ActionEvent());
        }
    }

    @FXML
    private void abrirTeclado(ActionEvent event) {
       
    }

}
