package com.benja.controlador;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by benjamin.salas on 13/04/2015.
 */
public class ControladorMenuPrincipal {

    public void clickBtnJugadores(Event event) throws IOException {
        System.out.println("Jugadores");

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/VistaJugadores.fxml"));

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);                // esto imita el comportamiento
        stage.initModality(Modality.APPLICATION_MODAL);     // de un JDialog

        stage.setResizable(false);
        stage.setTitle("Gestionar Jugadores");

        Scene scene = new Scene(root,600,400);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setScene(scene);
        stage.show();
    }
}
