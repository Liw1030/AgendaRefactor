package agendaTelefonica.modelo;

import java.text.Normalizer;
import java.util.Objects;

/**
 * Clase que representa un contacto dentro de la agenda telefónica.
 * Contiene nombre, apellido y número de teléfono, con validaciones integradas.
 */
public class Contacto {
    private String nombre;
    private String apellido;
    private String telefono;

    /**
     * Constructor que inicializa un contacto con nombre, apellido y teléfono.
     * Realiza validaciones para asegurar que los campos no estén vacíos
     * y que el teléfono tenga un formato válido (solo dígitos, entre 7 y 15).
     */
    public Contacto(String nombre, String apellido, String telefono) {
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        if (apellido == null || apellido.trim().isEmpty())
            throw new IllegalArgumentException("El apellido no puede estar vacío.");
        if (telefono == null || !telefono.trim().matches("\\d{7,15}")) {
            throw new IllegalArgumentException("Teléfono inválido: debe contener sólo números (7-15 dígitos).");
        }
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.telefono = telefono.trim();
    }

    // Métodos getter y setter para acceder y modificar los atributos del contacto

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        // Si el nombre es nulo, se asigna una cadena vacía para evitar errores
        this.nombre = nombre == null ? "" : nombre.trim();
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido == null ? "" : apellido.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    /**
     * Setter que valida el nuevo número antes de asignarlo.
     * Solo se aceptan números entre 7 y 15 dígitos.
     */
    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.trim().matches("\\d{7,15}")) {
            throw new IllegalArgumentException("Teléfono inválido: debe contener sólo números (7-15 dígitos).");
        }
        this.telefono = telefono.trim();
    }

    /**
     * Sobrescribe el método equals para comparar contactos.
     * Dos contactos se consideran iguales si tienen el mismo nombre, apellido y teléfono,
     * ignorando acentos, mayúsculas y espacios.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Contacto)) return false;
        Contacto contacto = (Contacto) o;
        return normalizarTexto(nombre).equals(normalizarTexto(contacto.nombre)) &&
                normalizarTexto(apellido).equals(normalizarTexto(contacto.apellido)) &&
                telefono.trim().equals(contacto.telefono.trim());
    }

    /**
     * Sobrescribe hashCode para mantener coherencia con equals.
     * Esto es esencial para que los contactos funcionen correctamente en colecciones como HashSet o HashMap.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                normalizarTexto(nombre),
                normalizarTexto(apellido),
                telefono.trim()
        );
    }

    /**
     * Método utilitario que normaliza texto eliminando acentos,
     * convirtiendo a minúsculas y eliminando espacios.
     * Se usa para comparar nombres y apellidos de forma robusta.
     */
    public static String normalizarTexto(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "") // Elimina marcas diacríticas (acentos)
                .toLowerCase()
                .trim();
    }

    /**
     * Representación en texto del contacto, útil para mostrar en consola o interfaz.
     */
    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + telefono;
    }
}