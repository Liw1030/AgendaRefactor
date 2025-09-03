package agendaTelefonica.app;

import agendaTelefonica.servicio.Agenda;

import javax.swing.*;

public class MainGui {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Agenda agenda = new Agenda(); // agenda vacía
            InterfazAgenda ventana = new InterfazAgenda(agenda);
            ventana.setVisible(true);
        });
    }
}
