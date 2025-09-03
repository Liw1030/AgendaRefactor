package agendaTelefonica.servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import agendaTelefonica.modelo.Contacto;

public class Agenda {
    private List<Contacto> contactos;
    private int maxSize;
    private static final int DEFAULT_SIZE = 10;

    // Constructor con tamaño por defecto
    public Agenda() {
        this.contactos = new ArrayList<>();
        this.maxSize = DEFAULT_SIZE;
    }

    // Constructor con tamaño definido
    public Agenda(int maxSize) {
        this.contactos = new ArrayList<>();
        this.maxSize = maxSize;
    }

    // Métodos a implementar

    public void anadirContacto(Contacto c) {
        if (c == null) {
            System.out.println("No se puede añadir: el contacto es nulo.");
            return;
        }

        // 1. Verificar si la agenda está llena
        if (espaciosLibres() == 0) {
            System.out.println("No se puede añadir el contacto: la agenda está llena.");
            return;
        }

        // 2. Validar duplicados (nombre+apellido o teléfono repetido)
        for (Contacto existente : contactos) {
            boolean mismoNombreApellido = Contacto.normalizarTexto(existente.getNombre())
                    .equals(Contacto.normalizarTexto(c.getNombre())) &&
                    Contacto.normalizarTexto(existente.getApellido())
                            .equals(Contacto.normalizarTexto(c.getApellido()));

            boolean mismoTelefono = existente.getTelefono().equals(c.getTelefono());

            if (mismoNombreApellido) {
                System.out.println("No se puede añadir: ya existe un contacto con el mismo nombre y apellido.");
                return;
            }

            if (mismoTelefono) {
                System.out.println("No se puede añadir: ya existe un contacto con el mismo número de teléfono.");
                return;
            }
        }

        // 3. Si todo bien → añadir
        contactos.add(c);
        System.out.println("Contacto añadido correctamente.");
    }

    public boolean existeContacto(String nombre, String apellido, String telefono) {
        if (nombre == null || apellido == null || telefono == null) return false;

        String n = Contacto.normalizarTexto(nombre);
        String a = Contacto.normalizarTexto(apellido);
        String t = telefono.trim();

        for (Contacto c : contactos) {
            if (Contacto.normalizarTexto(c.getNombre()).equals(n) &&
                    Contacto.normalizarTexto(c.getApellido()).equals(a) &&
                    c.getTelefono().trim().equals(t)) {
                return true;
            }
        }
        return false;
    }

    public void listarContactos() {
        if (contactos.isEmpty()) {
            System.out.println("La agenda no tiene contactos.");
            return;
        }

        contactos.sort(
                Comparator.comparing((Contacto c) -> c.getNombre().trim(), String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(c -> c.getApellido().trim(), String.CASE_INSENSITIVE_ORDER)
        );

        int i = 1;
        for (Contacto c : contactos) {
            System.out.println("Contacto " + i + ": Nombre: "
                    + c.getNombre() + " | Apellido: " + c.getApellido()
                    + " | Teléfono: " + c.getTelefono());
            i++;
        }
    }

    public void buscarContacto(String campo, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            System.out.println("Debe ingresar un valor para buscar.");
            return;
        }

        String vNorm = "telefono".equalsIgnoreCase(campo)
                ? valor.trim()
                : Contacto.normalizarTexto(valor);

        List<Contacto> resultados = new ArrayList<>();

        for (Contacto c : contactos) {
            if ("nombre".equalsIgnoreCase(campo)) {
                if (Contacto.normalizarTexto(c.getNombre()).equals(vNorm)) resultados.add(c);
            } else if ("apellido".equalsIgnoreCase(campo)) {
                if (Contacto.normalizarTexto(c.getApellido()).equals(vNorm)) resultados.add(c);
            } else if ("telefono".equalsIgnoreCase(campo)) {
                if (c.getTelefono().trim().equals(vNorm)) resultados.add(c);
            } else {
                System.out.println("Campo de búsqueda inválido. Use: nombre | apellido | telefono");
                return;
            }
        }

        if (resultados.isEmpty()) {
            System.out.println("No se encontraron contactos para ese criterio.");
        } else {
            System.out.println("Resultados:");
            for (Contacto r : resultados) {
                System.out.println(" - " + r);
            }
        }
    }

    public void eliminarContacto(String nombre, String apellido) {
        String nombreLimpio = Contacto.normalizarTexto(nombre);
        String apellidoLimpio = Contacto.normalizarTexto(apellido);

        for (Contacto actual : new ArrayList<>(contactos)) {
            if (Contacto.normalizarTexto(actual.getNombre()).equals(nombreLimpio) &&
                    Contacto.normalizarTexto(actual.getApellido()).equals(apellidoLimpio)) {
                contactos.remove(actual);
                System.out.println("El contacto fue eliminado con éxito.");
                return;
            }
        }
        System.out.println("El contacto no existe en la agenda.");
    }

    public void modificarTelefono(String nombre, String apellido, String nuevoTelefono) {
        if (nombre == null || apellido == null) {
            System.out.println("Nombre y apellido requeridos para modificar teléfono.");
            return;
        }

        String nombreLimpio = Contacto.normalizarTexto(nombre);
        String apellidoLimpio = Contacto.normalizarTexto(apellido);

        for (Contacto actual : contactos) {
            String nombreActual = Contacto.normalizarTexto(actual.getNombre());
            String apellidoActual = Contacto.normalizarTexto(actual.getApellido());

            if (nombreActual.equals(nombreLimpio) && apellidoActual.equals(apellidoLimpio)) {

                if (nuevoTelefono == null || !nuevoTelefono.trim().matches("\\d{7,15}")) {
                    System.out.println("Teléfono inválido: debe contener sólo números (7-15 dígitos).");
                    return;
                }

                // Verificar que no se repita el teléfono
                for (Contacto existente : contactos) {
                    if (existente != actual && existente.getTelefono().equals(nuevoTelefono.trim())) {
                        System.out.println("Ese número ya está asignado a otro contacto.");
                        return;
                    }
                }

                actual.setTelefono(nuevoTelefono.trim());
                System.out.println("Teléfono actualizado. Nuevo contacto: " + actual);
                return;
            }
        }
        System.out.println("No se encontró el contacto para modificar.");
    }

    public boolean agendaLlena() {
        return espaciosLibres() == 0;
    }

    public int espaciosLibres() {
        return maxSize - contactos.size();
    }

    public java.util.List<Contacto> obtenerContactos() {
        return new java.util.ArrayList<>(this.contactos);
    }
}