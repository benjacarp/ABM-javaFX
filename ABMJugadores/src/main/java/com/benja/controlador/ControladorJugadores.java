package com.benja.controlador;

import com.benja.modelo.Jugador;
import com.benja.servicios.ServicioJugador;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by benjamin.salas on 13/04/2015.
 */
public class ControladorJugadores implements Initializable {

    ///////////// elementos de la vista ///////////////
    public javafx.scene.control.TableView<Jugador> tabla;
    public javafx.scene.control.TableColumn<Jugador, String> colNombre;
    public javafx.scene.control.TableColumn<Jugador, String> colApellido;
    public javafx.scene.control.TableColumn<Jugador, String> colFecha;
    public javafx.scene.control.TableColumn<Jugador, String> colPos;
    public javafx.scene.control.TableColumn<Jugador, Integer> colId;

    public javafx.scene.control.Button btnVolver;
    public javafx.scene.control.Button btnGuardar;
    public javafx.scene.control.Button btnCancelar;
    public javafx.scene.control.Button btnModificar;
    public javafx.scene.control.Button btnEliminar;
    public javafx.scene.control.Button btnVer;

    public javafx.scene.control.TextField campoNombre;
    public javafx.scene.control.TextField campoApellido;
    public javafx.scene.control.TextField campoDni;
    public javafx.scene.control.TextField campoFechaNac;
    public javafx.scene.control.ComboBox comboPosicion;
    public javafx.scene.control.TextField campoBuscar;



    ///////////////// servicios ////////////////
    private ServicioJugador servicioJugador = new ServicioJugador();

    //////////////// eventos //////////////////
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Listeners de los campos para validacion
        servicioJugador.validarCampo(campoNombre);      //solo letras, sin espacios dobles
        servicioJugador.validarCampo(campoApellido);    //o comenzando con espacio
        servicioJugador.validarFecha(campoFechaNac);

        //inicializar tabla
        colId.setCellValueFactory(new PropertyValueFactory<Jugador, Integer>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Jugador, String>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Jugador, String>("apellido"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Jugador, String>("fechaNac"));
        colPos.setCellValueFactory(new PropertyValueFactory<Jugador, String>("posicion"));

        servicioJugador.poblarTabla(tabla);
        //inicializar combobox
        servicioJugador.poblarCombos(comboPosicion);
        // no se puede editar hasta no hacer clic en agregar
        deshabilitarEdicion();

        // seleccionar un registro habilita la modificacion y eliminacion
        tabla.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (tabla.getSelectionModel().getSelectedIndex() > -1) {
                    btnModificar.setDisable(false);
                    btnEliminar.setDisable(false);
                    btnVer.setDisable(false);

                } else {
                    btnModificar.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnVer.setDisable(true);
                }
            }
        });

        campoBuscar.setOnKeyReleased(new EventHandler<KeyEvent>() {

            boolean campoNombreflag = true;

            @Override
            public void handle(KeyEvent event) {
                if (campoBuscar.getText().equals("")) {
                    servicioJugador.poblarTabla(tabla);
                } else {
                    servicioJugador.poblarTabla(tabla, campoBuscar.getText());
                }
            }
        });
    }

    public void ver() {
        int edad = 999;// hacer la funcion que calcule la edad
        servicioJugador.mensajeJugador("Informacion del Jugador", edad, tabla);
    }

    public void agregar() {                         //click en el boton agregar
        habilitarEdicion();                         // habilita el panel de edicion
        limpiar();                                  // limpia los campos
        btnGuardar.setText("Dar Alta");
        tabla.getSelectionModel().clearSelection(); // deselecciona la tabla
        tabla.setDisable(false);                    // habilita la tabla xa seleccionar un jugador
        campoNombre.requestFocus();                 // para empezar a llenar directamente
    }

    public void eliminar(Event event) { //click en el boton borrar

        servicioJugador.Baja(tabla);
        servicioJugador.poblarTabla(tabla);
    }

    public void modificar(Event event) { //click en el boton modificar

        btnGuardar.setText("Guardar");
        habilitarEdicion();
        tabla.setDisable(true);
        // cargar la parte de edicion con los datos del jugador a modificar
        campoNombre.setText(tabla.getSelectionModel().getSelectedItem().getNombre());
        campoApellido.setText(tabla.getSelectionModel().getSelectedItem().getApellido());
        campoFechaNac.setText(tabla.getSelectionModel().getSelectedItem().getFechaNac());
        comboPosicion.setValue(tabla.getSelectionModel().getSelectedItem().getPosicion());
        //si se agregan muchos campos pasar a un servicio para ordenar
    }

    public void volver() { //click en el boton volver
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }

    public void guardar() {//mismo boton para alta y modificacion

        boolean valido = servicioJugador.validar(this);
        servicioJugador.formatearCampos(this);
        if (btnGuardar.getText().equals("Dar Alta")) {
            if (valido) {
                servicioJugador.Alta(this); //inicializar jugador con atributos de la interfaz, operaciones de hibernate, mostrar mensaje
            } else {
                servicioJugador.mensajeValidacion();
                //campoNombre.requestFocus();
                return;
            }
        } else {
            if (valido) {
                servicioJugador.Modificacion(this);
            } else {
                servicioJugador.mensajeValidacion();
                campoNombre.requestFocus();
                return;
            }
        }
        limpiar();
        servicioJugador.poblarTabla(tabla);
        deshabilitarEdicion();
        tabla.setDisable(false);
    }

    public void cancelar(Event event) {
        limpiar();
        tabla.getSelectionModel().clearSelection();
        deshabilitarEdicion();
        tabla.setDisable(false);
    }
    //ObservableList<Jugador> datos = FXCollections.observableArrayList(cargarDatos());


    public void limpiar() {
        campoNombre.clear();
        campoApellido.clear();
        campoFechaNac.clear();
        comboPosicion.setValue("Seleccione");
    }

    private void habilitarEdicion() {
        btnGuardar.setDisable(false);
        campoNombre.setDisable(false);
        campoApellido.setDisable(false);
        campoFechaNac.setDisable(false);
        comboPosicion.setDisable(false);
        btnCancelar.setDisable(false);
        campoDni.setDisable(false);
    }

    private void deshabilitarEdicion() {
        campoDni.setDisable(true);
        btnGuardar.setDisable(true);
        campoNombre.setDisable(true);
        campoApellido.setDisable(true);
        campoFechaNac.setDisable(true);
        comboPosicion.setDisable(true);
        btnCancelar.setDisable(true);
    }

    public void tipearCampoNombre(Event event) {
    }

    public void tipearCampoApellido(Event event) {

    }

    public void busqueda(Event event) {


    }
}