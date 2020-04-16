/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
public class DialogRememberController implements Initializable {

    @FXML
    private Label lblTitle;

    private Stage dialogStage;
    public Region region;
    @FXML
    private JFXButton btnRecusar;
    @FXML
    private JFXButton btnAceitar;
    @FXML
    private JFXCheckBox ckLembrar;

    private Boolean lembrar;
    private String output;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        root.setOpacity(1);
        //FadeMessage.fadeIn(root);
        btnRecusar.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT) {
                if (!btnAceitar.isDefaultButton()) {
                    btnAceitar.requestFocus();
                    btnAceitar.setDefaultButton(true);
                    btnRecusar.setDefaultButton(false);
                }
            }
        });
        btnAceitar.setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                if (!btnRecusar.isDefaultButton()) {
                    btnRecusar.requestFocus();
                    btnRecusar.setDefaultButton(true);
                    btnAceitar.setDefaultButton(false);
                }
            }
        });
        Platform.runLater(() -> {
            btnAceitar.requestFocus();
            btnAceitar.setDefaultButton(true);
        });
        ckLembrar.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            lembrar = newValue;
        });;
    }

    private void sair(ActionEvent event) {
        dialogStage.close();
        if (region != null) {
            region.setVisible(false);
        }
    }

    public void setTitulo(String titulo) {
        lblTitle.setText(titulo);
    }

    public void setRemember(Boolean remember) {
        lembrar = remember;
    }

    public void setRecusa(String recusa) {
        btnRecusar.setText(recusa);
    }

    public void setAceita(String aceita) {
        btnAceitar.setText(aceita);
    }

    public void setStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @FXML
    private void recusar(ActionEvent event) {
        dialogStage.close();
        if (region != null) {
            region.setVisible(false);
        }
        lembrar = ckLembrar.isSelected();
        output = btnRecusar.getText();
    }

    @FXML
    private void aceitar(ActionEvent event) {
        dialogStage.close();
        if (region != null) {
            region.setVisible(false);
        }
        lembrar = ckLembrar.isSelected();
        output = btnAceitar.getText();
    }

    public String getOutput() {
        return output;
    }

    public Boolean getLembrar() {
        System.out.println("GETLEMBRAR " + lembrar);
        return lembrar;
    }

    @FXML
    private void onEnterRecusar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            dialogStage.close();
            if (region != null) {
                region.setVisible(false);
            }
            lembrar = ckLembrar.isSelected();
            output = btnRecusar.getText();
        }
    }

    @FXML
    private void onEnterAceitar(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            dialogStage.close();
            if (region != null) {
                region.setVisible(false);
            }
            lembrar = ckLembrar.isSelected();
            output = btnAceitar.getText();
        }
    }

}
