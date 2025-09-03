# Agenda de Contactos en Java (Consola + GUI)

Este proyecto implementa una **Agenda de Contactos** en Java con dos formas de uso:

1. **Versión de Consola** → Muestra un menú con 9 opciones para gestionar contactos.  
2. **Versión Gráfica (Swing)** → Tabla de contactos y formularios para agregar, buscar, modificar y eliminar.

---

## Características principales

- **Gestión de contactos** con nombre, apellido y teléfono.  
- **Validaciones**:
  - No se permiten duplicados por nombre + apellido.
  - El teléfono no puede repetirse.
  - Se controla si la agenda está llena.  
- **Opciones disponibles**:
  1. Añadir contacto  
  2. Mostrar todos los contactos  
  3. Buscar contacto  
  4. Modificar teléfono  
  5. Eliminar contacto  
  6. Verificar si existe un contacto  
  7. Revisar si la agenda está llena  
  8. Consultar espacios libres  
  9. Salir  

- **Implementación de Excepciones** (`AgendaException`) para manejar errores en la lógica y mostrarlos en la GUI.  
- **Tabla interactiva en Swing** para listar contactos.  
- **Menú superior en GUI** para acceder fácilmente a las funcionalidades.  

---

## Tecnologías usadas
- **Java**  
- **Swing (javax.swing)** para la interfaz gráfica  
- **Maven/Gradle (opcional)** para gestión del proyecto  

---

## Ejecución

### Consola
```bash
javac Main.java
java Main

java AgendaMenuGUI
