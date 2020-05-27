/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.components;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.popsales.pojo.MotivoCancelamento;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renato
 */
public class DialogComboBoxController implements Initializable {

    private String output;
    private Stage dialogStage;
    private boolean closed;

    @FXML
    private Label lblTitle;
    @FXML
    private Button btnConfirmar;
    @FXML
    private AnchorPane root;
    @FXML
    private ComboBox<MotivoCancelamento> cbItens;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(cbItens::requestFocus);
        btnConfirmar.disableProperty().bind(cbItens.valueProperty().isNull());
//        root.setOpacity(0);
//        FadeMessage.fadeIn(root);
        lblTitle.setText("Selecione uma Opção");
    }

    @FXML
    private void confirmar(ActionEvent event) {
        MotivoCancelamento motivo = (MotivoCancelamento) cbItens.getValue();
        output = new Gson().toJson(motivo);
        dialogStage.close();
    }

    @FXML
    private void confirmEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            confirmar(new ActionEvent());
        } else if (event.getCode() == KeyCode.ESCAPE) {
            closeDialog();
        }
    }

    public void setItensCombo(List itens) {
        cbItens.getItems().addAll(itens);
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public Label getLblTitle() {
        return lblTitle;
    }

    public void setLblTitle(Label lblTitle) {
        this.lblTitle = lblTitle;
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void sair(MouseEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        output = "";
        setClosed(true);
        dialogStage.close();
    }

}
