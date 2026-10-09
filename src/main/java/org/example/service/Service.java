package org.example.service;

import org.example.repository.Repository;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Person;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class Service {

    //Person CRUD
    public static Person createPerson(String name, int age, Repository repo) {
        if (name.isEmpty()) {
            System.out.println("Error: Introduce un nombre válido.");
            return null;
        }
        if (age < 18) {
            System.out.println("Error: Introduce un a edad válida. Debe ser mayor o igual de 18 años.");
            return null;
        }

        Person p = new Person(name, age);
        repo.addPerson(p);
        System.out.println("Persona creada correctamente.");

        return p;
    }

    public static Person getPersonById(String id, Repository repo) {
        Person p = repo.getPersonById(id);
        if (p == null) {
            System.out.println("Error: La persona no existe.");
        }
        return p;
    }

    public static ArrayList<Person> getAllPeople(Repository repo) {
        return repo.getAllPeople();
    }

    public static boolean updatePerson(String id, String newName, int newAge, Repository repo) {
        Person p = repo.getPersonById(id);
        if (p == null) {
            System.out.println("Error: La persona no existe.");
            return false;
        }

        if (newName.isEmpty()) {
            System.out.println("Error: Introduce un nombre válido.");
            return false;
        }
        if (newAge < 18) {
            System.out.println("Error: Introduce una edad válida. Debe ser mayor o igual de 18 años.");
            return false;
        }

        p.setName(newName);
        p.setAge(newAge);

        return true;
    }

    public static boolean deletePerson(String id, Repository repo) {
        Person p = repo.getPersonById(id);
        if (p == null) {
            System.out.println("Error: La persona no existe.");
            return false;
        }
        repo.removePerson(id);
        return true;
    }


    //Car CRUD
    public static Car createCar(String make, String model, int year, Repository repo) {
        if (make.isEmpty()) {
            System.out.println("Error: Introduce una marca de coche válida.");
            return null;
        }
        if (model.isEmpty()) {
            System.out.println("Error: Introduce un modelo de coche válido.");
            return null;
        }
        if (year < 1900 || year > LocalDate.now().getYear()) {
            System.out.println("Error: Introduce un año de matriculación válida. Debe ser entre 1900 y el actual.");
            return null;
        }

        Car p = new Car(make, model, year);
        repo.addCar(p);
        System.out.println("El Coche ha sido creado correctamente.");

        return p;
    }

    public static Car getCarById(String id, Repository repo) {
        Car p = repo.getCarById(id);
        if (p == null) {
            System.out.println("Error: El coche no existe.");
        }
        return p;
    }

    public static ArrayList<Car> getAllCars(Repository repo) {
        return repo.getAllCars();
    }

    public static boolean updateCar(String id, String newMake, String newModel, int newYear, Repository repo) {
        Car p = repo.getCarById(id);
        if (p == null) {
            System.out.println("Error: El coche no existe.");
            return false;
        }

        if (newMake.isEmpty()) {
            System.out.println("Error: Introduce una marca de coche válida.");
            return false;
        }
        if (newModel.isEmpty()) {
            System.out.println("Error: Introduce un modelo de coche válido.");
            return false;
        }
        if (newYear < 1900 || newYear > LocalDate.now().getYear()) {
            System.out.println("Error: Introduce un año de matriculación válida. Debe ser entre 1900 y el actual.");
            return false;
        }

        p.setMake(newMake);
        p.setModel(newModel);
        p.setYear(newYear);

        return true;
    }

    public static boolean deleteCar(String id, Repository repo) {
        Car p = repo.getCarById(id);
        if (p == null) {
            System.out.println("Error: El coche no existe.");
            return false;
        }
        repo.removeCar(id);
        return true;
    }








    public static boolean buyCar(Person buyer, Person seller, int price, Repository repo){

        System.out.println("Welcome to BUY MENU");
        // person buyer a exists at array
        if (!repo.getAllPeople().contains(buyer)) {
            System.out.println("Buyer not found in repository.");
            return false;
        }
        System.out.println("Buyer exits: " + buyer);

        // person seller b exists at array
        if (!repo.getAllPeople().contains(seller)) {
            System.out.println("Seller not found in repository.");
            return false;
        }
        System.out.println("Seller exits: " + seller);

        // car exists??
        if (seller.getCar() == null) {
            System.out.println("Seller does not have a car to sell.");
            return false;
        }

        // person a has a car
        Car car = seller.getCar();
        System.out.println("Seller can sell a car: "  + car);

        // person b does not have a car
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car.");
            return false;
        }

        System.out.println("Buyer can buy a car.");

        // personB.setCar (bmw)
        buyer.setCar(car);
        // personA.car = null
        seller.setCar(null);

        System.out.println("Settings done, now creating CarTransaction ...");

        // create object CarTransaction
        CarTransaction transaction = new CarTransaction(buyer, seller, new Date(), car, "Car sold for " + price);
        // save object CarTransaction at repo
        repo.addCarTransaction(transaction);

        // PRINT
        System.out.println(transaction);

        return true;
    }
}
