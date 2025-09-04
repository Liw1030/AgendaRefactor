package agendaTelefonica.app;

import agendaTelefonica.servicio.Agenda;
import javax.swing.*;

/**
 * Clase principal para lanzar la interfaz gráfica de la agenda telefónica.
 * Utiliza Swing para crear una ventana interactiva.
 */
public class MainGui {
    public static void main(String[] args) {
        // Ejecuta el código en el hilo de eventos de Swing (EDT) para evitar problemas de concurrencia
        SwingUtilities.invokeLater(() -> {
            // Se crea una nueva instancia de Agenda (vacía por defecto)
            Agenda agenda = new Agenda();

            // Se crea la ventana principal de la interfaz gráfica, pasando la agenda como dependencia
            InterfazAgenda ventana = new InterfazAgenda(agenda);

            // Se hace visible la ventana
            ventana.setVisible(true);
        });
    }
}