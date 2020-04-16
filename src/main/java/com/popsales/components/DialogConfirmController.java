/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.components;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
public class DialogConfirmController implements Initializable {

    private Boolean check = false;
    private Stage stage;
    public Region region;

    @FXML
    private Label lblTitle;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXButton btnCancelar;
    @FXML
    private Label lblMessagem;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(btnConfirmar::requestFocus);
        root.setOpacity(1);
    }

    @FXML
    private void sair(ActionEvent event) {
        if (region != null) {
            region.setVisible(false);
        }
        check = false;
        stage.close();
    }

    @FXML
    private void confirmar(ActionEvent event) {
        check = true;
        if (region != null) {
            region.setVisible(false);
        }
        stage.close();
    }

    @FXML
    private void cancelar(ActionEvent event) {
        sair(event);
    }

    @FXML
    private void onEnterConfirm(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            confirmar(new ActionEvent());
        } else if (event.getCode() == KeyCode.ESCAPE) {
            sair(new ActionEvent());
        }
    }

    @FXML
    private void onEnterExit(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            sair(new ActionEvent());
        } else if (event.getCode() == KeyCode.ESCAPE) {
            sair(new ActionEvent());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMensagem(String mensagem) {
        lblMessagem.setText(mensagem);
    }

    public void setTitulo(String titulo) {
        lblTitle.setText(titulo);
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Boolean getCheck() {
        return check;
    }

}
