package agendaTelefonica.app;

import agendaTelefonica.modelo.Contacto;
import agendaTelefonica.servicio.Agenda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class InterfazAgenda extends JFrame {
    private final Agenda agenda;
    private final DefaultListModel<Contacto> modelo = new DefaultListModel<>();
    private final JList<Contacto> lista = new JList<>(modelo);
    private final JTextField tfNombre = new JTextField(10), tfApellido = new JTextField(10),
            tfTelefono = new JTextField(8), tfNuevo = new JTextField(8);

    private static class RoundedButton extends JButton {
        private final int arc = 20;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(6, 12, 6, 12));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.setColor(getBackground().darker());
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public InterfazAgenda(Agenda agenda) {
        super("Agenda simple");
        this.agenda = agenda;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(540, 360);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(6,6));

        // NORTH: campos + botones
        JPanel northPanel = new JPanel(new BorderLayout(4,4));

        JPanel camposPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 6));
        camposPanel.add(new JLabel("Nombre:")); camposPanel.add(tfNombre);
        camposPanel.add(new JLabel("Apellido:")); camposPanel.add(tfApellido);
        camposPanel.add(new JLabel("Tel:")); camposPanel.add(tfTelefono);

        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        RoundedButton bAdd = new RoundedButton("Añadir");
        RoundedButton bDel = new RoundedButton("Eliminar");
        RoundedButton bMod = new RoundedButton("Modificar");
        RoundedButton bBuscar = new RoundedButton("Buscar");
        botonesPanel.add(bAdd); botonesPanel.add(bDel); botonesPanel.add(bMod); botonesPanel.add(bBuscar);

        northPanel.add(camposPanel, BorderLayout.NORTH);
        northPanel.add(botonesPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // CENTER: lista de contactos
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(lista), BorderLayout.CENTER);

        // SOUTH: acciones auxiliares centradas
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        bot.add(new JLabel("Nuevo tel:"));
        bot.add(tfNuevo);

        RoundedButton bExiste = new RoundedButton("Existe?");
        RoundedButton bEsp = new RoundedButton("Espacios");
        RoundedButton bDemo = new RoundedButton("Demo");
        RoundedButton bLimpiar = new RoundedButton("Limpiar");

        bot.add(bExiste);
        bot.add(bEsp);
        bot.add(bDemo);
        bot.add(bLimpiar);
        add(bot, BorderLayout.SOUTH);

        // ---- Acciones ----
        bAdd.addActionListener(e -> {
            String n = tfNombre.getText().trim(), a = tfApellido.getText().trim(), t = tfTelefono.getText().trim();
            if (n.isEmpty() || a.isEmpty() || t.isEmpty()) { JOptionPane.showMessageDialog(this, "Complete los campos."); return; }
            if (agenda.agendaLlena()) { JOptionPane.showMessageDialog(this, "Agenda llena."); return; }
//            if (agenda.existeContacto(n, a, t)) { JOptionPane.showMessageDialog(this, "Ya existe el contacto con ese teléfono."); return; }
            try { agenda.anadirContacto(new Contacto(n, a, t)); actualizar(); limpiar(); }
            catch (IllegalArgumentException ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); }
        });

        bDel.addActionListener(e -> {
            Contacto sel = lista.getSelectedValue();
            String n, a;

            if (sel != null) {
                n = sel.getNombre();
                a = sel.getApellido();
            } else {
                n = tfNombre.getText().trim();
                a = tfApellido.getText().trim();
                if (n.isEmpty() || a.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Seleccione un contacto o ingrese nombre+apellido.");
                    return;
                }
            }

            // Confirmación antes de eliminar
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Está seguro de eliminar el contacto: " + n + " " + a + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (opcion == JOptionPane.YES_OPTION) {
                agenda.eliminarContacto(n, a);
                actualizar();
            }
        });

        bMod.addActionListener(e -> {
            String n = tfNombre.getText().trim(), a = tfApellido.getText().trim(), t = tfNuevo.getText().trim();
            if (n.isEmpty() || a.isEmpty() || t.isEmpty()) { JOptionPane.showMessageDialog(this, "Complete nombre, apellido y nuevo teléfono."); return; }
            agenda.modificarTelefono(n, a, t);
            actualizar();
        });

        bExiste.addActionListener(e -> {
            String n = tfNombre.getText().trim();
            String a = tfApellido.getText().trim();
            String t = tfTelefono.getText().trim(); // <-- agregamos teléfono
            if (n.isEmpty() || a.isEmpty() || t.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre, apellido y teléfono requeridos.");
                return;
            }

            boolean existe = agenda.existeContacto(n, a, t); // <-- usamos tu método
            JOptionPane.showMessageDialog(this, existe ? "Ya existe" : "No existe");
        });


        bEsp.addActionListener(e -> JOptionPane.showMessageDialog(this, "Espacios libres: " + agenda.espaciosLibres()));

        bDemo.addActionListener(e -> {
            String[][] d = {{"Juan","Pérez","3001234567"},{"María","López","3159876543"},{"Ana","Santos","3012223344"}};
            for (String[] x : d) if (!agenda.existeContacto(x[0], x[1], x[2]) && !agenda.agendaLlena()) try { agenda.anadirContacto(new Contacto(x[0], x[1], x[2])); } catch(Exception ignored){}
            actualizar();
        });

        bLimpiar.addActionListener(e -> limpiar());

        // ---- Estética ----
        bAdd.setBackground(new Color(46, 204, 113)); bAdd.setForeground(Color.WHITE);
        bDel.setBackground(new Color(231, 76, 60)); bDel.setForeground(Color.WHITE);

        Color azul = new Color(52, 152, 219);
        bMod.setBackground(azul); bMod.setForeground(Color.WHITE);
        bBuscar.setBackground(azul); bBuscar.setForeground(Color.WHITE);
        bExiste.setBackground(azul); bExiste.setForeground(Color.WHITE);
        bEsp.setBackground(azul); bEsp.setForeground(Color.WHITE);
        bDemo.setBackground(azul); bDemo.setForeground(Color.WHITE);
        bLimpiar.setBackground(azul); bLimpiar.setForeground(Color.WHITE);

        Font f = new Font("Segoe UI", Font.BOLD, 13);
        bAdd.setFont(f); bDel.setFont(f); bMod.setFont(f); bBuscar.setFont(f); bLimpiar.setFont(f);

        // ---- Buscar ----
        bBuscar.addActionListener(e -> {
            String[] opciones = {"Nombre", "Apellido", "Teléfono"};
            int sel = JOptionPane.showOptionDialog(this,
                    "Seleccione el campo para buscar:",
                    "Buscar",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);
            if (sel == JOptionPane.CLOSED_OPTION || sel < 0) return;
            String campo = sel == 0 ? "nombre" : sel == 1 ? "apellido" : "telefono";
            String valor = JOptionPane.showInputDialog(this, "Ingrese " + opciones[sel] + ":");
            if (valor == null || valor.trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Valor vacío. Cancelado."); return; }

            agenda.buscarContacto(campo, valor);
            java.util.List<Contacto> encontrados = new java.util.ArrayList<>();
            String vNorm = "telefono".equalsIgnoreCase(campo) ? valor.trim() : Contacto.normalizarTexto(valor);
            for (Contacto c : agenda.obtenerContactos()) {
                if ("nombre".equalsIgnoreCase(campo) && Contacto.normalizarTexto(c.getNombre()).equals(vNorm)) encontrados.add(c);
                else if ("apellido".equalsIgnoreCase(campo) && Contacto.normalizarTexto(c.getApellido()).equals(vNorm)) encontrados.add(c);
                else if ("telefono".equalsIgnoreCase(campo) && c.getTelefono().trim().equals(vNorm)) encontrados.add(c);
            }

            if (encontrados.isEmpty()) JOptionPane.showMessageDialog(this, "No se encontraron contactos para: " + valor);
            else {
                StringBuilder sb = new StringBuilder();
                for (Contacto r : encontrados) sb.append(r).append("\n");
                JOptionPane.showMessageDialog(this, sb.toString(), "Resultados", JOptionPane.INFORMATION_MESSAGE);
                Contacto primero = encontrados.get(0);
                lista.setSelectedValue(primero, true);
                tfNombre.setText(primero.getNombre());
                tfApellido.setText(primero.getApellido());
                tfTelefono.setText(primero.getTelefono());
            }
        });

        lista.addListSelectionListener(ev -> {
            Contacto s = lista.getSelectedValue();
            if (s != null) { tfNombre.setText(s.getNombre()); tfApellido.setText(s.getApellido()); tfTelefono.setText(s.getTelefono()); }
        });

        actualizar();
    }

    private void actualizar() {
        modelo.clear();
        List<Contacto> list = agenda.obtenerContactos();
        list.sort((a,b)-> { int c = a.getNombre().compareToIgnoreCase(b.getNombre()); return c!=0?c:a.getApellido().compareToIgnoreCase(b.getApellido()); });
        for (Contacto c : list) modelo.addElement(c);
    }

    private void limpiar() {
        tfNombre.setText("");
        tfApellido.setText("");
        tfTelefono.setText("");
        tfNuevo.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Agenda ag = new Agenda();
            InterfazAgenda v = new InterfazAgenda(ag);
            v.setVisible(true);
        });
    }
}
