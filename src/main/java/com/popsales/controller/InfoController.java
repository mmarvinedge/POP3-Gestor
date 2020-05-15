/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller;

import com.popsales.Utils;
import com.popsales.components.Mensagem;
import com.popsales.components.WhatsappException;
import com.popsales.controller.view.GerenciaView;
import com.popsales.model.Attribute;
import com.popsales.model.AttributeValue;
import com.popsales.model.FlavorPizza;
import com.popsales.model.Item;
import com.popsales.model.Order;
import com.popsales.services.WhatsAppService;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author Renato
 */
public class InfoController implements Initializable {

    public static Order order;
    @FXML
    private Label lblNome;
    @FXML
    private Label lblTelefone;
    @FXML
    private Label lblData;
    @FXML
    private Label lblEndereço;
    @FXML
    private HBox boxItem;
    @FXML
    private Label lblProdutos;
    @FXML
    private Label lblTaxas;
    @FXML
    private Label lblTotal;
    @FXML
    private VBox vBoxTable;
    @FXML
    private Label lblObs;
    private WhatsAppService wppService = new WhatsAppService();
    public static GerenciaController gerenciaController;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vBoxTable.getChildren().clear();
        lblNome.setText(order.getClientInfo().getName());
        lblTelefone.setText(order.getClientInfo().getPhone());
        lblObs.setText(order.getObservations() == null ? "Sem obs" : order.getObservations());

        lblProdutos.setText(Utils.formatToMoney(order.getProducts().stream().map(m -> m.getTotal()).reduce(BigDecimal.ZERO, BigDecimal::add)));
        lblTaxas.setText(Utils.formatToMoney(order.getDeliveryCost()));
        lblTotal.setText(Utils.formatToMoney(order.getTotal()));
        lblData.setText(Utils.formataData(order.getDtRegistro(), "dd/MM/yyyy HH:mm:sss"));
        if (order.getDelivery()) {
            lblEndereço.setWrapText(true);
            lblEndereço.setText(order.getAddress().getStreet() + " - " + (order.getAddress().getSuburb().isEmpty() ? "S/C" : order.getAddress().getSuburb()) + "\n"
                    + order.getAddress().getAuto() + (order.getAddress().getCity() == null ? "" : " - " + order.getAddress().getCity()));
        } else {
            lblEndereço.setText("Retirada em Balcão");
        }

        for (Item p : order.getProducts()) {
            HBox principal = new HBox();
            principal.setStyle("-fx-border-width:  0 0 1 0; -fx-border-color:  #ccc");

            HBox dois = new HBox();
            principal.setHgrow(dois, Priority.ALWAYS);

            VBox box = new VBox();
            HBox boxUm = new HBox();
            Label desc = new Label(p.getName());
            desc.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #545454; -fx-fill: #545454");
            Label obs = new Label();
            obs.setStyle("-fx-font-size: 12px;  -fx-text-fill: #545454; -fx-fill: #545454");
            if (!p.getObs().isEmpty()) {
                obs.setText("(" + p.getObs() + ")\n");
            }
            if (p.getAttributes().size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Attribute at : p.getAttributes()) {
                    sb.append("\t" + at.getDescription() + "\n");
                    for (AttributeValue v : at.getValues()) {
                        sb.append("\t\t" + v.getQuantity() + " x " + Utils.formataParaMoeda(v.getPrice()) + " " + v.getName()).append("\n");
                    }
                }
                sb.append(obs.getText());
                obs.setText(sb.toString());
            }
            if (p.getFlavors() != null && p.getFlavors().size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (FlavorPizza av : p.getFlavors()) {
                    sb.append(" (1/" + p.getFlavors().size() + " x ").append(av.getFlavor()).append(")\n");
                }
                sb.append(obs.getText());
                obs.setText(sb.toString());
            }
            boxUm.setAlignment(Pos.CENTER_LEFT);
            boxUm.getChildren().add(desc);
            boxUm.getChildren().add(obs);
            box.getChildren().add(boxUm);
            Label priceQnt = new Label(p.getQuantity() + " x " + Utils.formataParaMoeda(p.getPrice()));
            priceQnt.setStyle("-fx-font-size: 15px;  -fx-text-fill: #545454; -fx-fill: #545454");
            box.getChildren().add(priceQnt);

            dois.getChildren().add(box);
            HBox tres = new HBox();
            Label total = new Label(Utils.formataParaMoeda(p.getTotal()));
            total.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #cd4c51; -fx-fill: #cd4c51");
            tres.getChildren().add(total);

            principal.getChildren().add(dois);
            principal.getChildren().add(tres);
            vBoxTable.getChildren().add(principal);
        }
    }

    @FXML
    private void sair(ActionEvent event) {
        ((Stage) vBoxTable.getScene().getWindow()).close();
    }

    @FXML
    private void enviarMensagem(ActionEvent event) {
        try {
            String phone = "";
            // System.out.println(order.getClientInfo().getPhone().length());
            if (order.getClientInfo().getPhone().length() == 14) {
                phone = order.getClientInfo().getPhone().replace("(", "").replace(")9", "").replace("-", "");
                phone = "55" + phone;
            } else {
                phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
                phone = "55" + phone;
            }
            System.out.println(phone);
            String msg = Mensagem.dialogText("Digita a mensagem", "Digita a mensagem", null);
            StringBuilder sb = new StringBuilder();
            String[] lines = msg.split("\n");
            sb.append(Utils.removeAcentos(Arrays.asList(lines).stream().collect(Collectors.joining(" "))));
            System.out.println(sb.toString());

            if (sb.toString() != null && !sb.toString().isEmpty()) {
                if (gerenciaController.p == null) {
                    Notifications.create().title("Atençao!").text("Whatsapp não está sendo executado!").showWarning();
                } else {
                    wppService.sendMessage(phone, sb.toString());
                    Notifications.create().title("Atençao!").text("Mensagem enviada!").showInformation();
                }

            }
        } catch (WhatsappException e) {
            Notifications.create().title("Atenção").text("Whatsapp não rodando!").showWarning();
        }

    }

}
