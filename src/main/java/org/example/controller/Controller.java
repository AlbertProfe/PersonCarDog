package org.example.controller;


import org.example.model.Car;
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

        System.out.println( "Hello to Transaction Cars Person to Person!" );

        // 1. Instanciamos el repositorio único vacío
        Repository repo = new Repository();

        // 2. Poblamos el repositorio dinámicamente usando Java Faker (Exactamente 10 objetos en total)
        DataSeeder.seedRepository(repo);

        Scanner scan = new Scanner(System.in);

        while (true) {
            Utils.mainMenu();

            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1":
                    //System.out.println("Person - not implemented yet.");
                    System.out.println("\n--- LISTA DE PERSONAS (3 objetos) ---");
                    for (Person p : repo.getAllPeople()) {
                        System.out.println(p + " | Coche asignado: " + (p.getCar() != null ? p.getCar().getMake() : "Ninguno"));
                    }
                    break;
                case "2":
                    //System.out.println("Dog - not implemented yet.");
                    System.out.println("\n--- LISTA DE PERROS (2 objetos) ---");
                    for (Dog d : repo.getAllDogs()) {
                        System.out.println(d);
                    }
                    break;
                case "3":
                    //System.out.println("Car - not implemented yet.");
                    System.out.println("\n--- LISTA DE COCHES (3 objetos) ---");
                    for (Car c : repo.getAllCars()) {
                        System.out.println(c);
                    }
                    break;
                case "4":
                    //Service.buyCar(b, a , 100, repo);

                    // Recuperamos la lista de personas añadidas por el seeder
                    List<Person> people = repo.getAllPeople();

                    if (people.size() >= 2) {
                        // Tomamos a los candidatos preparados por el Seeder
                        Person seller = people.get(0); // Tiene coche
                        Person buyer = people.get(1);  // No tiene coche

                        // Ejecutamos el servicio de compraventa
                        int fakePrice = 15000;
                        Service.buyCar(buyer, seller, fakePrice, repo);
                    } else {
                        System.out.println("No hay suficientes personas en el repositorio.");
                    }

                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
