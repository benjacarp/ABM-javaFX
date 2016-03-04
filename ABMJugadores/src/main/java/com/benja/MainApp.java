package com.benja;

import com.benja.config.HibernateUtil;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MainApp extends Application {

    public static void main(String[] args) throws Exception {

        launch(args);
        
    }

    public void start(Stage stage) throws Exception {

        String fxmlFile = "/fxml/VistaMenuPrincipal.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));

        Scene scene = new Scene(root, 400, 300);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("Menu Principal");
        stage.setScene(scene);

        HibernateUtil.getSessionFactory();
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                //if(HibernateUtil.getSessionFactory() != null){
                    HibernateUtil.shutdown();
                //}

            }
        });
    }
}
