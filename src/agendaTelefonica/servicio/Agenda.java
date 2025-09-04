package agendaTelefonica.servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import agendaTelefonica.modelo.Contacto;

public class Agenda {
    // Lista que almacena los contactos registrados
    private List<Contacto> contactos;

    // Capacidad máxima de la agenda
    private int maxSize;

    // Tamaño por defecto si no se especifica
    private static final int DEFAULT_SIZE = 10;

    // Constructor que inicializa la agenda con tamaño por defecto
    public Agenda() {
        this.contactos = new ArrayList<>();
        this.maxSize = DEFAULT_SIZE;
    }

    // Constructor que permite definir el tamaño máximo de la agenda
    public Agenda(int maxSize) {
        this.contactos = new ArrayList<>();
        this.maxSize = maxSize;
    }

    /**
     * Añade un nuevo contacto a la agenda si no hay duplicados y hay espacio disponible.
     * Retorna un mensaje indicando el resultado de la operación.
     */
    public String anadirContacto(Contacto c) {
        if (c == null) return "No se puede añadir: el contacto es nulo.";

        if (espaciosLibres() == 0) return "No se puede añadir el contacto: la agenda está llena.";

        // Verifica duplicados por nombre+apellido o por número de teléfono
        for (Contacto existente : contactos) {
            boolean mismoNombreApellido = Contacto.normalizarTexto(existente.getNombre())
                    .equals(Contacto.normalizarTexto(c.getNombre())) &&
                    Contacto.normalizarTexto(existente.getApellido())
                            .equals(Contacto.normalizarTexto(c.getApellido()));

            boolean mismoTelefono = existente.getTelefono().equals(c.getTelefono());

            if (mismoNombreApellido) return "Ya existe un contacto con el mismo nombre y apellido.";
            if (mismoTelefono) return "Ya existe un contacto con el mismo número de teléfono.";
        }

        contactos.add(c);
        return "OK";
    }

    /**
     * Verifica si existe un contacto con nombre, apellido y teléfono exactos.
     */
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

    /**
     * Muestra todos los contactos ordenados alfabéticamente por nombre y apellido.
     */
    public void listarContactos() {
        if (contactos.isEmpty()) {
            System.out.println("La agenda no tiene contactos.");
            return;
        }

        // Ordena los contactos por nombre y luego por apellido, ignorando mayúsculas
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

    /**
     * Busca contactos por nombre, apellido o teléfono.
     * Muestra los resultados en consola.
     */
    public void buscarContacto(String campo, String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            System.out.println("Debe ingresar un valor para buscar.");
            return;
        }

        // Normaliza el valor según el campo
        String vNorm = "telefono".equalsIgnoreCase(campo)
                ? valor.trim()
                : Contacto.normalizarTexto(valor);

        List<Contacto> resultados = new ArrayList<>();

        // Filtra contactos según el campo especificado
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

        // Muestra resultados o mensaje si no se encontró nada
        if (resultados.isEmpty()) {
            System.out.println("No se encontraron contactos para ese criterio.");
        } else {
            System.out.println("Resultados:");
            for (Contacto r : resultados) {
                System.out.println(" - " + r);
            }
        }
    }

    /**
     * Elimina un contacto que coincida con el nombre y apellido proporcionados.
     */
    public void eliminarContacto(String nombre, String apellido) {
        String nombreLimpio = Contacto.normalizarTexto(nombre);
        String apellidoLimpio = Contacto.normalizarTexto(apellido);

        // Se crea una copia para evitar errores al modificar la lista mientras se recorre
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

    /**
     * Modifica el número de teléfono de un contacto si no está duplicado y es válido.
     */
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

                // Validación básica del formato del teléfono
                if (nuevoTelefono == null || !nuevoTelefono.trim().matches("\\d{7,15}")) {
                    System.out.println("Teléfono inválido: debe contener sólo números (7-15 dígitos).");
                    return;
                }

                // Verifica que el nuevo número no esté asignado a otro contacto
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

    /**
     * Indica si la agenda está llena.
     */
    public boolean agendaLlena() {
        return espaciosLibres() == 0;
    }

    /**
     * Devuelve la cantidad de espacios disponibles en la agenda.
     */
    public int espaciosLibres() {
        return maxSize - contactos.size();
    }

    /**
     * Devuelve una copia de la lista de contactos para evitar modificaciones externas.
     */
    public List<Contacto> obtenerContactos() {
        return new ArrayList<>(this.contactos);
    }
}