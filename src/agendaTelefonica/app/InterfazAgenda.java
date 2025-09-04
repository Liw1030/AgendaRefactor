package agendaTelefonica.app;

import agendaTelefonica.modelo.Contacto;
import agendaTelefonica.servicio.Agenda;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Clase principal de la interfaz gráfica para la agenda telefónica.
 * Permite añadir, eliminar, modificar, buscar y listar contactos usando Swing.
 * Ahora se usa JTable para mostrar contactos en columnas: Nombre, Apellido, Teléfono.
 */
public class InterfazAgenda extends JFrame {

    /** La agenda que maneja los contactos */
    private final Agenda agenda;

    /** Campos de texto para ingresar nombre, apellido y teléfono */
    private final JTextField tfNombre = new JTextField(10),
            tfApellido = new JTextField(10),
            tfTelefono = new JTextField(8);

    /** Columnas de la tabla */
    private final String[] columnas = {"Nombre", "Apellido", "Teléfono"};

    /** Modelo dinámico para la tabla de contactos */
    private final DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);

    /** Tabla para mostrar los contactos en columnas */
    private final JTable tabla = new JTable(modeloTabla);

    /**
     * Clase interna que representa un botón con esquinas redondeadas.
     * Mejora la estética de la interfaz.
     */
    private static class RoundedButton extends JButton {
        private final int arc = 20; // radio de las esquinas

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
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Constructor de la interfaz de la agenda.
     *
     * @param agenda instancia de Agenda que maneja los contactos
     */
    public InterfazAgenda(Agenda agenda) {
        super("Agenda simple");
        this.agenda = agenda;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(6, 6));

        // ---- PANEL NORTE: campos de texto y botones
        JPanel northPanel = new JPanel(new BorderLayout(4, 4));

        JPanel camposPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        camposPanel.add(new JLabel("Nombre:"));
        camposPanel.add(tfNombre);
        camposPanel.add(new JLabel("Apellido:"));
        camposPanel.add(tfApellido);
        camposPanel.add(new JLabel("Tel:"));
        camposPanel.add(tfTelefono);


        JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        RoundedButton bAdd = new RoundedButton("Añadir");
        RoundedButton bDel = new RoundedButton("Eliminar");
        botonesPanel.add(bAdd);
        botonesPanel.add(bDel);

        northPanel.add(camposPanel, BorderLayout.NORTH);
        northPanel.add(botonesPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        // ---- PANEL CENTRAL: JTable para contactos ----
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // Listener para sincronizar selección de fila con campos de texto
        tabla.getSelectionModel().addListSelectionListener(ev -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                tfNombre.setText(tabla.getValueAt(fila, 0).toString());
                tfApellido.setText(tabla.getValueAt(fila, 1).toString());
                tfTelefono.setText(tabla.getValueAt(fila, 2).toString());
            }
        });

        // ---- PANEL SUR: botones auxiliares ----
        JPanel bot = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        RoundedButton bExiste = new RoundedButton("Existe Contacto?");
        RoundedButton bEsp = new RoundedButton("Cupos Libres");
        RoundedButton bMod = new RoundedButton("Modificar Tel");
        RoundedButton bBuscar = new RoundedButton("Buscar");
        RoundedButton bLimpiar = new RoundedButton("Limpiar");
        RoundedButton bDemo = new RoundedButton("Demo");

        bot.add(bExiste); bot.add(bEsp);bot.add(bMod);
        bot.add(bBuscar);bot.add(bLimpiar);bot.add(bDemo);
        add(bot, BorderLayout.SOUTH);

        // ---- ACCIONES DE LOS BOTONES ----

        // Añadir contacto
        bAdd.addActionListener(e -> {
            String n = tfNombre.getText().trim(),
                    a = tfApellido.getText().trim(),
                    t = tfTelefono.getText().trim();

            if (n.isEmpty() || a.isEmpty() || t.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete los campos.");
                return;
            }

            String resultado = agenda.anadirContacto(new Contacto(n, a, t));
            if (!resultado.equals("OK")) {
                JOptionPane.showMessageDialog(this, resultado);
                return;
            }

            // Mostrar alerta de contacto agregado
            JOptionPane.showMessageDialog(this, "Contacto agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            actualizar(); // actualiza la tabla
            limpiar();    // limpia los campos de texto
        });


        // Eliminar contacto
        bDel.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                String n = tabla.getValueAt(fila, 0).toString();
                String a = tabla.getValueAt(fila, 1).toString();

                String[] opciones = {"Sí", "No"}; // textos personalizados
                int opcion = JOptionPane.showOptionDialog(this,
                        "¿Está seguro de eliminar el contacto: " + n + " " + a + "?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        opciones,
                        opciones[1]); // opción por defecto "No"

                if (opcion == 0) { // 0 = "Sí"
                    agenda.eliminarContacto(n, a);
                    actualizar();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un contacto para eliminar.");
            }
        });


        // Modificar teléfono
        bMod.addActionListener(e -> {
            String n = tfNombre.getText().trim();
            String a = tfApellido.getText().trim();
            if (n.isEmpty() || a.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete nombre y apellido del contacto a modificar.");
                return;
            }
            Contacto sel = null;
            for (Contacto c : agenda.obtenerContactos()) {
                if (Contacto.normalizarTexto(c.getNombre()).equals(Contacto.normalizarTexto(n)) &&
                        Contacto.normalizarTexto(c.getApellido()).equals(Contacto.normalizarTexto(a))) {
                    sel = c;
                    break;
                }
            }
            if (sel == null) { JOptionPane.showMessageDialog(this, "Contacto no encontrado."); return; }

            String nuevoTel = JOptionPane.showInputDialog(this,
                    "Ingrese el nuevo teléfono para " + n + " " + a + ":", sel.getTelefono());
            if (nuevoTel == null || nuevoTel.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Modificación cancelada o teléfono vacío."); return;
            }
            agenda.modificarTelefono(n, a, nuevoTel.trim());
            actualizar();
        });

        // Existe?
        bExiste.addActionListener(e -> {
            String n = tfNombre.getText().trim(), a = tfApellido.getText().trim(), t = tfTelefono.getText().trim();
            if (n.isEmpty() || a.isEmpty() || t.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nombre, apellido y teléfono requeridos."); return;
            }
            boolean existe = agenda.existeContacto(n, a, t);
            JOptionPane.showMessageDialog(this, existe ? "Ya existe" : "No existe");
        });

        // Espacios libres
        bEsp.addActionListener(e -> JOptionPane.showMessageDialog(this, "Espacios libres: " + agenda.espaciosLibres()));

        // Demo de contactos
        bDemo.addActionListener(e -> {
            String[][] d = {{"Juan", "Pérez", "3001234567"}, {"María", "López", "3159876543"}, {"Ana", "Santos", "3012223344"}};
            for (String[] x : d) {
                if (!agenda.existeContacto(x[0], x[1], x[2]) && !agenda.agendaLlena()) {
                    agenda.anadirContacto(new Contacto(x[0], x[1], x[2]));
                }
            }
            actualizar();
        });

        // Limpiar campos
        bLimpiar.addActionListener(e -> limpiar());

        // ---- ESTÉTICA DE BOTONES ----
        bAdd.setBackground(new Color(46, 204, 113));
        bAdd.setForeground(Color.WHITE);
        bDel.setBackground(new Color(231, 76, 60));
        bDel.setForeground(Color.WHITE); Color azul = new Color(52, 152, 219);
        bMod.setBackground(azul); bMod.setForeground(Color.WHITE);
        bBuscar.setBackground(azul); bBuscar.setForeground(Color.WHITE);
        bExiste.setBackground(azul); bExiste.setForeground(Color.WHITE);
        bEsp.setBackground(azul); bEsp.setForeground(Color.WHITE);
        bDemo.setBackground(azul); bDemo.setForeground(Color.WHITE);
        bLimpiar.setBackground(azul); bLimpiar.setForeground(Color.WHITE);

        Font f = new Font("Segoe UI", Font.BOLD, 13);
        bAdd.setFont(f);
        bDel.setFont(f);
        bMod.setFont(f);
        bBuscar.setFont(f);
        bLimpiar.setFont(f);

        // Buscar contactos
        bBuscar.addActionListener(e -> {
            String[] opciones = {"Nombre", "Apellido", "Teléfono"};
            int sel = JOptionPane.showOptionDialog(this, "Seleccione el campo para buscar:", "Buscar",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
            if (sel < 0) return;
            String campo = sel == 0 ? "nombre" : sel == 1 ? "apellido" : "telefono";
            String valor = JOptionPane.showInputDialog(this, "Ingrese " + opciones[sel] + ":");
            if (valor == null || valor.trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Valor vacío. Cancelado."); return; }

            List<Contacto> encontrados = new java.util.ArrayList<>();
            String vNorm = campo.equals("telefono") ? valor.trim() : Contacto.normalizarTexto(valor);
            for (Contacto c : agenda.obtenerContactos()) {
                if ("nombre".equals(campo) && Contacto.normalizarTexto(c.getNombre()).equals(vNorm)) encontrados.add(c);
                else if ("apellido".equals(campo) && Contacto.normalizarTexto(c.getApellido()).equals(vNorm)) encontrados.add(c);
                else if ("telefono".equals(campo) && c.getTelefono().trim().equals(vNorm)) encontrados.add(c);
            }

            if (encontrados.isEmpty()) JOptionPane.showMessageDialog(this, "No se encontraron contactos para: " + valor);
            else {
                StringBuilder sb = new StringBuilder();
                for (Contacto r : encontrados) sb.append(r).append("\n");
                JOptionPane.showMessageDialog(this, sb.toString(), "Resultados", JOptionPane.INFORMATION_MESSAGE);
                actualizar();
            }
        });

        actualizar(); // mostrar contactos iniciales
    }

    /** Actualiza la tabla de contactos en JTable */
    private void actualizar() {
        modeloTabla.setRowCount(0);
        List<Contacto> list = agenda.obtenerContactos();
        list.sort((a, b) -> {
            int c = a.getNombre().compareToIgnoreCase(b.getNombre());
            return c != 0 ? c : a.getApellido().compareToIgnoreCase(b.getApellido());
        });
        for (Contacto c : list) modeloTabla.addRow(new Object[]{c.getNombre(), c.getApellido(), c.getTelefono()});
    }

    /** Limpia los campos de texto */
    private void limpiar() {
        tfNombre.setText("");
        tfApellido.setText("");
        tfTelefono.setText("");
    }

    /** Método main para iniciar la aplicación */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Agenda ag = new Agenda();
            InterfazAgenda v = new InterfazAgenda(ag);
            v.setVisible(true);
        });
    }
}
