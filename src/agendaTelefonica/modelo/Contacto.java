package agendaTelefonica.modelo;

import java.text.Normalizer;
import java.util.Objects;

public class Contacto {
    private String nombre;
    private String apellido;
    private String telefono;

    // Constructor
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



    // Getters y Setters para leer o modificar los datos :3
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
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

    public void setTelefono(String telefono) {
        if (telefono == null || !telefono.trim().matches("\\d{7,15}")) {
            throw new IllegalArgumentException("Teléfono inválido: debe contener sólo números (7-15 dígitos).");
        }
        this.telefono = telefono.trim();
    }

    /**
     Sobreescribimos equals para que dos contactos sean iguales
     si tienen el mismo nombre y apellido (ignorando mayúsculas/minúsculas).
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
     Siempre que sobrescribimos equals, es recomendable sobrescribir hashCode.
     Así evitamos inconsistencias al usar estructuras como HashSet o HashMap.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                normalizarTexto(nombre),
                normalizarTexto(apellido),
                telefono.trim()
        );
    }

    public static String normalizarTexto(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .trim();
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " - " + telefono;
    }
}