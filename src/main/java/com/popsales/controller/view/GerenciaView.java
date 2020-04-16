/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller.view;

import com.google.gson.Gson;
import com.jfoenix.controls.JFXButton;
import com.popsales.Sessao;
import com.popsales.Utils;
import com.popsales.controller.GerenciaController;
import com.popsales.custom.Impressao;
import com.popsales.model.Order;
import com.popsales.services.OrderService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import org.controlsfx.control.Notifications;

/**
 *
 * @author Renato
 */
public class GerenciaView {

    public GerenciaController view;
    public static List<Order> ordersAguardando = new ArrayList();
    public static List<Order> ordersProducao = new ArrayList();
    public static List<Order> ordersAguardandoFinalizar = new ArrayList();
    public int registrosAntes = 0;

    private OrderService orderService = new OrderService();

    public GerenciaView(GerenciaController view) {
        this.view = view;
        loadComponents();
        Sessao.t = new Timer();
        Sessao.t.schedule(
                new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    loadComponents();
                    loadData();
                });

            }
        }, 0, 60000);

    }

    public void loadData() {
        carregarAguardando();
        carregarProduzindo();
        carregarFinalizando();
    }

    private void carregarAguardando() {
        ordersAguardando = orderService.getOrders("Aguardando");
        if (registrosAntes == 0) {
            registrosAntes = ordersAguardando.size();
        } else if (registrosAntes != ordersAguardando.size()) {
            Notifications.create().title("Informação").text("Novo Pedido!").showInformation();
            registrosAntes = ordersAguardando.size();
        }
        for (Order or : ordersAguardando) {
            this.view.boxAguardandoAceite.getChildren().add(createCardOrderAguardando(or));
        }
    }

    private void carregarProduzindo() {
        ordersProducao = orderService.getOrders("Produzindo");
        for (Order or : ordersProducao) {
            this.view.boxAguardandoProducao.getChildren().add(createCardOrderProduzindo(or));
        }
    }

    private void carregarFinalizando() {
        ordersAguardandoFinalizar = orderService.getOrders("Finalizando");
        for (Order or : ordersAguardandoFinalizar) {
            this.view.boxAguardandoFinalizacao.getChildren().add(createCardOrderFinalizando(or));
        }
    }

    public void loadComponents() {
        this.view.boxAguardandoAceite.getChildren().clear();
        this.view.boxAguardandoProducao.getChildren().clear();
        this.view.boxAguardandoFinalizacao.getChildren().clear();
    }

    public VBox createCardOrderAguardando(Order order) {
        VBox saida = new VBox();
        saida.setId(order.getId());
        saida.setPadding(new Insets(5));
        saida.setMinHeight(150);
        HBox um = new HBox();
        um.setAlignment(Pos.TOP_RIGHT);
        um.getChildren().add(createLabel("08:00 Minutos", 12, "#111", Boolean.TRUE));

        HBox dois = new HBox();

        VBox doisUm = new VBox();
        doisUm.getChildren().add(createLabel(Utils.formatToMoney(new BigDecimal(order.getTotal())).toString(), 18, "#545454", Boolean.TRUE));

        HBox hboxNome = new HBox();
        hboxNome.setSpacing(5.0);
        hboxNome.getChildren().add(creatIcon(FontAwesomeIcon.USER, "25", "#838383"));
        hboxNome.getChildren().add(createLabel(order.getClientInfo().getName(), 16, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxNome);

        HBox hboxTelefone = new HBox();
        hboxTelefone.setSpacing(5.0);
        hboxTelefone.getChildren().add(creatIcon(FontAwesomeIcon.PHONE, "25", "#838383"));
        hboxTelefone.getChildren().add(createLabel(order.getClientInfo().getPhone(), 16, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxTelefone);

        HBox doisdois = new HBox();
        doisdois.setSpacing(5.0);
        doisdois.setPrefWidth(160.0);
        doisdois.setMinWidth(160.0);
        doisdois.setMaxWidth(160.0);
        doisdois.setAlignment(Pos.CENTER_RIGHT);
        JFXButton bt1 = createButton(FontAwesomeIcon.CHECK, "#00bdaa");
        bt1.setOnAction((ActionEvent event) -> {
            try {
                order.setStatus("Produzindo");
                Node n = this.view.boxAguardandoAceite.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoAceite.getChildren().remove(n);
                this.view.boxAguardandoProducao.getChildren().add(createCardOrderProduzindo(order));
                orderService.update(order);
                Impressao.imprimirOrder(order);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        doisdois.getChildren().add(bt1);

        JFXButton bt2 = createButton(FontAwesomeIcon.CLOSE, "#cd4c51");
        bt2.setOnAction((ActionEvent event) -> {
            try {
                order.setStatus("Cancelado");
                Node n = this.view.boxAguardandoAceite.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoAceite.getChildren().remove(n);
                orderService.update(order);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        doisdois.getChildren().add(bt2);

        JFXButton bt3 = createButton(FontAwesomeIcon.INFO, "#400082");
        bt3.setOnAction((ActionEvent event) -> {

        });
        doisdois.getChildren().add(bt3);
        doisUm.getChildren().add(doisdois);
        dois.setHgrow(doisUm, Priority.ALWAYS);
        dois.getChildren().add(doisUm);

        dois.getChildren().add(doisdois);

        HBox tres = new HBox();
        tres.setSpacing(5.0);
        if (order.getDelivery()) {
            tres.getChildren().add(creatIcon(FontAwesomeIcon.MAP_MARKER, "25", "#838383"));
            tres.getChildren().add(createLabel(order.getAddress().getStreet() + "\n"
                    + order.getAddress().getSuburb() + " - " + order.getAddress().getAuto(), 13, "#545454", Boolean.FALSE));
        } else {
            tres.getChildren().add(creatIcon(FontAwesomeIcon.TABLE, "25", "#838383"));
            tres.getChildren().add(createLabel("Retirada Local", 13, "#545454", Boolean.FALSE));
        }
        saida.getChildren().add(um);
        saida.getChildren().add(dois);
        saida.getChildren().add(tres);
        return saida;
    }

    public VBox createCardOrderProduzindo(Order order) {
        VBox saida = new VBox();
        saida.setId(order.getId());
        saida.setPadding(new Insets(5));
        saida.setMinHeight(150);
        HBox um = new HBox();
        um.setAlignment(Pos.TOP_RIGHT);
        um.getChildren().add(createLabel("08:00 Minutos", 12, "#111", Boolean.TRUE));

        HBox dois = new HBox();

        VBox doisUm = new VBox();
        doisUm.getChildren().add(createLabel(Utils.formatToMoney(new BigDecimal(order.getTotal())).toString(), 18, "#545454", Boolean.TRUE));

        HBox hboxNome = new HBox();
        hboxNome.setSpacing(5.0);
        hboxNome.getChildren().add(creatIcon(FontAwesomeIcon.USER, "25", "#838383"));
        hboxNome.getChildren().add(createLabel(order.getClientInfo().getName(), 16, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxNome);

        HBox hboxTelefone = new HBox();
        hboxTelefone.setSpacing(5.0);
        hboxTelefone.getChildren().add(creatIcon(FontAwesomeIcon.PHONE, "25", "#838383"));
        hboxTelefone.getChildren().add(createLabel(order.getClientInfo().getPhone(), 16, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxTelefone);

        HBox doisdois = new HBox();
        doisdois.setSpacing(5.0);
        doisdois.setPrefWidth(160.0);
        doisdois.setMinWidth(160.0);
        doisdois.setMaxWidth(160.0);
        doisdois.setAlignment(Pos.CENTER_RIGHT);
        JFXButton button1 = createButton(FontAwesomeIcon.CHECK, "#00bdaa");
        button1.setOnAction((ActionEvent event) -> {
            try {
                System.out.println("CLICK");
                order.setStatus("Finalizando");
                Node n = this.view.boxAguardandoProducao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoProducao.getChildren().remove(n);
                this.view.boxAguardandoFinalizacao.getChildren().add(createCardOrderFinalizando(order));
                orderService.update(order);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        doisdois.getChildren().add(button1);
        JFXButton button2 = createButton(FontAwesomeIcon.CLOSE, "#cd4c51");
        button2.setOnAction((ActionEvent event) -> {
            try {
                order.setStatus("Aguardando");
                Node n = this.view.boxAguardandoProducao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoProducao.getChildren().remove(n);
                this.view.boxAguardandoAceite.getChildren().add(createCardOrderAguardando(order));
                orderService.update(order);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        doisdois.getChildren().add(button2);

        JFXButton button3 = createButton(FontAwesomeIcon.INFO, "#400082");
        button3.setOnAction((ActionEvent event) -> {

        });
        doisdois.getChildren().add(button3);
        doisUm.getChildren().add(doisdois);
        dois.setHgrow(doisUm, Priority.ALWAYS);
        dois.getChildren().add(doisUm);

        dois.getChildren().add(doisdois);

        HBox tres = new HBox();
        tres.setSpacing(5.0);
        if (order.getDelivery()) {
            tres.getChildren().add(creatIcon(FontAwesomeIcon.MAP_MARKER, "25", "#838383"));
            tres.getChildren().add(createLabel(order.getAddress().getStreet() + "\n"
                    + order.getAddress().getSuburb() + " - " + order.getAddress().getAuto(), 13, "#545454", Boolean.FALSE));
        } else {
            tres.getChildren().add(creatIcon(FontAwesomeIcon.TABLE, "25", "#838383"));
            tres.getChildren().add(createLabel("Retirada Local", 13, "#545454", Boolean.FALSE));
        }
        saida.getChildren().add(um);
        saida.getChildren().add(dois);
        saida.getChildren().add(tres);
        return saida;
    }

    public VBox createCardOrderFinalizando(Order order) {
        VBox saida = new VBox();
        saida.setId(order.getId());
        saida.setPadding(new Insets(5));
        saida.setMinHeight(150);
        HBox um = new HBox();
        um.setAlignment(Pos.TOP_RIGHT);
        um.getChildren().add(createLabel("08:00 Minutos", 12, "#111", Boolean.TRUE));

        HBox dois = new HBox();

        VBox doisUm = new VBox();
        doisUm.getChildren().add(createLabel(Utils.formatToMoney(new BigDecimal(order.getTotal())).toString(), 18, "#545454", Boolean.TRUE));

        HBox hboxNome = new HBox();
        hboxNome.setSpacing(5.0);
        hboxNome.getChildren().add(creatIcon(FontAwesomeIcon.USER, "25", "#838383"));
        hboxNome.getChildren().add(createLabel(order.getClientInfo().getName(), 16, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxNome);

        HBox hboxTelefone = new HBox();
        hboxTelefone.setSpacing(5.0);
        hboxTelefone.getChildren().add(creatIcon(FontAwesomeIcon.PHONE, "25", "#838383"));
        hboxTelefone.getChildren().add(createLabel(order.getClientInfo().getPhone(), 16, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxTelefone);

        HBox doisdois = new HBox();
        doisdois.setSpacing(5.0);
        doisdois.setPrefWidth(160.0);
        doisdois.setMinWidth(160.0);
        doisdois.setMaxWidth(160.0);
        doisdois.setAlignment(Pos.CENTER_RIGHT);
        JFXButton button1 = createButton(FontAwesomeIcon.CHECK, "#00bdaa");
        button1.setOnAction((ActionEvent event) -> {
            try {
                order.setStatus("Finalizado");
                Node n = view.boxAguardandoFinalizacao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoFinalizacao.getChildren().remove(n);
                orderService.update(order);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        doisdois.getChildren().add(button1);

        JFXButton button2 = createButton(FontAwesomeIcon.CLOSE, "#cd4c51");
        button2.setOnAction((ActionEvent event) -> {
            try {
                order.setStatus("Produzindo");
                Node n = this.view.boxAguardandoFinalizacao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoFinalizacao.getChildren().remove(n);
                this.view.boxAguardandoProducao.getChildren().add(createCardOrderProduzindo(order));
                orderService.update(order);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        doisdois.getChildren().add(button2);
        JFXButton button3 = createButton(FontAwesomeIcon.INFO, "#400082");
        doisdois.getChildren().add(button3);
        doisUm.getChildren().add(doisdois);
        dois.setHgrow(doisUm, Priority.ALWAYS);
        dois.getChildren().add(doisUm);

        dois.getChildren().add(doisdois);

        HBox tres = new HBox();
        tres.setSpacing(5.0);
        if (order.getDelivery()) {
            tres.getChildren().add(creatIcon(FontAwesomeIcon.MAP_MARKER, "25", "#838383"));
            tres.getChildren().add(createLabel(order.getAddress().getStreet() + "\n"
                    + order.getAddress().getSuburb() + " - " + order.getAddress().getAuto(), 13, "#545454", Boolean.FALSE));
        } else {
            tres.getChildren().add(creatIcon(FontAwesomeIcon.TABLE, "25", "#838383"));
            tres.getChildren().add(createLabel("Retirada Local", 13, "#545454", Boolean.FALSE));
        }
        saida.getChildren().add(um);
        saida.getChildren().add(dois);
        saida.getChildren().add(tres);
        return saida;
    }

    private FontAwesomeIconView creatIcon(FontAwesomeIcon fi, String size, String color) {
        FontAwesomeIconView i = new FontAwesomeIconView(fi);
        i.setSize(size);
        i.setFill(Paint.valueOf(color));
        return i;
    }

    private Label createLabel(String text, Integer size, String color, Boolean bold) {
        Label lblPedido = new Label(text);
        lblPedido.setStyle("-fx-font-size: " + size + "px; " + (bold == true ? "-fx-font-weight: bold" : "") + ";");
        lblPedido.setTextFill(Paint.valueOf(color));
        return lblPedido;
    }

    private JFXButton createButton(FontAwesomeIcon icon, String color) {
        JFXButton bt = new JFXButton();
        bt.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        bt.setStyle("-fx-background-color: " + color);
        bt.setGraphic(creatIcon(icon, "25", "#fff"));
        bt.setPrefSize(50, 50);
        return bt;

    }
}
