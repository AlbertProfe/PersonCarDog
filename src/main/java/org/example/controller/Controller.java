package org.example.controller;

import org.example.utils.Utils;
import org.example.model.Car;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.service.Service;

import java.util.ArrayList;
import java.util.Scanner;

public class Controller {

    public static void run() {

        System.out.println( "Hello to Transaction Cars Person to Person!" );

        Repository repo = new Repository();

        Scanner scan = new Scanner(System.in);

        while (true) {
            Utils.mainMenu();

            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1":
                    submenuPerson(scan, repo);
                    break;
                case "2":
                    System.out.println("Dog - not implemented yet.");
                    break;
                case "3":
                    submenuCar(scan, repo);
                    break;
                case "4":
                    //Service.buyCar(b, a , 100, repo);
                    break;
                case "5":
                    System.out.println("Car - not implemented yet.");
                    break;
                case "6":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    public static void submenuPerson(Scanner scan, Repository repo) {
        while (true) {
            Utils.personMenu();
            String option =Utils.askMenuOption(scan);
            switch (option) {
                case "1":
                    String namePerson = Utils.askString(scan, "Introduce el nombre de la persona: ");
                    int agePerson = Utils.askInt(scan, "Introduce la edad de la persona: ");
                     Person newPerson = Service.createPerson(namePerson, agePerson, repo);
                     if (newPerson != null) {
                         System.out.println(newPerson);
                     }
                    break;
                case "2":
                    ArrayList<Person> allPeople = Service.getAllPeople(repo);
                    if (allPeople.isEmpty()) {
                        System.out.println("La lista de personas está vacía.");
                        break;
                    }
                    System.out.println("Lista de personas: ");
                    for (Person allPerson : allPeople) {
                        System.out.println(allPerson.toString());
                    }
                    break;
                case "3":
                    String idPerson = Utils.askString(scan, "Introduce el id de la persona: ");
                    Person personById = Service.getPersonById(idPerson, repo);
                    if (personById != null) {
                        System.out.println(personById);
                    }
                    break;
                case "4":
                    String idPersonToUpdate = Utils.askString(scan, "Introduce el id de la persona: ");
                    String newName = Utils.askString(scan, "Introduce el nombre de la persona: ");
                    int newAge = Utils.askInt(scan, "Introduce la edad de la persona: ");
                    boolean success = Service.updatePerson(idPersonToUpdate, newName,  newAge, repo);
                    if (success) {
                        System.out.println("Modificación realizada exitosamente.");
                        Person personUpdated = Service.getPersonById(idPersonToUpdate, repo);
                        System.out.println(personUpdated.toString());
                    }
                    break;
                case "5":
                    String idPersonToDelete = Utils.askString(scan, "Introduce el id de la persona: ");
                    boolean successDelete = Service.deletePerson(idPersonToDelete, repo);
                    if (successDelete) {
                        System.out.println("La persona ha sido eliminda exitosamente.");
                    }
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }

        }
    }


    public static void submenuCar(Scanner scan, Repository repo) {
        while (true) {
            Utils.carMenu();
            String option =Utils.askMenuOption(scan);
            switch (option) {
                case "1":
                    String makeCar = Utils.askString(scan, "Introduce la marca del coche: ");
                    String modelCar = Utils.askString(scan, "Introduce el modelo del coche: ");
                    int yearCar = Utils.askInt(scan, "Introduce el año de matriculación del coche: ");
                    Car newCar = Service.createCar(makeCar, modelCar, yearCar, repo);
                    if (newCar != null) {
                        System.out.println(newCar);
                    }
                    break;
                case "2":
                    ArrayList<Car> allCars = Service.getAllCars(repo);
                    if (allCars.isEmpty()) {
                        System.out.println("La lista de coches está vacía.");
                        break;
                    }
                    System.out.println("Lista de Coches: ");
                    for (Car allCar : allCars) {
                        System.out.println(allCar.toString());
                    }
                    break;
                case "3":
                    String idCar = Utils.askString(scan, "Introduce el id del coche: ");
                    Car CarById = Service.getCarById(idCar, repo);
                    if (CarById != null) {
                        System.out.println(CarById);
                    }
                    break;
                case "4":
                    String idCarToUpdate = Utils.askString(scan, "Introduce el id del coche: ");
                    String newMake = Utils.askString(scan, "Introduce la marca del coche: ");
                    String newModel = Utils.askString(scan, "Introduce el modelo del coche: ");
                    int newYear = Utils.askInt(scan, "Introduce el año de matriculación del coche: ");
                    boolean success = Service.updateCar(idCarToUpdate, newMake,  newModel, newYear, repo);
                    if (success) {
                        System.out.println("Modificación realizada exitosamente.");
                        Car CarUpdated = Service.getCarById(idCarToUpdate, repo);
                        System.out.println(CarUpdated.toString());
                    }
                    break;
                case "5":
                    String idCarToDelete = Utils.askString(scan, "Introduce el id del coche: ");
                    boolean successDelete = Service.deleteCar(idCarToDelete, repo);
                    if (successDelete) {
                        System.out.println("El coche ha sido elimindo exitosamente.");
                    }
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }

        }
    }

}
