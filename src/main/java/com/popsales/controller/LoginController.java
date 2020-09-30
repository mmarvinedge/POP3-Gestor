/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.popsales.Constantes;
import com.popsales.Sessao;
import com.popsales.Utils;
import com.popsales.components.Mensagem;
import com.popsales.model.Company;
import com.popsales.model.User;
import com.popsales.services.CompanyServices;
import com.popsales.services.LoginService;
import com.popsales.services.ProductService;
import com.popsales.services.VersaoService;
import java.awt.Event;
import com.popsales.util.DateUtil;
import com.popsales.util.FileUtil;
import com.popsales.ws.Client_API;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.SwingUtilities;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

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
    static WebSocketContainer container;
    static String uri;

    public static Session session;
    public static Client_API clientSocket;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        VersaoService v = new VersaoService();
        verificarDiretorios();
        Platform.runLater(() -> {
            if (Constantes.versao < v.getVersao()) {
                Mensagem.dialogAlert("Identificamos uma atualização!\n\n"
                        + "Versão Corrente: " + Constantes.versao + "\n"
                        + "Versão Disponível: " + v.getVersao(), null, btnLogin.getScene().getWindow());
                if (new File("C:\\popsales\\update.jar").exists()) {
                    executarUpdate();
                    downloadSrc();
                    System.exit(0);
                } else {
                    downloadUpdate();
                    executarUpdate();
                    System.exit(0);
                }

            }
            System.out.println("DOWNLOAD EXAMPLE");

            downloadExample();
            System.out.println("DOWNLOAD JS");
            downloadClientJS();

            iptSenha.setOnKeyPressed((KeyEvent event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    entrar(new ActionEvent());
                }
            });
        });
    }

    public void verificarDiretorios() {
        if (!new File("C:\\popsales").exists()) {
            new File("C:\\popsales").mkdirs();
        }
        if (!new File("C:\\popsales\\update").exists()) {
            new File("C:\\popsales\\update").mkdirs();
        }
    }

    public void executarUpdate() {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd \"C:\\popsales\" && java -jar update.jar");
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadUpdate() {
        try {
            URL url = new URL("http://metresistemas.com.br/update.jar");
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream("C:\\popsales\\update.jar");
            java.io.BufferedOutputStream bout = new BufferedOutputStream(
                    fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                // calculate progress
                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);

                // update progress bar
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("PROGRESS: " + ((currentProgress / 1000) / 100f));
                            }
                        });
                    }
                });
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadExample() {
        try {
            URL url = new URL("http://metresistemas.com.br/example.js");
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream("C:\\popsales\\bin\\example.js");
            java.io.BufferedOutputStream bout = new BufferedOutputStream(
                    fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                // calculate progress
                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);

                // update progress bar
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("PROGRESS: " + ((currentProgress / 1000) / 100f));
                            }
                        });
                    }
                });
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadClientJS() {
        try {
            URL url = new URL("http://metresistemas.com.br/Client.js");
            HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
            long completeFileSize = httpConnection.getContentLength();

            java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());
            java.io.FileOutputStream fos = new java.io.FileOutputStream("C:\\popsales\\bin\\src\\Client.js");
            java.io.BufferedOutputStream bout = new BufferedOutputStream(
                    fos, 1024);
            byte[] data = new byte[1024];
            long downloadedFileSize = 0;
            int x = 0;
            while ((x = in.read(data, 0, 1024)) >= 0) {
                downloadedFileSize += x;

                // calculate progress
                final int currentProgress = (int) ((((double) downloadedFileSize) / ((double) completeFileSize)) * 100000d);

                // update progress bar
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("PROGRESS: " + ((currentProgress / 1000) / 100f));
                            }
                        });
                    }
                });
                bout.write(data, 0, x);
            }
            bout.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> files
            = new ArrayList(Arrays.asList("http://metresistemas.com.br/pop_files/factories/ChatFactory.js",
                    "http://metresistemas.com.br/pop_files/factories/ContactFactory.js",
                    "http://metresistemas.com.br/pop_files/structures/Base.js",
                    "http://metresistemas.com.br/pop_files/structures/BusinessContact.js",
                    "http://metresistemas.com.br/pop_files/structures/Chat.js",
                    "http://metresistemas.com.br/pop_files/structures/ClientInfo.js",
                    "http://metresistemas.com.br/pop_files/structures/Contact.js",
                    "http://metresistemas.com.br/pop_files/structures/GroupChat.js",
                    "http://metresistemas.com.br/pop_files/structures/GroupNotification.js",
                    "http://metresistemas.com.br/pop_files/structures/index.js",
                    "http://metresistemas.com.br/pop_files/structures/Location.js",
                    "http://metresistemas.com.br/pop_files/structures/Message.js",
                    "http://metresistemas.com.br/pop_files/structures/MessageMedia.js",
                    "http://metresistemas.com.br/pop_files/structures/PrivateChat.js",
                    "http://metresistemas.com.br/pop_files/structures/PrivateContact.js",
                    "http://metresistemas.com.br/pop_files/util/Constants.js",
                    "http://metresistemas.com.br/pop_files/util/Injected.js",
                    "http://metresistemas.com.br/pop_files/util/InterfaceController.js",
                    "http://metresistemas.com.br/pop_files/util/Util.js"));

    public void downloadSrc() {
        files.forEach(path -> {
            try {
                URL url = new URL(path);
                HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                long completeFileSize = httpConnection.getContentLength();

                java.io.BufferedInputStream in = new java.io.BufferedInputStream(httpConnection.getInputStream());

                String novo = path.replace("http://metresistemas.com.br/pop_files/", "C:\\popsales\\bin\\src\\");

                java.io.FileOutputStream fos = new java.io.FileOutputStream(novo);
                java.io.BufferedOutputStream bout = new BufferedOutputStream(
                        fos, 1024);
                byte[] data = new byte[1024];
                long downloadedFileSize = 0;
                int x = 0;
                while ((x = in.read(data, 0, 1024)) >= 0) {
                    bout.write(data, 0, x);
                }
                bout.close();
                in.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    @FXML
    private void entrar(ActionEvent event) {
        if (login()) {
            return;
        }
    }

    private boolean login() {
        if (iptUser.getText().isEmpty()) {
            return true;
        }
        if (iptSenha.getText().isEmpty()) {
            return true;
        }
        try {
            Sessao.user = loginService.Login(new User(iptUser.getText(), iptSenha.getText()));
        } catch (IOException ex) {
            ex.printStackTrace();
            Mensagem.dialogAlert(ex.getMessage(), null, iptUser.getScene().getWindow());
            return true;
        } catch (Exception e) {
            Mensagem.dialogAlert(e.getMessage(), null, iptUser.getScene().getWindow());
            e.printStackTrace();
            return true;
        }
        System.out.println(Sessao.user.getName());
        if (Sessao.user.getName() != null) {
            if (Sessao.user.getName().equalsIgnoreCase("trialexpired")) {
                Mensagem.dialogAlert("Seu período teste de 15 dias se encerraram, para continuar utilizando entre em contato com seu agente de vendas!", btnLogin, btnLogin.getScene().getWindow());
            } else if (Sessao.user.getName().equalsIgnoreCase("block")) {
                Mensagem.dialogAlert("Sua licença expirou, para renovar acesse nosso site.", btnLogin, btnLogin.getScene().getWindow());
            } else {
                Sessao.company = companyServices.loadCompany(Sessao.user.getCompanyId());
                List<String> printers = productServices.getPrinters();
                Sessao.impressorasProdutos = new ArrayList();
                Sessao.impressorasWindows = new ArrayList();
                printers.forEach(c -> {
                    Sessao.impressorasProdutos.add(c);
                });
                PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
                for (PrintService ps : printServices) {
                    Sessao.impressorasWindows.add(ps.getName());
                }
                socketAPI(Sessao.company);
                abreJanela();
            }
        } else {
            Mensagem.dialogAlert("Usuário ou senha inválidos, tente novamente.", btnLogin, btnLogin.getScene().getWindow());
            System.out.println("USUARIO NAO ENCONTRADO!");
        }
        return false;
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
            stage.getIcons().add(new Image("/img/sales.png"));

            stage.setTitle("POP Sales");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();
            ((Stage) btnLogin.getScene().getWindow()).close();
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            login();
        }
    }

    public static void socketAPI(Company company) {
        try {
            container = ContainerProvider.getWebSocketContainer();
            String url = "popsales.ddns.net/ws";
            uri = "ws://" + url + "/ws/" + company.getId();
            System.out.println("URL Socket SG: " + uri);
            if (session == null) {
                container.setAsyncSendTimeout(1000);
                clientSocket = new Client_API();
                session = container.connectToServer(clientSocket.getClass(), URI.create(uri));
            }
        } catch (DeploymentException ex) {
            System.err.println("O sistema não conseguiu conectar ao websocket (API) - Provavelmente ele está offline ou fora da rede.");
        } catch (IOException ex) {
            System.err.println("Não foi possivel conectar no websocket: " + uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
