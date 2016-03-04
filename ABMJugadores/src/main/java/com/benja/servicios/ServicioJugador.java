package com.benja.servicios;

import com.benja.config.HibernateUtil;
import com.benja.controlador.ControladorJugadores;
import com.benja.modelo.Jugador;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

/**
 * Created by benjamin.salas on 16/04/2015.
 */
public class ServicioJugador {

    public void mensajeJugador(String titulo, int edad, TableView<Jugador> tabla) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText("Jugador: " +
                        tabla.getSelectionModel().getSelectedItem().getNombre() + " " +
                        tabla.getSelectionModel().getSelectedItem().getApellido() + "\n" +
                        "Fecha de Nac.: " +
                        tabla.getSelectionModel().getSelectedItem().getFechaNac() + " " +
                        " (" + edad + " años)" + "\nPosición: " +
                        tabla.getSelectionModel().getSelectedItem().getPosicion()
        );
        alert.showAndWait();
    }

    private void mensajeAltaBajaJugador(String titulo, Jugador nuevoJugador) {
        int edad = 0; // calcular
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText("Jugador: " +
                        nuevoJugador.getNombre() + " " +
                        nuevoJugador.getApellido() + "\n" +
                        "Fecha de Nac.: " +
                        nuevoJugador.getFechaNac() + " " +
                        " (" + edad + " años)" + "\nPosición: " +
                        nuevoJugador.getPosicion()
        );
        alert.showAndWait();
    }

    public void Baja(TableView<Jugador> tabla) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Baja");
        alert.setHeaderText("Está a punto de borrar a " +
                tabla.getSelectionModel().getSelectedItem().getNombre() + " " +
                tabla.getSelectionModel().getSelectedItem().getApellido() + " de la base de datos");
        alert.setContentText("¿Esta seguro?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Jugador jugador = tabla.getSelectionModel().getSelectedItem();
            SessionFactory sf = HibernateUtil.getSessionFactory();
            Session s = sf.openSession();
            org.hibernate.Transaction tx = s.beginTransaction();
            try {
                s.delete(jugador);
                tx.commit();
                s.close();
                mensajeAltaBajaJugador("Jugador Eliminado de la tabla", jugador);
            } catch (Exception ex) {
                System.out.println("" + ex);
            }
        }
    }

    public void Alta(ControladorJugadores controladorJugadores) {

        Jugador nuevoJugador = new Jugador(controladorJugadores.campoNombre.getText(),
                controladorJugadores.campoApellido.getText(),
                String.valueOf(controladorJugadores.campoFechaNac.getText()),
                String.valueOf(controladorJugadores.comboPosicion.getValue()));

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            s.save(nuevoJugador);
            tx.commit();
            s.close();
            mensajeAltaBajaJugador("Nuevo Jugador", nuevoJugador);

        } catch (Exception ex) {
            System.out.println("" + ex);
        }
    }


    public void Modificacion(ControladorJugadores controladorJugadores) {
        Jugador jugador = controladorJugadores.tabla.getSelectionModel().getSelectedItem();

        jugador.setNombre(controladorJugadores.campoNombre.getText());
        jugador.setApellido(controladorJugadores.campoApellido.getText());
        jugador.setFechaNac(String.valueOf(controladorJugadores.campoFechaNac.getText()));
        jugador.setPosicion(String.valueOf(controladorJugadores.comboPosicion.getValue()));

        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        org.hibernate.Transaction tx = s.beginTransaction();

        try {
            s.update(jugador);
            tx.commit();
            s.close();
            mensajeJugador("Jugador Actualizado", 0, controladorJugadores.tabla);
        } catch (Exception ex) {
            System.out.println("" + ex);
        }
    }

    public void poblarTabla(TableView<Jugador> tabla) {
        ObservableList<Jugador> datosAct = FXCollections.observableArrayList(cargarDatos());
        tabla.setItems(datosAct);
    }

    private List<Jugador> cargarDatos() {
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        org.hibernate.Transaction tx = s.beginTransaction();

        List<Jugador> jugadores = null;

        try {
            jugadores = s.createQuery("from Jugador").list();
            tx.commit();
            s.close();
            return jugadores;

        } catch (Exception ex) {
            System.out.println("" + ex);
            return jugadores;
        }
    }

    private List<Jugador> cargarDatosConFiltro() {
        return null;
    }

    public void poblarCombos(ComboBox comboPosicion) {

        ObservableList<String> listPosiciones = FXCollections.observableArrayList(
                "Arquero", "Defensor", "Mediocampista", "Delantero"
        );

        comboPosicion.setItems(listPosiciones);
    }

    public boolean validar(ControladorJugadores controladorJugadores) {
        if (!controlarFecha(controladorJugadores.campoFechaNac.getText())) {
            return false;
        }
        if (controladorJugadores.campoNombre.getText().equals("")) {
            return false;
        }
        if (controladorJugadores.campoApellido.getText().equals("")) {
            return false;
        }
        if (controladorJugadores.comboPosicion.getSelectionModel().getSelectedItem().equals("Seleccione")) {
            return false;
        } else {
            return true;
        }
    }

    private boolean controlarFecha(String fecha) {
        try {
            char digito0 = fecha.charAt(0);
            char digito1 = fecha.charAt(1);
            char digito2 = fecha.charAt(2);
            char digito3 = fecha.charAt(3);
            char digito4 = fecha.charAt(4);
            char digito5 = fecha.charAt(5);
            char digito6 = fecha.charAt(6);
            char digito7 = fecha.charAt(7);
            char digito8 = fecha.charAt(8);
            char digito9 = fecha.charAt(9);

            String diaString = "" + digito0 + digito1;
            String mesString = "" + digito3 + digito4;
            String añoString = "" + digito6 + digito7 + digito8 + digito9;
            int dia = Integer.parseInt(diaString);
            int mes = Integer.parseInt(mesString);
            int año = Integer.parseInt(añoString);

            if (año >= 2015) {//fecha actual
                return false;
            }
            if (dia == 31 && (mes == 2 || mes == 4 || mes == 6 || mes == 9 || mes == 11)) {
                return false;
            }
            if ((dia == 29) && mes == 2) {
                if (año % 4 != 0) {
                    return false;
                }
            }
            if ((dia == 30) && mes == 2) {
                return false;
            }

            System.out.println("dia: " + dia + " mes: " + mes);

            if (!Character.isDigit(digito0))
                return false;
            if (!Character.isDigit(digito1))
                return false;
            if (digito2 != '/')
                return false;
            if (!Character.isDigit(digito3))
                return false;
            if (!Character.isDigit(digito4))
                return false;
            if (digito5 != '/')
                return false;
            if (!Character.isDigit(digito6))
                return false;
            if (!Character.isDigit(digito7))
                return false;
            if (!Character.isDigit(digito8))
                return false;
            if (!Character.isDigit(digito9))
                return false;
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    public void validarCampo(TextField campo) {

        campo.setOnKeyTyped(new EventHandler<KeyEvent>() {

            boolean campoNombreflag = true;

            @Override
            public void handle(KeyEvent event) {
                String s = event.getCharacter();
                char c = s.charAt(0);
                if (Character.isAlphabetic(c) || (s.equals(" ") && !campoNombreflag)) {
                    if (s.equals(" ")) {
                        campoNombreflag = true;
                    } else {
                        campoNombreflag = false;
                    }
                } else {
                    event.consume();
                }
            }
        });
    }

    public void mensajeValidacion() {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error de Validación");
        alert.setHeaderText("Datos incorrectos");
        alert.setContentText("Corrija para poder guardar");

        alert.showAndWait();
    }

    public void formatearCampos(ControladorJugadores controladorJugadores) {
        formatear(controladorJugadores.campoNombre);
        formatear(controladorJugadores.campoApellido);
    }

    private void formatear(TextField campo) {

        String s = campo.getText();

        if (campo.getText().equals("")) {

        } else {

            s = s.toLowerCase();

            StringBuilder b = new StringBuilder(s);
            int i = 0;
            do {
                b.replace(i, i + 1, b.substring(i, i + 1).toUpperCase());
                i = b.indexOf(" ", i) + 1;
            } while (i > 0 && i < b.length());

            s = String.valueOf(b);

            campo.setText(s);
        }
    }

    public void validarFecha(final TextField campoFecha) {


        campoFecha.setOnKeyTyped(new EventHandler<KeyEvent>() {
            int pos = 0;

            @Override
            public void handle(KeyEvent event) {
                String s = event.getCharacter();
                char c = s.charAt(0);
                if ((Character.isDigit(c) || s.equals("/")) && campoFecha.getText().length() < 10) {

                } else {
                    event.consume();
                }
            }
        });

    }

    public void poblarTabla(TableView<Jugador> tabla, String text) {

        ObservableList<Jugador> datosAct = FXCollections.observableArrayList(cargarDatos());

        for (Jugador jugador : datosAct) {

            if (jugador.getApellido().contains(text)) {
                datosAct.remove(jugador);
            }
        }

        tabla.setItems(datosAct);
    }
}
