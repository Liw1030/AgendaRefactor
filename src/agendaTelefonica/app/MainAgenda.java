package agendaTelefonica.app;

import java.util.Scanner;
import agendaTelefonica.modelo.Contacto;
import agendaTelefonica.servicio.Agenda;
import java.util.InputMismatchException;


public class MainAgenda {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Agenda agenda = new Agenda();

        // Contactos prueba (todos teléfonos diferentes)
        agenda.anadirContacto(new Contacto("Juan", "Pérez", "111111111"));
        agenda.anadirContacto(new Contacto("JUAN", "pérez", "222222222"));
        agenda.anadirContacto(new Contacto("María", "López", "333333333"));
        agenda.anadirContacto(new Contacto("Carlos", "Gómez", "444444444"));
        agenda.anadirContacto(new Contacto("MARÍA", "lópez", "555555555"));
        agenda.anadirContacto(new Contacto("Pedro", "Martínez", "666666666"));
        agenda.anadirContacto(new Contacto("Lucía", "Ramírez", "777777777"));
        agenda.anadirContacto(new Contacto("Andrés", "Torres", "888888888"));
        agenda.anadirContacto(new Contacto("Sofía", "Morales", "999999999"));
        agenda.anadirContacto(new Contacto("Diego", "Castro", "101010101"));

        int opcion = -1;

        do {
            System.out.println("\n===== MENÚ AGENDA =====");
            System.out.println("1. Añadir contacto");
            System.out.println("2. Existe contacto");
            System.out.println("3. Listar contactos");
            System.out.println("4. Buscar contacto");
            System.out.println("5. Eliminar contacto");
            System.out.println("6. Modificar teléfono");
            System.out.println("7. Ver si agenda está llena");
            System.out.println("8. Espacios libres");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Error: Debe ingresar un número válido.");
                scanner.nextLine();
                continue;
            }

            switch (opcion) {
                case 1:
                    try {
                        System.out.print("Nombre: ");
                        String nombre = scanner.nextLine().trim();

                        System.out.print("Apellido: ");
                        String apellido = scanner.nextLine().trim();

                        System.out.print("Teléfono: ");
                        String telefono = scanner.nextLine().trim();

                        Contacto contacto = new Contacto(nombre, apellido, telefono);
                        agenda.anadirContacto(contacto);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error al crear contacto: " + e.getMessage());
                    }
                    break;

                case 2:
                    System.out.print("Nombre: ");
                    String nombreExiste = scanner.nextLine();
                    System.out.print("Apellido: ");
                    String apellidoExiste = scanner.nextLine();
                    System.out.print("Teléfono: ");
                    String telefonoExiste = scanner.nextLine();

                    boolean existe = agenda.existeContacto(nombreExiste, apellidoExiste, telefonoExiste);
                    System.out.println(existe ? "El contacto SÍ existe." : "El contacto NO existe.");
                    break;

                case 3:
                    agenda.listarContactos();
                    break;

                case 4:
                    System.out.println("¿Cómo desea buscar?");
                    System.out.println("1. Por nombre");
                    System.out.println("2. Por apellido");
                    System.out.println("3. Por teléfono");
                    System.out.print("Seleccione una opción: ");

                    int opcionBusqueda;
                    try {
                        opcionBusqueda = Integer.parseInt(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.println("Opción inválida.");
                        break;
                    }

                    if (opcionBusqueda == 1) {
                        System.out.print("Ingrese el nombre: ");
                        String n = scanner.nextLine();
                        agenda.buscarContacto("nombre", n);
                    } else if (opcionBusqueda == 2) {
                        System.out.print("Ingrese el apellido: ");
                        String a = scanner.nextLine();
                        agenda.buscarContacto("apellido", a);
                    } else if (opcionBusqueda == 3) {
                        System.out.print("Ingrese el teléfono: ");
                        String t = scanner.nextLine();
                        agenda.buscarContacto("telefono", t);
                    } else {
                        System.out.println("Opción inválida.");
                    }
                    break;

                case 5:
                    System.out.print("Ingrese el nombre del contacto a eliminar: ");
                    String nombreEliminar = scanner.nextLine().trim();
                    System.out.print("Ingrese el apellido del contacto a eliminar: ");
                    String apellidoEliminar = scanner.nextLine().trim();

                    agenda.eliminarContacto(nombreEliminar, apellidoEliminar);
                    break;

                case 6:
                    System.out.print("Nombre del contacto a modificar: ");
                    String nombreMod = scanner.nextLine().trim();
                    System.out.print("Apellido del contacto a modificar: ");
                    String apellidoMod = scanner.nextLine().trim();
                    System.out.print("Nuevo teléfono (solo dígitos, 7-15): ");
                    String nuevoTel = scanner.nextLine().trim();

                    agenda.modificarTelefono(nombreMod, apellidoMod, nuevoTel);
                    break;

                case 7:
                    if (agenda.agendaLlena()) {
                        System.out.println("La agenda está llena.");
                    } else {
                        System.out.println("La agenda todavía tiene espacios libres: " + agenda.espaciosLibres());
                    }
                    break;

                case 8:
                    System.out.println("Espacios libres: " + agenda.espaciosLibres());
                    break;

                case 0:
                    System.out.println("Saliendo de la agenda...");
                    break;

                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }

        } while (opcion != 0);
    }
}