/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.popsales.components;

import com.popsales.MainApp;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 *
 * @author Renato
 */
public class Mensagem {

    public static BigDecimal dialogNumber(String title, Region region) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogNumber.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            DialogNumberController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRegion(region);
            controller.setTitulo(title);
            if (region != null) {
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (!controller.getOutput().isEmpty()) {
                BigDecimal saida = new BigDecimal(controller.getOutput());
                return saida;
            } else {
                return new BigDecimal("-1");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new BigDecimal("-1");
        }
    }

    public static BigDecimal dialogNumber(String title, Region region, Scene cena) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogNumber.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.initOwner(cena.getWindow());
            DialogNumberController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRegion(region);
            controller.setTitulo(title);
            if (region != null) {
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (!controller.getOutput().isEmpty()) {
                BigDecimal saida = new BigDecimal(controller.getOutput());
                return saida;
            } else {
                return new BigDecimal("-1");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new BigDecimal("-1");
        }
    }

    public static BigDecimal dialogDecimal(String title, Region region) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogDecimal.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            DialogDecimalController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTitulo(title);

            if (region != null) {
                controller.setRegion(region);
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (!controller.getOutput().isEmpty()) {
                BigDecimal saida = new BigDecimal(controller.getOutput().replace(".", "").replace(",", "."));
                System.out.println("VALOR CAPTURADO!!!");
                return saida;
            } else {
                return BigDecimal.ZERO;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BigDecimal dialogTextDecimal(String title, Region region) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogTextDecimal.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            DialogTextDecimalController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTitulo(title);

            if (region != null) {
                controller.setRegion(region);
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (!controller.getOutput().isEmpty()) {
                BigDecimal saida = new BigDecimal(controller.getOutput().replace(",", "."));
                System.out.println("VALOR CAPTURADO!!!");
                return saida;
            } else {
                return BigDecimal.ZERO;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BigDecimal dialogTextDecimal(String title, Region region, Scene cena) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogTextDecimal.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.initOwner(cena.getWindow());
            DialogTextDecimalController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTitulo(title);

            if (region != null) {
                controller.setRegion(region);
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (!controller.getOutput().isEmpty()) {
                BigDecimal saida = new BigDecimal(controller.getOutput().replace(",", "."));
                System.out.println("VALOR CAPTURADO!!!");
                return saida;
            } else {
                return BigDecimal.ZERO;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dialogText(String title, String assunto, Region region) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogText.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            DialogTextController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setTitulo(title);
            controller.setAssunto(assunto);

            if (region != null) {
                controller.setRegion(region);
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (region != null) {
                controller.setRegion(region);
                region.setVisible(false);
            }
            if (controller.getOutput() != null && !controller.getOutput().isEmpty()) {
                return controller.getOutput().trim();
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String dialogPassword(String title, Region region) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogPassword.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            DialogPasswordController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRegion(region);
            controller.setTitulo(title);
            if (region != null) {
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (!controller.getOutput().isEmpty()) {
                return controller.getOutput();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dialogPassword(String title, Region region, Scene cena) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogPassword.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.initOwner(cena.getWindow());
            DialogPasswordController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRegion(region);
            controller.setTitulo(title);
            if (region != null) {
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (!controller.getOutput().isEmpty()) {
                return controller.getOutput();
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dialogPassword(String title, Region region, Boolean cripto) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogPassword.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            DialogPasswordController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setRegion(region);
            controller.setTitulo(title);
            if (region != null) {
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            if (!controller.getOutput().isEmpty()) {
                if (cripto) {
                    return controller.getOutput();
                } else {
                    return controller.getOutput();
                }

            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean dialogConfirm(String title, String mensagem, Region region, Window w) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogConfirmFXML.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.toFront();
            if (w != null) {
                dialogStage.initOwner(w);
            }
            DialogConfirmController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setRegion(region);
            controller.setTitulo(title);
            controller.setMensagem(mensagem);
            if (region != null) {
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            return controller.getCheck();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void dialogAlert(String mensagem, Region region, Window w) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogAlert.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setAlwaysOnTop(true);
            if (w != null) {
                dialogStage.initOwner(w);
            }
            DialogAlertController controller = loader.getController();
            controller.setStage(dialogStage);
            if (region != null) {
                region.setVisible(true);
                controller.setRegion(region);
            }
            controller.setMensagem(mensagem);
            dialogStage.showAndWait();
            if (region != null) {
                region.setVisible(false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void dialogException(Exception e, Region region, Window w) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogException.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            dialogStage.setAlwaysOnTop(true);
            if (w != null) {
                dialogStage.initOwner(w);
            }
            DialogExceptionController controller = loader.getController();
            controller.setStage(dialogStage);
            if (region != null) {
                region.setVisible(true);
                controller.setRegion(region);
            }
            controller.setEx(e);
            dialogStage.showAndWait();
            if (region != null) {
                region.setVisible(false);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void dialogInformacao(String mensagem, Region region, Window w) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogInformacao.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            if (w != null) {
                dialogStage.initOwner(w);
            }
            DialogInformacaoController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setRegion(region);
            controller.setMensagem(mensagem);
            if (region != null) {
                region.setVisible(true);
            }

//            new FadeInRightTransition(page).play();
            dialogStage.showAndWait();
//            KeyFrame start = new KeyFrame(Duration.ZERO,
//                    new KeyValue(page.translateXProperty(), page.getWidth()),
//                    new KeyValue(page.translateXProperty(), 0));
//            Timeline slide = new Timeline(start);
//
//            slide.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map dialogOption(String titulo, Region region, Window w, String aceita, String recusa) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/fxml/DialogRemember.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            if (w != null) {
                dialogStage.initOwner(w);
            }
            DialogRememberController controller = loader.getController();
            controller.setStage(dialogStage);
            controller.setRegion(region);
            controller.setTitulo(titulo);
            controller.setAceita(aceita);
            controller.setRecusa(recusa);
            if (region != null) {
                region.setVisible(true);
            }
            dialogStage.showAndWait();
            Map m = new HashMap();
            m.put("lembrar", controller.getLembrar());
            m.put("value", controller.getOutput());
            return m;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
