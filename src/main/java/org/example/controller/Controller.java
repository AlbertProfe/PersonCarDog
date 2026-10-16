package org.example.controller;

import org.example.model.Car;
import org.example.model.Dog;
import org.example.model.Person;

import org.example.persistence.JsonStore;
import org.example.repository.Repository;
import org.example.service.Service;
import org.example.utils.DataSeeder;
import org.example.utils.ResultOperation;
import org.example.utils.Utils;

import java.util.Scanner;

public class Controller {

    public static void run() {

        System.out.println("Hello to Transaction Cars Person to Person!");

        // 1. We create the repo object because we need it in the controller
        //    and we need it in the service, so we create it here
        Repository repo = new Repository();

        // 2. If a saved JSON file exists we load it (repo survives restarts),
        //    otherwise we populate the repo with dummy data using DataSeeder and Java Faker
        if (org.example.persistence.JsonStore.exists(org.example.persistence.JsonStore.DEFAULT_PATH)) {
            Service.loadRepository(org.example.persistence.JsonStore.DEFAULT_PATH, repo);
        } else {
            DataSeeder.seedRepository(repo);
        }
        Scanner scan = new Scanner(System.in);

        while (true) {
            Utils.mainMenu();

            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1":
                    personSubMenu(scan, repo);
                    break;
                case "2":
                    dogSubMenu(scan, repo);
                    break;
                case "3":
                    carSubMenu(scan, repo);
                    break;
                case "4":
                    // buyCar uses current dummy data for demo; enhance later if needed
                    runBuyCarDemo(scan, repo);
                    break;
                case "5":
                    carTransactionSubMenu(scan, repo);
                    break;
                case "6":
                    runAdoptDog(scan, repo);
                    break;
                case "7":
                    runReturnDog(scan, repo);
                    break;
                case "8":
                    dogAdoptionSubMenu(scan, repo);
                    break;
                case "9":
                    Service.saveRepository(org.example.persistence.JsonStore.DEFAULT_PATH, repo);
                    break;
                case "10":
                    Service.loadRepository(org.example.persistence.JsonStore.DEFAULT_PATH, repo);
                    break;
                case "11":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

    // ===== PERSON SUBMENU =====
    private static void personSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.personMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    String name = Utils.askString(scan, "Enter name: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    ResultOperation resCreatePerson = Service.createPerson(name, age, repo);
                    System.out.println("resultOperation: " + resCreatePerson);
                    break;
                case "2":
                    ResultOperation resAllPeople = Service.getAllPeople(repo);
                    System.out.println("resultOperation: " + resAllPeople);
                    break;
                case "3":
                    String pid = Utils.askString(scan, "Enter person ID: ");
                    ResultOperation resGetPerson = Service.getPersonById(pid, repo);
                    System.out.println("resultOperation: " + resGetPerson);
                    break;
                case "4":
                    String uid = Utils.askString(scan, "Enter person ID to update: ");
                    String newName = Utils.askString(scan, "New name (leave blank to keep): ");
                    int newAge = Utils.askInt(scan, "New age (-1 to keep): ");
                    ResultOperation resultOperation = Service.updatePerson(uid, newName, newAge, repo);
                    System.out.println("resultOperation: " + resultOperation);
                    break;
                case "5":
                    String did = Utils.askString(scan, "Enter person ID to delete: ");
                    ResultOperation resDeletePerson = Service.deletePerson(did, repo);
                    System.out.println("resultOperation: " + resDeletePerson);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== DOG SUBMENU =====
    private static void dogSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.dogMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    String name = Utils.askString(scan, "Enter dog name: ");
                    String breed = Utils.askString(scan, "Enter breed: ");
                    int age = Utils.askInt(scan, "Enter age: ");
                    ResultOperation resCreateDog = Service.createDog(name, breed, age, repo);
                    System.out.println("resultOperation: " + resCreateDog);
                    break;
                case "2":
                    ResultOperation resAllDogs = Service.getAllDogs(repo);
                    System.out.println("resultOperation: " + resAllDogs);
                    break;
                case "3":
                    String did = Utils.askString(scan, "Enter dog ID: ");
                    ResultOperation resGetDog = Service.getDogById(did, repo);
                    System.out.println("resultOperation: " + resGetDog);
                    break;
                case "4":
                    String uid = Utils.askString(scan, "Enter dog ID to update: ");
                    String newName = Utils.askString(scan, "New name (blank=keep): ");
                    String newBreed = Utils.askString(scan, "New breed (blank=keep): ");
                    int newAge = Utils.askInt(scan, "New age (-1=keep): ");
                    ResultOperation resUpdateDog = Service.updateDog(uid, newName, newBreed, newAge, repo);
                    System.out.println("resultOperation: " + resUpdateDog);
                    break;
                case "5":
                    String delId = Utils.askString(scan, "Enter dog ID to delete: ");
                    ResultOperation resDeleteDog = Service.deleteDog(delId, repo);
                    System.out.println("resultOperation: " + resDeleteDog);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== CAR SUBMENU =====
    private static void carSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    String make = Utils.askString(scan, "Enter make: ");
                    String model = Utils.askString(scan, "Enter model: ");
                    int year = Utils.askInt(scan, "Enter year: ");
                    ResultOperation resCreateCar = Service.createCar(make, model, year, repo);
                    System.out.println("resultOperation: " + resCreateCar);
                    break;
                case "2":
                    ResultOperation resAllCars = Service.getAllCars(repo);
                    System.out.println("resultOperation: " + resAllCars);
                    break;
                case "3":
                    String cid = Utils.askString(scan, "Enter car ID: ");
                    ResultOperation resGetCar = Service.getCarById(cid, repo);
                    System.out.println("resultOperation: " + resGetCar);
                    break;
                case "4":
                    String uid = Utils.askString(scan, "Enter car ID to update: ");
                    String newMake = Utils.askString(scan, "New make (blank=keep): ");
                    String newModel = Utils.askString(scan, "New model (blank=keep): ");
                    int newYear = Utils.askInt(scan, "New year (-1=keep): ");
                    ResultOperation resUpdateCar = Service.updateCar(uid, newMake, newModel, newYear, repo);
                    System.out.println("resultOperation: " + resUpdateCar);
                    break;
                case "5":
                    String delId = Utils.askString(scan, "Enter car ID to delete: ");
                    ResultOperation resDeleteCar = Service.deleteCar(delId, repo);
                    System.out.println("resultOperation: " + resDeleteCar);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // ===== CAR TRANSACTION (READ ONLY) SUBMENU =====
    private static void carTransactionSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.carTransactionMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    ResultOperation resAllTx = Service.getAllCarTransactions(repo);
                    System.out.println("resultOperation: " + resAllTx);
                    break;
                case "2":
                    String tid = Utils.askString(scan, "Enter transaction ID: ");
                    ResultOperation resGetTx = Service.getCarTransactionById(tid, repo);
                    System.out.println("resultOperation: " + resGetTx);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Demo buyCar using two people from repo (or create minimal if none)
    private static void runBuyCarDemo(Scanner scan, Repository repo) {
        System.out.println("Welcome to BUY A CAR (person to person)");

        java.util.ArrayList<Person> people = repo.getAllPeople();
        if (people.size() < 2) {
            System.out.println("Need at least two people. Creating demo people...");
            ResultOperation resSeller = Service.createPerson("DemoSeller", 30, repo);
            ResultOperation resBuyer = Service.createPerson("DemoBuyer", 25, repo);
            ResultOperation resDemoCar = Service.createCar("DemoMake", "DemoModel", 2020, repo);
            Person sellerDemo = resSeller.isSuccess() ? (Person) resSeller.getUpdatedObject() : null;
            Person buyerDemo = resBuyer.isSuccess() ? (Person) resBuyer.getUpdatedObject() : null;
            Car demoCar = resDemoCar.isSuccess() ? (Car) resDemoCar.getUpdatedObject() : null;
            if (sellerDemo != null) sellerDemo.setCar(demoCar);
            people = repo.getAllPeople();
        }

        // Pick first two for simplicity (or let user choose later)
        Person seller = people.get(0);
        Person buyer = people.size() > 1 ? people.get(1) : people.get(0);

        if (seller.getCar() == null) {
            System.out.println("Seller has no car. Create one for seller first (use Car menu).");
            return;
        }
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car. Use menus to adjust data first.");
            return;
        }

        int price = Utils.askInt(scan, "Enter price: ");
        if (price < 0) price = 0;

        ResultOperation resBuyCar = Service.buyCar(buyer, seller, price, repo);
        System.out.println("resultOperation: " + resBuyCar);
    }

    // ===== DOG ADOPTION (READ ONLY) SUBMENU =====
    private static void dogAdoptionSubMenu(Scanner scan, Repository repo) {
        while (true) {
            Utils.dogAdoptionMenu();
            String opt = Utils.askMenuOption(scan);
            switch (opt) {
                case "1":
                    ResultOperation resAllAdoptions = Service.getAllDogAdoptions(repo);
                    System.out.println("resultOperation: " + resAllAdoptions);
                    break;
                case "2":
                    String aid = Utils.askString(scan, "Enter adoption ID: ");
                    ResultOperation resGetAdoption = Service.getDogAdoptionById(aid, repo);
                    System.out.println("resultOperation: " + resGetAdoption);
                    break;
                case "3":
                    String pid = Utils.askString(scan, "Enter person ID: ");
                    Person p = repo.getPersonById(pid.trim());
                    ResultOperation resDogs = Service.getDogsByOwner(p, repo);
                    System.out.println("resultOperation: " + resDogs);
                    break;
                case "4":
                    String did = Utils.askString(scan, "Enter dog ID: ");
                    Dog dg = repo.getDogById(did.trim());
                    ResultOperation resOwners = Service.getOwnersByDog(dg, repo);
                    System.out.println("resultOperation: " + resOwners);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Adopt a dog (shared custody): user picks person and dog BY ID
    private static void runAdoptDog(Scanner scan, Repository repo) {
        System.out.println("Welcome to ADOPT A DOG (shared custody)");

        String personId = Utils.askString(scan, "Enter adopter (person) ID: ");
        String dogId = Utils.askString(scan, "Enter dog ID: ");
        int fee = Utils.askInt(scan, "Enter adoption fee: ");

        Person adopter = repo.getPersonById(personId.trim());
        Dog dog = repo.getDogById(dogId.trim());

        ResultOperation resAdopt = Service.adoptDog(adopter, dog, fee, repo);
        System.out.println("resultOperation: " + resAdopt);
    }

    // Return a dog: end only this person's custody for the dog
    private static void runReturnDog(Scanner scan, Repository repo) {
        System.out.println("Welcome to RETURN A DOG (end one adoption)");

        String personId = Utils.askString(scan, "Enter person ID: ");
        String dogId = Utils.askString(scan, "Enter dog ID: ");

        Person person = repo.getPersonById(personId.trim());
        Dog dog = repo.getDogById(dogId.trim());

        ResultOperation resReturn = Service.returnDog(person, dog, repo);
        System.out.println("resultOperation: " + resReturn);
    }
}
