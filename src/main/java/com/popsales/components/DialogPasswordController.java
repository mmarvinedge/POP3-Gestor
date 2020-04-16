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
public class DialogPasswordController implements Initializable {

    @FXML
    private Label lblTitle;
    @FXML
    private JFXButton btnConfirmar;
    @FXML
    public PasswordField textField;
    private String titulo;

    private String output;
    private Stage dialogStage;
    public Region region;
    @FXML
    private AnchorPane root;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(textField::requestFocus);
        btnConfirmar.disableProperty().bind(Bindings.isEmpty(textField.textProperty()));
        root.setOpacity(1);
    }

    @FXML
    private void sair(ActionEvent event) {
        output = "";
        dialogStage.close();
        if (region != null) {
            region.setVisible(false);
        }
    }

    @FXML
    private void confirmar(ActionEvent event) {
        output = textField.getText();
        dialogStage.close();
        if (region != null) {
            region.setVisible(false);
        }
    }

    @FXML
    private void maisMenos(ActionEvent event) {
    }

    @FXML
    private void dois(ActionEvent event) {
        processaTecladoVirtual("2");
    }

    @FXML
    private void tres(ActionEvent event) {
        processaTecladoVirtual("3");
    }

    @FXML
    private void quatro(ActionEvent event) {
        processaTecladoVirtual("4");
    }

    @FXML
    private void um(ActionEvent event) {
        processaTecladoVirtual("1");
    }

    @FXML
    private void cinco(ActionEvent event) {
        processaTecladoVirtual("5");
    }

    @FXML
    private void seis(ActionEvent event) {
        processaTecladoVirtual("7");
    }

    @FXML
    private void sete(ActionEvent event) {
        processaTecladoVirtual("7");
    }

    @FXML
    private void ponto(ActionEvent event) {
        processaTecladoVirtual(".");
    }

    @FXML
    private void zero(ActionEvent event) {
        processaTecladoVirtual("0");
    }

    @FXML
    private void oito(ActionEvent event) {
        processaTecladoVirtual("8");
    }

    @FXML
    private void nove(ActionEvent event) {
        processaTecladoVirtual("9");
    }

    @FXML
    private void limpar(ActionEvent event) {
        textField.clear();
    }

    public void setTitulo(String titulo) {
        lblTitle.setText(titulo);
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

    private void processaTecladoVirtual(String conteudo) {

        textField.setText(textField.getText() + conteudo);
        textField.positionCaret(textField.getText().length() + 1);
        textField.selectAll();

    }

    @FXML
    private void confirmEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            confirmar(new ActionEvent());
        } else if (event.getCode() == KeyCode.ESCAPE) {
            sair(new ActionEvent());
        }
    }

}
