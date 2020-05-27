/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller.view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXButton;
import com.popsales.Sessao;
import com.popsales.Utils;
import static com.popsales.Utils.formataData;
import com.popsales.components.Mensagem;
import com.popsales.components.WhatsappException;
import com.popsales.controller.GerenciaController;
import com.popsales.controller.InfoController;
import com.popsales.custom.Impressao;
import com.popsales.model.Attribute;
import com.popsales.model.AttributeValue;
import com.popsales.model.FlavorPizza;
import com.popsales.model.Order;
import com.popsales.pojo.MotivoCancelamento;
import com.popsales.services.OrderService;
import com.popsales.services.WhatsAppService;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

    private List<MotivoCancelamento> motivosCancelamento;

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
        carregaMotivosCancelamentoJSON();
    }

    public void loadData() {
        carregarAguardando();
        carregarProduzindo();
        carregarFinalizando();
    }

    private void carregarAguardando() {
        ordersAguardando = orderService.getOrders("Aguardando");
        ordersAguardando.forEach(c -> {
            try {
                if (c.getDtRegister() != null) {
                    try {
                        c.setDtRegistro(new SimpleDateFormat("yyyy-MM-ddd hh:mm:ss", new Locale("pt_BR")).parse(c.getDtRegister()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.out.println("ERRO AO CONVERTER: ");
                e.printStackTrace();
            }
        });
        if (ordersAguardando.size() > 0) {
            Date ultimaData = ordersAguardando.stream().filter(p -> p.getDtRegistro() != null).map(m -> m.getDtRegistro()).max((Date o1, Date o2) -> {
                return o1.compareTo(o2);
            }).get();
            System.out.println("ULTIMA DATA: " + formataData(ultimaData, "dd/MM/yyyy HH:mm:ss"));
            System.out.println("ULTIMA ATUAL: " + formataData(ultimoUpdate, "dd/MM/yyyy HH:mm:ss"));
            if (ultimoUpdate == null) {
                ultimoUpdate = ultimaData;
                Notifications.create().title("Informação").text("Novo Pedido!").showInformation();
                tocarAudio();
            } else if (ultimaData.after(ultimoUpdate)) {
                Notifications.create().title("Informação").text("Novo Pedido!").showInformation();
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

        ordersProducao.forEach(c -> {
            try {
                if (c.getDtRegister() != null) {
                    try {
                        c.setDtRegistro(new SimpleDateFormat("yyyy-MM-ddd hh:mm:ss", new Locale("pt_BR")).parse(c.getDtRegister()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.out.println("ERRO AO CONVERTER: ");
                e.printStackTrace();
            }
        });

        for (Order or : ordersProducao) {
            this.view.boxAguardandoProducao.getChildren().add(createCardOrderProduzindo(or));
        }
    }

    private void carregarFinalizando() {
        ordersAguardandoFinalizar = orderService.getOrders("Finalizando");

        ordersAguardandoFinalizar.forEach(c -> {
            try {
                if (c.getDtRegister() != null) {
                    try {
                        c.setDtRegistro(new SimpleDateFormat("yyyy-MM-ddd hh:mm:ss", new Locale("pt_BR")).parse(c.getDtRegister()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.out.println("ERRO AO CONVERTER: ");
                e.printStackTrace();
            }
        });

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
        JFXButton bt1 = createButton(FontAwesomeIcon.CHECK, "#00bdaa");
        bt1.setOnAction((ActionEvent event) -> {
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
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("#*SEU PEDIDO FOI CONFIRMADO.*");
                    sb.append("|_Acompanhe abaixo o seu pedido_||");
                    sb.append("]*Nº PEDIDO*: ").append(order.getNum_order()).append("||");
                    order.getProducts().forEach(p -> {
                        sb.append("/ *Produto*: ").append(p.getName().toUpperCase()).append("|");
                        sb.append("/ *Quantidade*: ").append(p.getQuantity().intValue()).append("|");
                        sb.append("/ *Valor*: R$ ").append(p.getTotal()).append("|");
                        if (p.getAttributes() != null && p.getAttributes().size() > 0) {
                            System.out.println("sizeeeeeeeeeeeeeeeeeeeee " + p.getAttributes().size());
                            sb.append("= *ADICIONAIS*|");
                            for (Attribute a : p.getAttributes()) {
                                for (AttributeValue v : a.getValues()) {
                                    sb.append("¬ *").append(v.getQuantity()).append("x* ").append(v.getName().toUpperCase()).append("|");
                                }
                            }
                        } else if (p.getFlavors() != null) {
                            for (FlavorPizza f : p.getFlavors()) {
                                sb.append("¬ *SABORES*: ").append(f.getFlavor().toUpperCase()).append("|");
                            }
                        }
                        sb.append("|");
                    });
                    sb.append("----------------|");
                    if (order.getDelivery()) {
                        sb.append("|*Taxa de Entrega:* R$ ").append(order.getDeliveryCost());
                    }
                    sb.append("|*Total:* R$ ").append(order.getTotal());
                    sb.append("||! ").append(order.getClientInfo().getName());
                    sb.append("|% ").append(order.getClientInfo().getPhone());
                    sb.append("|[ ").append(order.getForma());
                    if (order.getTroco() != null) {
                        if (order.getTroco()) {
                            sb.append("|¨ Troco para ").append(order.getTrocoPara().floatValue());
                        }
                    }
                    if (order.getDelivery()) {
                        sb.append("|{ ").append(order.getAddress().getStreet()).append("- ").append(order.getAddress().getStreetNumber()).append(", ").append(order.getAddress().getAuto()).append(", ").append(order.getAddress().getSuburb());
                    } else {
                        sb.append("|@ Você optou por retirar no local, para solicitar nosso endereço digite *localização*");
                    }
                    String msg = sb.toString();
                    System.out.println(msg);
                    if (view.p != null) {
                        wppService.sendMessage(phone, msg);
                    } else {
                        Mensagem.dialogAlert("O WhatsApp não está sendo executado, seu cliente não receberá as mensagens de atualização do pedido.", view.region, view.boxAguardandoAceite.getScene().getWindow());
                    }
                    Thread.sleep(2000);
                } catch (WhatsappException e) {
                    Notifications.create().title("Atenção").text("Whatsapp não rodando!").showWarning();
                } catch (InterruptedException ex) {
                    Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
                }

                order.setStatus("Produzindo");
                Node n = this.view.boxAguardandoAceite.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoAceite.getChildren().remove(n);
                this.view.boxAguardandoProducao.getChildren().add(createCardOrderProduzindo(order));
                orderService.update(order);
                Impressao.imprimirOrder(order);
                Impressao.imprimirOrderControle(order);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        doisdois.getChildren().add(bt1);

        JFXButton bt2 = createButton(FontAwesomeIcon.CLOSE, "#cd4c51");
        bt2.setOnAction((ActionEvent event) -> {
            try {
                motivosCancelamento = motivosCancelamento.stream()
                        .filter(m -> Integer.parseInt(m.getCodigo()) < 800)
                        .collect(Collectors.toList());
                motivosCancelamento.sort((c1, c2) -> c1.getMotivo().compareTo(c2.getMotivo()));
                if (Mensagem.dialogConfirm("Atenção!", "Deseja cancelar o pedido?", view.region, view.boxAguardandoAceite.getScene().getWindow())) {
                    String phone = "";
                    String json = Mensagem.dialogComboBox("Cancelar Pedido!!!", motivosCancelamento);
                    if (json == null) {
                        return;
                    }else{
                        MotivoCancelamento motivo = new Gson().fromJson(json, MotivoCancelamento.class);
                        order.setCancelmentReason(motivo.getMotivo());
                    }
                    System.out.println(order.getClientInfo().getPhone().length());
                    if (order.getClientInfo().getPhone().length() == 14) {
                        phone = order.getClientInfo().getPhone().replace("(", "").replace(")9", "").replace("-", "");
                        phone = "55" + phone;
                    } else {
                        phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
                        phone = "55" + phone;
                    }
                    System.out.println(phone);
                    if (view.p != null) {
                        
                        wppService.sendMessage(phone, "Seu pedido " + order.getNum_order() + " foi cancelado.|Motivo: " + order.getCancelmentReason());
                    } else {
                        Mensagem.dialogAlert("O WhatsApp não está sendo executado, seu cliente não receberá as mensagens de atualização do pedido.", view.region, view.boxAguardandoAceite.getScene().getWindow());
                    }
                    order.setStatus("Cancelado");
                    Thread.sleep(2000);
                    Node n = this.view.boxAguardandoAceite.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                    this.view.boxAguardandoAceite.getChildren().remove(n);
                    orderService.update(order);
                }
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WhatsappException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
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
            try {
                String phone = "";
                if (order.getClientInfo().getPhone().length() == 14) {
                    phone = order.getClientInfo().getPhone().replace("(", "").replace(")9", "").replace("-", "");
                    phone = "55" + phone;
                } else {
                    phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
                    phone = "55" + phone;
                }
                System.out.println(phone);
                if (order.getDelivery()) {
                    if (view.p != null) {
                        wppService.sendMessage(phone, "Seu pedido acabou de sair para entrega ¢³");
                    } else {
                        Mensagem.dialogAlert("O WhatsApp não está sendo executado, seu cliente não receberá as mensagens de atualização do pedido.", view.region, view.boxAguardandoAceite.getScene().getWindow());
                    }
                } else {
                    if (view.p != null) {
                        wppService.sendMessage(phone, "Seu pedido está pronto para ser retirado.");
                    } else {
                        Mensagem.dialogAlert("O WhatsApp não está sendo executado, seu cliente não receberá as mensagens de atualização do pedido.", view.region, view.boxAguardandoAceite.getScene().getWindow());
                    }
                }
                System.out.println("CLICK");
                order.setStatus("Finalizando");
                Thread.sleep(2000);
                Node n = this.view.boxAguardandoProducao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoProducao.getChildren().remove(n);
                this.view.boxAguardandoFinalizacao.getChildren().add(createCardOrderFinalizando(order));
                orderService.update(order);
                Impressao.imprimirOrderEntrega(order);
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WhatsappException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        doisdois.getChildren().add(button1);
        JFXButton button2 = createButton(FontAwesomeIcon.ARROW_LEFT, "#cd4c51");
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
            try {

                String phone = "";
                if (order.getClientInfo().getPhone().length() == 14) {
                    phone = order.getClientInfo().getPhone().replace("(", "").replace(")9", "").replace("-", "");
                    phone = "55" + phone;
                } else {
                    phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
                    phone = "55" + phone;
                }
                System.out.println(phone);
                if (order.getDelivery()) {
                    if (view.p != null) {
                        wppService.sendMessage(phone, "Pedido entregue, obrigado pela preferência.");
                    } else {
                        Notifications.create().title("Atençao!").text("Whatsapp não está sendo executado!").showWarning();
                    }
                } else {
                    if (view.p != null) {
                        wppService.sendMessage(phone, "Pedido retirado, obrigado pela preferência.");
                    } else {
                        Notifications.create().title("Atençao!").text("Whatsapp não está sendo executado!").showWarning();
                    }
                }
                order.setStatus("Finalizado");
                Thread.sleep(2000);
                Node n = view.boxAguardandoFinalizacao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                this.view.boxAguardandoFinalizacao.getChildren().remove(n);
                orderService.update(order);

            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WhatsappException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        doisdois.getChildren().add(button1);

        JFXButton button2 = createButton(FontAwesomeIcon.CLOSE, "#cd4c51");
        button2.setOnAction((ActionEvent event) -> {
            try {
                motivosCancelamento = motivosCancelamento.stream()
                        .filter(m -> Integer.parseInt(m.getCodigo()) > 800)
                        .collect(Collectors.toList());
                motivosCancelamento.sort((c1, c2) -> c1.getMotivo().compareTo(c2.getMotivo()));
                if (Mensagem.dialogConfirm("Atenção!", "Deseja cancelar o pedido?", view.region, view.boxAguardandoAceite.getScene().getWindow())) {
                    String phone = "";
                    String json = Mensagem.dialogComboBox("Cancelar Pedido!!!", motivosCancelamento);
                    if (json == null) {
                        return;
                    }
                    if (order.getClientInfo().getPhone().length() == 14) {
                        phone = order.getClientInfo().getPhone().replace("(", "").replace(")9", "").replace("-", "");
                        phone = "55" + phone;
                    } else {
                        phone = order.getClientInfo().getPhone().replace("(", "").replace(")", "").replace("-", "");
                        phone = "55" + phone;
                    }
                    System.out.println(phone);
                    if (view.p != null) {
                        wppService.sendMessage(phone, "Seu pedido " + order.getNum_order() + " foi cancelado.");
                    } else {
                        Notifications.create().title("Atençao!").text("Whatsapp não está sendo executado!").showWarning();
                    }
                    order.setStatus("Cancelado");
                    Thread.sleep(2000);
                    Node n = this.view.boxAguardandoFinalizacao.getChildren().stream().filter(p -> p.getId().equals(order.getId())).findAny().get();
                    this.view.boxAguardandoFinalizacao.getChildren().remove(n);
                    orderService.update(order);
                }
            } catch (IOException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (WhatsappException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(GerenciaView.class.getName()).log(Level.SEVERE, null, ex);
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

    private void carregaMotivosCancelamentoJSON() {
        String json = "[\n"
                + "  {\n"
                + "    \"codigo\": 501,\n"
                + "    \"motivo\": \"PROBLEMAS DE SISTEMA\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 502,\n"
                + "    \"motivo\": \"PEDIDO EM DUPLICIDADE\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 503,\n"
                + "    \"motivo\": \"ITEM INDISPONÍVEL\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 504,\n"
                + "    \"motivo\": \"RESTAURANTE SEM MOTOBOY\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 505,\n"
                + "    \"motivo\": \"CARDÁPIO DESATUALIZADO\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 506,\n"
                + "    \"motivo\": \"PEDIDO FORA DA ÁREA DE ENTREGA\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 507,\n"
                + "    \"motivo\": \"CLIENTE GOLPISTA / TROTE\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 508,\n"
                + "    \"motivo\": \"FORA DO HORÁRIO DO DELIVERY\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 509,\n"
                + "    \"motivo\": \"DIFICULDADES INTERNAS DO RESTAURANTE\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 511,\n"
                + "    \"motivo\": \"ÁREA DE RISCO\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 512,\n"
                + "    \"motivo\": \"RESTAURANTE ABRIRÁ MAIS TARDE\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 513,\n"
                + "    \"motivo\": \"RESTAURANTE FECHOU MAIS CEDO\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 803,\n"
                + "    \"motivo\": \"ITEM INDISPONÍVEL\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 805,\n"
                + "    \"motivo\": \"RESTAURANTE SEM MOTOBOY\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 801,\n"
                + "    \"motivo\": \"OUTROS (OBRIGATÖRIO INFORMAR DESCRIÇÃO)\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 804,\n"
                + "    \"motivo\": \"CADASTRO DO CLIENTE INCOMPLETO - CLIENTE NÃO ATENDE\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 807,\n"
                + "    \"motivo\": \"PEDIDO FORA DA ÁREA DE ENTREGA\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 808,\n"
                + "    \"motivo\": \"CLIENTE GOLPISTA / TROTE\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 809,\n"
                + "    \"motivo\": \"FORA DO HORÁRIO DO DELIVERY\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 815,\n"
                + "    \"motivo\": \"DIFICULDADES INTERNAS DO RESTAURANTE\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 818,\n"
                + "    \"motivo\": \"TAXA DE ENTREGA INCONSISTENTE\"\n"
                + "  },\n"
                + "  {\n"
                + "    \"codigo\": 820,\n"
                + "    \"motivo\": \"ÁREA DE RISCO\"\n"
                + "  }\n"
                + "]";

        motivosCancelamento = new Gson().fromJson(json, new TypeToken<List<MotivoCancelamento>>() {
        }.getType());
    }
}
