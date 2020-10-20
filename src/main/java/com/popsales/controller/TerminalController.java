package com.popsales.controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.popsales.Sessao;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;

/**
 * FXML Controller class
 *
 * @author Renato
 */
public class TerminalController implements Initializable {

    @FXML
    private CheckBox cbCozinha;
    @FXML
    private CheckBox cbEntregador;
    @FXML
    private CheckBox cbControle;
    @FXML
    private CheckBox cbViaEntregador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //LOAD
        try {
            cbCozinha.setSelected(Sessao.ini.get("Terminal", "ImprimirCozinha", Boolean.class));
        } catch (Exception e) {
            cbCozinha.setSelected(false);
        }
        try {
            cbEntregador.setSelected(Sessao.ini.get("Terminal", "ImprimirEntregador", Boolean.class));
        } catch (Exception e) {
            cbEntregador.setSelected(false);
        }
        try {
            cbControle.setSelected(Sessao.ini.get("Terminal", "ImprimirControle", Boolean.class));
        } catch (Exception e) {
            cbControle.setSelected(false);
        }
        try {
            cbViaEntregador.setSelected(Sessao.ini.get("Terminal", "ImprimirViaEntregador", Boolean.class));
        } catch (Exception e) {
            cbViaEntregador.setSelected(false);
        }

        //REGISTRANDO OS LISTENERS
        cbCozinha.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            try {
                Sessao.ini.put("Terminal", "ImprimirCozinha", cbCozinha.isSelected());
                Sessao.ini.store(new File("C:/popsales/config.ini"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cbEntregador.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            try {
                Sessao.ini.put("Terminal", "ImprimirEntregador", cbEntregador.isSelected());
                Sessao.ini.store(new File("C:/popsales/config.ini"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cbControle.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            try {
                Sessao.ini.put("Terminal", "ImprimirControle", cbControle.isSelected());
                Sessao.ini.store(new File("C:/popsales/config.ini"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        cbViaEntregador.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            try {
                Sessao.ini.put("Terminal", "ImprimirViaEntregador", cbViaEntregador.isSelected());
                Sessao.ini.store(new File("C:/popsales/config.ini"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

}
