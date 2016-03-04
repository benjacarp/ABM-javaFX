package com.benja.persistencia;

import com.benja.config.HibernateUtil;
import com.benja.modelo.Jugador;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by benjamin.salas on 17/04/2015.
 */
public class JugadorDao {

    public void alta(Jugador jugador){
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();
        try {
            s.save(jugador);
            tx.commit();
            s.close();
            mensaje(jugador, "Jugador Nuevo!");
        } catch (Exception ex) {
            mesajeError(ex, "añadir jugador...");
        }
    }
    public void baja(Jugador jugador){
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();
        try {
            s.delete(jugador);
            tx.commit();
            s.close();
            mensaje(jugador, "Jugador Eliminado!");
        } catch (Exception ex) {
            mesajeError(ex, "eliminar jugador...");
        }
    }
    public void modificacion(Jugador jugador){
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();
        try {
            s.update(jugador);
            tx.commit();
            s.close();
            mensaje(jugador,"Jugador Actualizado!");

        } catch (Exception ex) {
            mesajeError(ex, "modificar jugador...");
        }
    }

    public List<Jugador> findAll() {
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
            mesajeError(ex, "cargar tabla de jugadores");
            return jugadores;
        }
    }

    private void mensaje(Jugador jugador, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(mensaje);
        alert.setHeaderText("Jugador: " +
                jugador.getNombre() + " " +
                jugador.getApellido() + "\n" +
                "Fecha de Nac.: " +
                jugador.getFechaNac() + " " +
                "\nPosición: " +
                jugador.getPosicion());
        alert.setContentText(null);
        alert.showAndWait();
    }
    private void mesajeError(Exception ex, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error al " + mensaje);
        alert.setHeaderText(""+ex);
        alert.setContentText(null);
        alert.showAndWait();
    }
}
