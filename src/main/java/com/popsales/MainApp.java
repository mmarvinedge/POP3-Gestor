package com.popsales;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.ini4j.Ini;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        File f = new File("C:/popsales/config.ini");
        if (f.exists()) {
            Sessao.ini = new Ini(f);
        } else {
            f.getParentFile().mkdir();
            f.createNewFile();
            Sessao.ini = new Ini();
            Sessao.ini.store(f);
        }

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LoginFXML.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Entrar POP3");
        stage.getIcons().add(new Image("/img/sales.png"));
        stage.setScene(scene);
        stage.setResizable(false);
        setUserAgentStylesheet(STYLESHEET_MODENA);
        Font.loadFont(MainApp.class.getResourceAsStream("styles/FUTURAXK.TTF"), 10);
        stage.show();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
