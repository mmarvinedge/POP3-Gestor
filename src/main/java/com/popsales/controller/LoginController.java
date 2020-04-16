/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.popsales.Sessao;
import com.popsales.components.Mensagem;
import com.popsales.model.User;
import com.popsales.services.CompanyServices;
import com.popsales.services.LoginService;
import com.popsales.services.ProductService;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Renato
 */
public class LoginController implements Initializable {

    private LoginService loginService = new LoginService();
    private CompanyServices companyServices = new CompanyServices();
    private ProductService productServices = new ProductService();

    @FXML
    private JFXButton btnLogin;
    @FXML
    private JFXTextField iptUser;
    @FXML
    private JFXPasswordField iptSenha;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void entrar(ActionEvent event) {
        if (iptUser.getText().isEmpty()) {
            return;
        }
        if (iptSenha.getText().isEmpty()) {
            return;
        }
        try {
            Sessao.user = loginService.Login(new User(iptUser.getText(), iptSenha.getText()));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            Mensagem.dialogAlert(ex.getMessage(), null, iptUser.getScene().getWindow());
            return;
        } catch (Exception e) {
            Mensagem.dialogAlert(e.getMessage(), null, iptUser.getScene().getWindow());
            System.out.println(e.getMessage());
            return;
        }

        if (Sessao.user.getId() != null) {
            Sessao.company = companyServices.loadCompany(Sessao.user.getCompanyId());
            List<String> printers = productServices.getPrinters();
            printers.forEach(c -> {
                System.out.println("PRINTER: " + c);
            });
            abreJanela();
        } else {
            System.out.println("USUARIO NAO ENCONTRADO!");
        }
    }

    private void abreJanela() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/GerenciaFXML.fxml"));
            Scene scene = new Scene(root);
            Stage stage = new Stage(StageStyle.DECORATED);

            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());

            stage.setTitle("POP Sales - Nome da Empresa");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();
            ((Stage) btnLogin.getScene().getWindow()).close();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
