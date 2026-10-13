package org.example.controller;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Person;
import org.example.model.Dog;
import org.example.repository.Repository;
import org.example.service.Service;
import org.example.utils.DataSeeder;
import org.example.utils.Utils;

import java.util.List;
import java.util.Scanner;

public class Controller {

    public static void run() {
        System.out.println("Hello to Transaction Cars Person to Person!");

        // 1. Inicializamos el repositorio (H2 creará o verificará las tablas en disco)
        Repository repo = new Repository();
        Scanner scan = new Scanner(System.in);

        // 2. MODO INTERACTIVO DE INICIALIZACIÓN
        System.out.println("\n=========================================");
        System.out.println("   SYSTEM INITIALIZATION ENVIRONMENT     ");
        System.out.println("=========================================");
        System.out.println("1. Production Mode (Start clean / Load saved disk data)");
        System.out.println("2. Sandbox Mode (Populate database with Fake Demo Data)");
        System.out.print("Select initialization mode (1-2): ");

        String initOption = scan.nextLine();

        if (initOption.equals("2")) {
            // Ejecuta el seeder si el usuario lo solicita explícitamente.
            // Gracias al control que pusimos antes, si ya hay datos, avisará en lugar de duplicar.
            DataSeeder.seedRepository(repo);
        } else {
            System.out.println("ℹ️ Production Mode activated. Loading secure data from disk...");
        }

        while (true) {
            Utils.mainMenu();
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1":
                    // Redirigimos el flujo del programa hacia nuestra nueva gestión de personas
                    managePersonMenu(scan, repo);
                    break;
                case "2":
                    // Redirigimos el flujo hacia nuestra nueva gestión completa de perros
                    manageDogMenu(scan, repo);
                    break;
                case "3":
                    // Redirigimos el flujo hacia nuestra nueva gestión completa de coches
                    manageCarMenu(scan, repo);
                    break;
                case "4":
                    // 1. Solicitamos los datos dinámicamente al usuario por consola
                    String sellerId = Utils.askString(scan, "Enter Seller (Owner) Person ID: ");
                    String buyerId = Utils.askString(scan, "Enter Buyer Person ID: ");
                    int price = Utils.askInt(scan, "Enter car sale price: ");

                    System.out.println("\n--- PROCESSING CAR TRANSACTION ---");

                    // 2. Usamos los métodos READ (getPersonById) que ya programaste en tu Service
                    Person seller = Service.getPersonById(sellerId, repo);
                    Person buyer = Service.getPersonById(buyerId, repo);

                    // 3. Validamos si ambos existen antes de intentar operar con ellos
                    if (seller == null) {
                        System.out.println("Transaction Cancelled: Seller with ID " + sellerId + " does not exist.");
                    } else if (buyer == null) {
                        System.out.println("Transaction Cancelled: Buyer with ID " + buyerId + " does not exist.");
                    } else {
                        // 4. Si ambos existen, ejecutamos tu lógica de negocio "buyCar" de manera 100% dinámica
                        boolean success = Service.buyCar(buyer, seller, price, repo);
                        if (success) {
                            System.out.println("Transaction completed successfully!");
                        } else {
                            System.out.println("Transaction failed. Check business rules constraints.");
                        }
                    }
                    break;
                    /*
                    List<Person> people = repo.getAllPeople();
                    if (people.size() >= 2) {

                        Person seller = people.get(0);
                        Person buyer = people.get(1);
                        int fakePrice = 15000;



                        Service.buyCar(buyer, seller, fakePrice, repo);
                    } else {
                        System.out.println("No hay suficientes personas en el repositorio.");
                    }
                    break;

                         */

                case "5":
                    // Redirigimos el flujo hacia la lectura exclusiva de transacciones
                    manageTransactionMenu(scan, repo);
                    break;
                case "6": // CASO 6: Cierra el programa de forma correcta
                    System.out.println("Goodbye!");
                    return; // Rompe el bucle principal y finaliza la aplicación
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ DE PERSONAS: Conexión directa entre la pantalla y la lógica (Service)
    // =========================================================================
    private static void managePersonMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.personMenu(); // Pintamos el menú de personas en la pantalla
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1": // CASO 1: Crear Persona
                    String name = Utils.askString(scan, "Enter name: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    // Llamamos a la fábrica (Service) para que valide y guarde los datos
                    Service.createPerson(name, age, repo);
                    break;

                case "2": // CASO 2: Listar todas las personas
                    System.out.println("\n--- CURRENT PEOPLE IN SYSTEM ---");
                    // Obtenemos la lista y la recorremos con un bucle for-each (Helsinki Cap. 4)
                    for (Person currentPerson  : Service.getAllPeople(repo)) {
                        System.out.println(currentPerson  + " | Car: " + (currentPerson .getCar() != null ? currentPerson .getCar().getMake() : "None"));
                    }
                    break;

                case "3": // CASO 3: Buscar por ID
                    String searchId = Utils.askString(scan, "Enter Person ID to find: ");
                    Service.getPersonById(searchId, repo);
                    break;

                case "4": // CASO 4: Actualizar datos de una persona
                    String updateId = Utils.askString(scan, "Enter Person ID to update: ");
                    String newName = Utils.askString(scan, "Enter new name: ");
                    int newAge = Utils.askInt(scan, "Enter new age: ");
                    Service.updatePerson(updateId, newName, newAge, repo);
                    break;

                case "5": // CASO 5: Eliminar a una persona
                    String deleteId = Utils.askString(scan, "Enter Person ID to delete: ");
                    Service.deletePerson(deleteId, repo);
                    break;

                case "6": // CASO 6: Asignar Coche a Persona
                    String pId = Utils.askString(scan, "Enter Person ID: ");
                    String cId = Utils.askString(scan, "Enter Car ID to assign: ");
                    Service.assignCarToPerson(pId, cId, repo);
                    break;
                case "7": // CASO 6: Volver atrás. El antiguo caso 6 pasa a ser 7
                    return; // Rompe este bucle y regresa al Menú Principal
                /*
                case "6": // CASO 6: Volver atrás
                    return; // Rompe este bucle y regresa al Menú Principal
                */
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ DE COCHES: Captura de pantalla (Utils) unida a la lógica (Service)
    // =========================================================================
    private static void manageCarMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carMenu(); // Pintamos el menú de coches en la pantalla usando las herramientas de Utils
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1": // Crear Coche
                    String make = Utils.askString(scan, "Enter make: ");
                    String model = Utils.askString(scan, "Enter model: ");
                    int year = Utils.askInt(scan, "Enter year: ");
                    Service.createCar(make, model, year, repo); // Pasamos los datos recolectados al Chef (Símil)
                    break;

                case "2": // Listar todos los coches
                    System.out.println("\n--- CURRENT CARS IN SYSTEM ---");
                    for (Car currentCar : Service.getAllCars(repo)) {
                        System.out.println(currentCar); // Imprime el molde del coche usando su método toString()
                    }
                    break;

                case "3": // Buscar por ID
                    String searchId = Utils.askString(scan, "Enter Car ID to find: ");
                    Service.getCarById(searchId, repo);
                    break;

                case "4": // Actualizar Coche
                    String updateId = Utils.askString(scan, "Enter Car ID to update: ");
                    String newMake = Utils.askString(scan, "Enter new make: ");
                    String newModel = Utils.askString(scan, "Enter new model: ");
                    int newYear = Utils.askInt(scan, "Enter new year: ");
                    Service.updateCar(updateId, newMake, newModel, newYear, repo);
                    break;

                case "5": // Eliminar Coche
                    String deleteId = Utils.askString(scan, "Enter Car ID to delete: ");
                    Service.deleteCar(deleteId, repo);
                    break;

                case "6": // Volver al Menú Principal
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // =========================================================================
    // SUBMENÚ DE PERROS: Captura de pantalla (Utils) unida a la lógica (Service)
    // =========================================================================
    private static void manageDogMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.dogMenu(); // Pintamos el menú de perros en la pantalla usando las herramientas de Utils
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1": // Crear Perro
                    String name = Utils.askString(scan, "Enter dog name: ");
                    String breed = Utils.askString(scan, "Enter breed: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    Service.createDog(name, breed, age, repo); // Enviamos los datos recolectados al Service
                    break;

                case "2": // Listar todos los perros
                    System.out.println("\n--- CURRENT DOGS IN SYSTEM ---");
                    for (Dog currentDog : Service.getAllDogs(repo)) {
                        System.out.println(currentDog); // Muestra los datos del molde usando su método toString()
                    }
                    break;

                case "3": // Buscar por ID
                    String searchId = Utils.askString(scan, "Enter Dog ID to find: ");
                    Service.getDogById(searchId, repo);
                    break;

                case "4": // Actualizar Perro
                    String updateId = Utils.askString(scan, "Enter Dog ID to update: ");
                    String newName = Utils.askString(scan, "Enter new name: ");
                    String newBreed = Utils.askString(scan, "Enter new breed: ");
                    int newAge = Utils.askInt(scan, "Enter new age: ");
                    Service.updateDog(updateId, newName, newBreed, newAge, repo);
                    break;

                case "5": // Eliminar Perro
                    String deleteId = Utils.askString(scan, "Enter Dog ID to delete: ");
                    Service.deleteDog(deleteId, repo);
                    break;

                case "6": // Volver al Menú Principal
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
    // =========================================================================
    // SUBMENÚ DE TRANSACCIONES: Interfaz de Solo Lectura (Read-Only) !!!!!!!!!
    // =========================================================================
    private static void manageTransactionMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carTransactionMenu(); // Pintamos el menú especial de transacciones (Read-Only)
            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1": // Listar todas las transacciones históricas
                    System.out.println("\n--- CAR TRANSACTION HISTORICAL LOGS ---");
                    for (CarTransaction currentTransaction : Service.getAllCarTransactions(repo)) {
                        System.out.println(currentTransaction); // Imprime el registro inalterable generado por el sistema
                    }
                    break;

                case "2": // Buscar una transacción concreta por su identificador único
                    String searchId = Utils.askString(scan, "Enter Transaction ID to find: ");
                    Service.getCarTransactionById(searchId, repo);
                    break;

                case "3": // Volver al Menú Principal
                    return;

                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
