/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller.view;

import com.jfoenix.controls.JFXButton;
import com.popsales.Sessao;
import com.popsales.Utils;
import com.popsales.components.Mensagem;
import com.popsales.components.WhatsappException;
import com.popsales.controller.GerenciaController;
import com.popsales.controller.InfoController;
import com.popsales.custom.Impressao;
import com.popsales.model.Attribute;
import com.popsales.model.AttributeValue;
import com.popsales.model.FlavorPizza;
import com.popsales.model.Order;
import com.popsales.services.OrderService;
import com.popsales.services.WhatsAppService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
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
    private WhatsAppService wppService = new WhatsAppService();

    private OrderService orderService = new OrderService();
    public Date ultimoUpdate;

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
        processarData(ordersAguardando);

        if (ordersAguardando.size() > 0) {
            Date ultimaData = ordersAguardando.stream().filter(p -> p.getDtRegistro() != null).map(m -> m.getDtRegistro()).max((Date o1, Date o2) -> {
                return o1.compareTo(o2);
            }).get();
            if (ultimoUpdate == null) {
                ultimoUpdate = ultimaData;
                Notifications.create().title("Informa????o").text("Novo Pedido!").showInformation();
                tocarAudio();
            } else if (ultimaData.after(ultimoUpdate)) {
                Notifications.create().title("Informa????o").text("Novo Pedido!").showInformation();
                tocarAudio();
                ((Stage) view.boxAguardandoAceite.getScene().getWindow()).setMaximized(true);
                ultimoUpdate = ultimaData;
            }
        }

        for (Order or : ordersAguardando) {
            this.view.boxAguardandoAceite.getChildren().add(createCardOrderAguardando(or));
        }
    }

    public void tocarAudio() {
        try {
            Clip oClip = AudioSystem.getClip();
            AudioInputStream oStream = AudioSystem.getAudioInputStream(new File("C:\\popsales\\button.wav"));
            oClip.open(oStream);
            oClip.loop(0); // Toca uma vez
        } catch (Exception e) {
        }
    }

    private void carregarProduzindo() {
        ordersProducao = orderService.getOrders("Produzindo");
        processarData(ordersProducao);

        for (Order or : ordersProducao) {
            this.view.boxAguardandoProducao.getChildren().add(createCardOrderProduzindo(or));
        }
    }

    private void carregarFinalizando() {
        ordersAguardandoFinalizar = orderService.getOrders("Finalizando");
        processarData(ordersAguardandoFinalizar);

        for (Order or : ordersAguardandoFinalizar) {
            this.view.boxAguardandoFinalizacao.getChildren().add(createCardOrderFinalizando(or));
        }
    }

    public static Date converterData(Order c) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", new Locale("pt_BR")).parse(c.getDtRegister());
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
        um.getChildren().add(createLabel(Utils.formataData(order.getDtRegistro(), "dd/MM/yyyy HH:mm:sss"), 12, "#111", Boolean.TRUE));

        HBox dois = new HBox();

        VBox doisUm = new VBox();
        doisUm.getChildren().add(createLabel(order.getNum_order() + " - " + Utils.formatToMoney(order.getTotal()).toString(), 18, "#545454", Boolean.TRUE));

        HBox hboxNome = new HBox();
        hboxNome.setSpacing(5.0);
        hboxNome.getChildren().add(creatIcon(FontAwesomeIcon.USER, "25", "#838383"));
        hboxNome.getChildren().add(createLabel(order.getClientInfo().getName(), 14, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxNome);

        HBox hboxTelefone = new HBox();
        hboxTelefone.setSpacing(5.0);
        hboxTelefone.getChildren().add(creatIcon(FontAwesomeIcon.PHONE, "25", "#838383"));
        hboxTelefone.getChildren().add(createLabel(order.getClientInfo().getPhone(), 14, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxTelefone);

        HBox doisdois = new HBox();
        doisdois.setSpacing(5.0);
        doisdois.setPrefWidth(160.0);
        doisdois.setMinWidth(160.0);
        doisdois.setMaxWidth(160.0);
        doisdois.setAlignment(Pos.CENTER_RIGHT);
        JFXButton buttonAceitarPedidoParaProduzir = createButton(FontAwesomeIcon.CHECK, "#00bdaa");
        buttonAceitarPedidoParaProduzir.setOnAction((ActionEvent event) -> {
            try {

                order.setStatus("Produzindo");
                order.setDtAcept(new Date());
                orderService.update(order);
                Impressao.imprimirOrder(order);
                Impressao.imprimirOrderControle(order);
                Boolean viaEntrega = false;
                try {
                    viaEntrega = Sessao.ini.get("Terminal", "ImprimirViaEntregador", Boolean.class);
                    if (viaEntrega) {
                        Impressao.imprimirOrderEntrega(order);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Node n = this.view.boxAguardandoAceite.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoAceite.getChildren().remove(n);
                this.view.boxAguardandoProducao.getChildren().add(createCardOrderProduzindo(order));

            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            try {
                enviarMensagemConfirmacao(order);
            } catch (Exception e) {
                System.out.println("ERRO AO ENVIAR A MENSAGEM");
            }

        });
        doisdois.getChildren().add(buttonAceitarPedidoParaProduzir);

        JFXButton bt2 = createButton(FontAwesomeIcon.CLOSE, "#cd4c51");
        bt2.setOnAction((ActionEvent event) -> {
            if (enviarMensagemCancelamento(order)) {
                try {
                    Node n = this.view.boxAguardandoAceite.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                    this.view.boxAguardandoAceite.getChildren().remove(n);
                    order.setStatus("Cancelado");
                    order.setDtAcept(null);
                    order.setDtRefuse(new Date());

                    orderService.update(order);
                } catch (IOException e) {
                    Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, e);
                }
            } else {
            }
        });
        doisdois.getChildren().add(bt2);

        JFXButton bt3 = createButton(FontAwesomeIcon.INFO, "#400082");
        bt3.setOnAction((ActionEvent event) -> {
            InfoController.order = order;
            InfoController.gerenciaController = view;
            novaJanelaAnchor(getClass().getResource("/fxml/InfoFXML.fxml"), view.boxAguardandoAceite.getScene().getWindow(), view.region);
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
            tres.getChildren().add(createLabel(order.getAddress().getStreet() + " - " + order.getAddress().getStreetNumber() + "\n"
                    + order.getAddress().getSuburb() + " - " + order.getAddress().getAuto(), 13, "#545454", Boolean.FALSE));
        } else {
            tres.getChildren().add(creatIcon(FontAwesomeIcon.TABLE, "25", "#838383"));
            tres.getChildren().add(createLabel("Retirada Local", 13, "#545454", Boolean.FALSE));
        }
        tres.setStyle("-fx-border-width:  1 0 0 0; -fx-border-color:  #ccc");
        saida.getChildren().add(um);
        saida.getChildren().add(dois);
        saida.getChildren().add(tres);

        saida.setStyle("-fx-border-width:  0 0 1 0; -fx-border-color:  #ccc");
        return saida;
    }

    public VBox createCardOrderProduzindo(Order order) {
        VBox saida = new VBox();
        saida.setId(order.getId());
        saida.setPadding(new Insets(5));
        saida.setMinHeight(150);
        HBox um = new HBox();
        um.setAlignment(Pos.TOP_RIGHT);
        um.getChildren().add(createLabel(Utils.formataData(order.getDtRegistro(), "dd/MM/yyyy HH:mm:sss"), 12, "#111", Boolean.TRUE));

        HBox dois = new HBox();

        VBox doisUm = new VBox();
        doisUm.getChildren().add(createLabel(order.getNum_order() + " - " + Utils.formatToMoney(order.getTotal()).toString(), 18, "#545454", Boolean.TRUE));

        HBox hboxNome = new HBox();
        hboxNome.setSpacing(5.0);
        hboxNome.getChildren().add(creatIcon(FontAwesomeIcon.USER, "25", "#838383"));
        hboxNome.getChildren().add(createLabel(order.getClientInfo().getName(), 14, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxNome);

        HBox hboxTelefone = new HBox();
        hboxTelefone.setSpacing(5.0);
        hboxTelefone.getChildren().add(creatIcon(FontAwesomeIcon.PHONE, "25", "#838383"));
        hboxTelefone.getChildren().add(createLabel(order.getClientInfo().getPhone(), 14, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxTelefone);

        HBox doisdois = new HBox();
        doisdois.setSpacing(5.0);
        doisdois.setPrefWidth(160.0);
        doisdois.setMinWidth(160.0);
        doisdois.setMaxWidth(160.0);
        doisdois.setAlignment(Pos.CENTER_RIGHT);
        JFXButton buttonConfirmarEnvio = createButton(FontAwesomeIcon.CHECK, "#00bdaa");
        buttonConfirmarEnvio.setOnAction((ActionEvent event) -> {
            enviarMensagemSaiuParaEntrega(order);
            try {

                order.setDtDelivery(new Date());
                orderService.update(order);
                Boolean viaEntrega = false;
                try {
                    viaEntrega = Sessao.ini.get("Terminal", "ImprimirViaEntregador", Boolean.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("viaEntrega: " + viaEntrega);
                if (viaEntrega == null) {
                    Impressao.imprimirOrderEntrega(order);
                } else if (!viaEntrega) {
                    Impressao.imprimirOrderEntrega(order);
                }

                Node n = this.view.boxAguardandoProducao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoProducao.getChildren().remove(n);
                this.view.boxAguardandoFinalizacao.getChildren().add(createCardOrderFinalizando(order));
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        doisdois.getChildren().add(buttonConfirmarEnvio);
        JFXButton buttonRetornarAguardando = createButton(FontAwesomeIcon.ARROW_LEFT, "#cd4c51");
        buttonRetornarAguardando.setOnAction((ActionEvent event) -> {
            try {
                order.setStatus("Aguardando");
                Node n = this.view.boxAguardandoProducao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoProducao.getChildren().remove(n);
                this.view.boxAguardandoAceite.getChildren().add(createCardOrderAguardando(order));
                order.setDtAcept(null);
                order.setDtDelivery(null);
                order.setDtFinish(null);
                order.setDtRefuse(null);
                orderService.update(order);

            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        doisdois.getChildren().add(buttonRetornarAguardando);

        JFXButton button3 = createButton(FontAwesomeIcon.INFO, "#400082");
        button3.setOnAction((ActionEvent event) -> {
            InfoController.order = order;
            InfoController.gerenciaController = view;
            novaJanelaAnchor(getClass().getResource("/fxml/InfoFXML.fxml"), view.boxAguardandoAceite.getScene().getWindow(), view.region);
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
            tres.getChildren().add(createLabel(order.getAddress().getStreet() + " - " + order.getAddress().getStreetNumber() + "\n"
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
        um.getChildren().add(createLabel(Utils.formataData(order.getDtRegistro(), "dd/MM/yyyy HH:mm:sss"), 12, "#111", Boolean.TRUE));

        HBox dois = new HBox();

        VBox doisUm = new VBox();
        doisUm.getChildren().add(createLabel(order.getNum_order() + " - " + Utils.formatToMoney(order.getTotal()).toString(), 18, "#545454", Boolean.TRUE));

        HBox hboxNome = new HBox();
        hboxNome.setSpacing(5.0);
        hboxNome.getChildren().add(creatIcon(FontAwesomeIcon.USER, "25", "#838383"));
        hboxNome.getChildren().add(createLabel(order.getClientInfo().getName(), 14, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxNome);

        HBox hboxTelefone = new HBox();
        hboxTelefone.setSpacing(5.0);
        hboxTelefone.getChildren().add(creatIcon(FontAwesomeIcon.PHONE, "25", "#838383"));
        hboxTelefone.getChildren().add(createLabel(order.getClientInfo().getPhone(), 14, "#545454", Boolean.TRUE));
        doisUm.getChildren().add(hboxTelefone);

        HBox doisdois = new HBox();
        doisdois.setSpacing(5.0);
        doisdois.setPrefWidth(160.0);
        doisdois.setMinWidth(160.0);
        doisdois.setMaxWidth(160.0);
        doisdois.setAlignment(Pos.CENTER_RIGHT);
        JFXButton button1 = createButton(FontAwesomeIcon.CHECK, "#00bdaa");
        button1.setOnAction((ActionEvent event) -> {
            enviarMensagemEntregue(order);
            try {

                order.setStatus("Finalizado");
                order.setDtFinish(new Date());
                order.setDtRefuse(null);
                orderService.update(order);

                Node n = view.boxAguardandoFinalizacao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoFinalizacao.getChildren().remove(n);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        doisdois.getChildren().add(button1);

        JFXButton button2 = createButton(FontAwesomeIcon.CLOSE, "#cd4c51");
        button2.setOnAction((ActionEvent event) -> {
            if (enviarMensagemCancelamento(order)) {
                try {
                    Node n = this.view.boxAguardandoFinalizacao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                    this.view.boxAguardandoFinalizacao.getChildren().remove(n);
                    order.setStatus("Cancelado");
                    order.setDtAcept(null);
                    order.setDtRefuse(new Date());
                    orderService.update(order);
                } catch (IOException ex) {
                    Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
            }
        });

        doisdois.getChildren().add(button2);
        JFXButton button3 = createButton(FontAwesomeIcon.INFO, "#400082");
        button3.setOnAction((ActionEvent event) -> {
            InfoController.order = order;
            InfoController.gerenciaController = view;
            novaJanelaAnchor(getClass().getResource("/fxml/InfoFXML.fxml"), view.boxAguardandoAceite.getScene().getWindow(), view.region);
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
            tres.getChildren().add(createLabel(order.getAddress().getStreet() + " - " + order.getAddress().getStreetNumber() + "\n"
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

    public void enviarMensagemEntregue(Order order) {
        try {

            String phone = "";
            phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
            phone = "55" + phone;
            System.out.println(phone);
            if (order.getDelivery()) {
                if (view.p != null) {
                    wppService.sendMessage(phone, "Pedido entregue, obrigado pela prefer??ncia. ????");
                } else {
                    Notifications.create().title("Aten??ao!").text("Whatsapp n??o est?? sendo executado!").showWarning();
                }
            } else {
                if (view.p != null) {
                    wppService.sendMessage(phone, "Pedido retirado, obrigado pela prefer??ncia. ????");
                } else {
                    Notifications.create().title("Aten??ao!").text("Whatsapp n??o est?? sendo executado!").showWarning();
                }
            }

            Thread.sleep(2000);

        } catch (WhatsappException ex) {
            Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    private void processarData(List<Order> lista) {
        lista.forEach(c -> {
            try {
                if (c.getDtRegister() != null) {
                    try {
                        c.setDtRegistro(converterData(c));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.out.println("ERRO AO CONVERTER: ");
                e.printStackTrace();
            }
        });
    }

    public static void novaJanelaAnchor(URL url, Window s, Region region) {
        try {
            Stage stage = new Stage();
            AnchorPane cena = new FXMLLoader().load(url);
            Scene cen = new Scene(cena);
            stage.setScene(cen);
            stage.setResizable(false);
            //stage.setAlwaysOnTop(true);
//            stage.alwaysOnTopProperty();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(s);
            region.setVisible(true);
            stage.showAndWait();
            region.setVisible(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Boolean enviarMensagemCancelamento(Order order) {
        try {
            if (Mensagem.dialogConfirm("Aten????o!", "Desejsa cancelar o pedido?", view.region, view.boxAguardandoAceite.getScene().getWindow())) {
                String phone = "";

                phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
                phone = "55" + phone;

                System.out.println(phone);
                if (view.p != null) {
                    wppService.sendMessage(phone, "??? Seu pedido " + order.getNum_order() + " foi cancelado.");
                } else {
                    Mensagem.dialogAlert("O WhatsApp n??o est?? sendo executado, seu cliente n??o receber?? as mensagens de atualiza????o do pedido.", view.region, view.boxAguardandoAceite.getScene().getWindow());
                }

                Thread.sleep(2000);
                return true;
            } else {
                return false;
            }

        } catch (WhatsappException ex) {
            Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void enviarMensagemConfirmacao(Order order) {
        try {
            String phone = "";
            // System.out.println(order.getClientInfo().getPhone().length());
            phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
            phone = "55" + phone;

            System.out.println(phone);
            StringBuilder sb = new StringBuilder();
            sb.append("# *SEU PEDIDO FOI CONFIRMADO*");
            sb.append("|_Acompanhe abaixo o seu pedido_||");
            sb.append("]*N?? PEDIDO*: ").append(order.getNum_order()).append("||");
            order.getProducts().forEach(p -> {
                sb.append("/// *Produto*: ").append(p.getName().toUpperCase()).append("|");
                sb.append("/// *Quantidade*: ").append(p.getQuantity().intValue()).append("|");
                sb.append("/// *Valor*: R$ ").append(p.getTotal()).append("|");
                if (p.getAttributes() != null && p.getAttributes().size() > 0) {
                    System.out.println("sizeeeeeeeeeeeeeeeeeeeee " + p.getAttributes().size());
                    sb.append("= *ADICIONAIS*|");
                    for (Attribute a : p.getAttributes()) {
                        for (AttributeValue v : a.getValues()) {
                            sb.append("?? *").append(v.getQuantity()).append("x* ").append(v.getName().toUpperCase()).append("|");
                        }
                    }
                } else if (p.getFlavors() != null) {
                    for (FlavorPizza f : p.getFlavors()) {
                        sb.append("?? *SABORES*: ").append(f.getFlavor().toUpperCase()).append("|");
                    }
                }
                sb.append("|");
            });
            sb.append("----------------|");
            if (order.getCoupon() != null && order.getCoupon() != "") {
                sb.append("?? *CUPOM:* ").append(order.getCoupon().toUpperCase());
                sb.append("|*Produtos:* R$ ").append(order.getSubtotal());
                sb.append("|*Desconto:* R$ ").append(order.getDiscountValue());
            }

            if (order.getDelivery()) {
                sb.append("|*Taxa de Entrega:* R$ ").append(order.getDeliveryCost());
            }

            sb.append("|*Total:* R$ ").append(order.getTotal());
            sb.append("||;; ").append(order.getClientInfo().getName());
            sb.append("|% ").append(order.getClientInfo().getPhone());
            sb.append("|[ ").append(order.getForma());
            if (order.getTroco() != null) {
                if (order.getTroco()) {
                    sb.append("|?? Troco para ").append(BigDecimal.valueOf(order.getTrocoPara()).setScale(2, RoundingMode.HALF_UP));
                }
            }
            if (order.getDelivery()) {
                sb.append("|{ ").append(order.getAddress().getStreet()).append("- ").append(order.getAddress().getStreetNumber()).append(", ").append(order.getAddress().getAuto()).append(", ").append(order.getAddress().getSuburb());
            } else {
                sb.append("|@ Voc?? optou por retirar no local, para solicitar nosso endere??o digite *localiza????o*");
            }
            sb.append("||?? *ATEN????O*: Para solicitar altera????es no seu pedido nos fa??a uma liga????o.");
            sb.append("||");
            sb.append("_Para acompanhar o seu pedido acesse o link abaixo_||");
            sb.append("http://food.popsales.com.br/popsales/situacao/pedido/" + order.getId());
            String msg = sb.toString();
            System.out.println(msg);
            if (view.p != null) {
                wppService.sendMessage(phone, msg);
            } else {
                Mensagem.dialogAlert("O WhatsApp n??o est?? sendo executado, seu cliente n??o receber?? as mensagens de atualiza????o do pedido.", view.region, view.boxAguardandoAceite.getScene().getWindow());
            }
            Thread.sleep(2000);
        } catch (WhatsappException e) {
            Notifications.create().title("Aten????o").text("Whatsapp n??o rodando!").showWarning();
        } catch (InterruptedException ex) {
            Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void enviarMensagemSaiuParaEntrega(Order order) {
        try {
            String phone = "";

            phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
            phone = "55" + phone;

            System.out.println(phone);
            if (order.getDelivery()) {
                if (view.p != null) {
                    wppService.sendMessage(phone, "Seu pedido acabou de sair para entrega ????????");
                } else {
                    Mensagem.dialogAlert("O WhatsApp n??o est?? sendo executado, seu cliente n??o receber?? as mensagens de atualiza????o do pedido.", view.region, view.boxAguardandoAceite.getScene().getWindow());
                }
            } else {
                if (view.p != null) {
                    wppService.sendMessage(phone, "???? Seu pedido est?? pronto para ser retirado");
                } else {
                    Mensagem.dialogAlert("O WhatsApp n??o est?? sendo executado, seu cliente n??o receber?? as mensagens de atualiza????o do pedido.", view.region, view.boxAguardandoAceite.getScene().getWindow());
                }
            }
            System.out.println("CLICK");
            order.setStatus("Finalizando");
            Thread.sleep(2000);

        } catch (WhatsappException ex) {
            Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
