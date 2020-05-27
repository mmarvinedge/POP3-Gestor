/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olivet.components;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author DELL
 */
public class DialogComboBoxController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label lblTitle;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    private JFXComboBox<?> cbItens;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void sair(ActionEvent event) {
    }

    @FXML
    private void confirmEnter(KeyEvent event) {
    }

    @FXML
    private void confirmar(ActionEvent event) {
    }
    
}
