package org.example.service;

import java.util.ArrayList;
import java.util.Date;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.utils.ResultUpdateOperation;

public class Service {

    // ========== PERSON CRUD ==========

    public static Person createPerson(String name, int age, Repository repo) {
        System.out.println("Welcome to CREATE PERSON");

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return null;
        }

        if (age < 0) {
            System.out.println("Age cannot be negative.");
            return null;
        }

        Person p = new Person(name.trim(), age);
        repo.addPerson(p);

        System.out.println("Person created: " + p);
        return p;
    }

    public static Person getPersonById(String id, Repository repo) {
        System.out.println("Welcome to GET PERSON BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            System.out.println("Person not found with id: " + id);
        } else {
            System.out.println("Found: " + p);
        }

        return p;
    }

    public static ArrayList<Person> getAllPeople(Repository repo) {
        System.out.println("Welcome to LIST ALL PEOPLE");

        ArrayList<Person> list = repo.getAllPeople();

        System.out.println("Total people: " + list.size());

        for (Person p : list) {
            System.out.println(p);
        }

        return list;
    }

    public static ResultUpdateOperation updatePerson(
            String id, String newName, int newAge, Repository repo) {

        if (id == null || id.trim().isEmpty()) {
            return new ResultUpdateOperation(
                    "UPDATE_PERSON",
                    System.currentTimeMillis(),
                    false,
                    "ID cannot be empty.",
                    null
            );
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            return new ResultUpdateOperation(
                    "UPDATE_PERSON",
                    System.currentTimeMillis(),
                    false,
                    "Person not found.",
                    null
            );
        }

        if (newName != null && !newName.trim().isEmpty()) {
            p.setName(newName.trim());
        }

        if (newAge >= 0) {
            p.setAge(newAge);
        }

        return new ResultUpdateOperation(
                "UPDATE_PERSON",
                System.currentTimeMillis(),
                true,
                "Person updated: " + p,
                p
        );
    }

    public static boolean deletePerson(String id, Repository repo) {
        System.out.println("Welcome to DELETE PERSON");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            System.out.println("Person not found.");
            return false;
        }

        // Clear car reference if owns one
        if (p.getCar() != null) {
            p.setCar(null);
            System.out.println(
                    "Cleared car reference from person before delete."
            );
        }

        boolean removed = repo.removePerson(id.trim());

        if (removed) {
            System.out.println("Person deleted successfully.");
        }

        return removed;
    }

    // ========== CAR CRUD ==========

    public static Car createCar(
            String make, String model, int year, Repository repo) {

        System.out.println("Welcome to CREATE CAR");

        if (make == null || make.trim().isEmpty()) {
            System.out.println("Make cannot be empty.");
            return null;
        }

        if (model == null || model.trim().isEmpty()) {
            System.out.println("Model cannot be empty.");
            return null;
        }

        if (year < 1886) {
            System.out.println("Year looks invalid.");
            return null;
        }

        Car c = new Car(make.trim(), model.trim(), year);
        repo.addCar(c);

        System.out.println("Car created: " + c);
        return c;
    }

    public static Car getCarById(String id, Repository repo) {
        System.out.println("Welcome to GET CAR BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            System.out.println("Car not found with id: " + id);
        } else {
            System.out.println("Found: " + c);
        }

        return c;
    }

    public static ArrayList<Car> getAllCars(Repository repo) {
        System.out.println("Welcome to LIST ALL CARS");

        ArrayList<Car> list = repo.getAllCars();

        System.out.println("Total cars: " + list.size());

        for (Car c : list) {
            System.out.println(c);
        }

        return list;
    }

    public static ResultUpdateOperation updateCar(
            String id, String newMake, String newModel,
            int newYear, Repository repo) {

        if (id == null || id.trim().isEmpty()) {
            return new ResultUpdateOperation(
                    "UPDATE_CAR",
                    System.currentTimeMillis(),
                    false,
                    "ID cannot be empty.",
                    null
            );
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            return new ResultUpdateOperation(
                    "UPDATE_CAR",
                    System.currentTimeMillis(),
                    false,
                    "Car not found.",
                    null
            );
        }

        if (newMake != null && !newMake.trim().isEmpty()) {
            c.setMake(newMake.trim());
        }

        if (newModel != null && !newModel.trim().isEmpty()) {
            c.setModel(newModel.trim());
        }

        if (newYear >= 1886) {
            c.setYear(newYear);
        }

        return new ResultUpdateOperation(
                "UPDATE_CAR",
                System.currentTimeMillis(),
                true,
                "Car updated: " + c,
                c
        );
    }

    public static boolean deleteCar(String id, Repository repo) {
        System.out.println("Welcome to DELETE CAR");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            System.out.println("Car not found.");
            return false;
        }

        // Clear from any person owning it
        for (Person p : repo.getAllPeople()) {
            if (p.getCar() != null
                    && p.getCar().getId().equals(id.trim())) {

                p.setCar(null);
                System.out.println("Removed car reference from owner: " + p);
            }
        }

        boolean removed = repo.removeCar(id.trim());

        if (removed) {
            System.out.println("Car deleted successfully.");
        }

        return removed;
    }

    // ========== DOG CRUD ==========

    public static Dog createDog(
            String name, String breed, int age, Repository repo) {

        System.out.println("Welcome to CREATE DOG");

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return null;
        }

        if (breed == null || breed.trim().isEmpty()) {
            System.out.println("Breed cannot be empty.");
            return null;
        }

        if (age < 0) {
            System.out.println("Age cannot be negative.");
            return null;
        }

        Dog d = new Dog(name.trim(), breed.trim(), age);
        repo.addDog(d);

        System.out.println("Dog created: " + d);
        return d;
    }

    public static Dog getDogById(String id, Repository repo) {
        System.out.println("Welcome to GET DOG BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        Dog d = repo.getDogById(id.trim());

        if (d == null) {
            System.out.println("Dog not found with id: " + id);
        } else {
            System.out.println("Found: " + d);
        }

        return d;
    }

    public static ArrayList<Dog> getAllDogs(Repository repo) {
        System.out.println("Welcome to LIST ALL DOGS");

        ArrayList<Dog> list = repo.getAllDogs();

        System.out.println("Total dogs: " + list.size());

        for (Dog d : list) {
            System.out.println(d);
        }

        return list;
    }

    public static ResultUpdateOperation updateDog(
            String id, String newName, String newBreed,
            int newAge, Repository repo) {

        if (id == null || id.trim().isEmpty()) {
            return new ResultUpdateOperation(
                    "UPDATE_DOG",
                    System.currentTimeMillis(),
                    false,
                    "ID cannot be empty.",
                    null
            );
        }

        Dog d = repo.getDogById(id.trim());

        if (d == null) {
            return new ResultUpdateOperation(
                    "UPDATE_DOG",
                    System.currentTimeMillis(),
                    false,
                    "Dog not found.",
                    null
            );
        }

        if (newName != null && !newName.trim().isEmpty()) {
            d.setName(newName.trim());
        }

        if (newBreed != null && !newBreed.trim().isEmpty()) {
            d.setBreed(newBreed.trim());
        }

        if (newAge >= 0) {
            d.setAge(newAge);
        }

        return new ResultUpdateOperation(
                "UPDATE_DOG",
                System.currentTimeMillis(),
                true,
                "Dog updated: " + d,
                d
        );
    }

    public static boolean deleteDog(String id, Repository repo) {
        System.out.println("Welcome to DELETE DOG");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }

        Dog d = repo.getDogById(id.trim());

        if (d == null) {
            System.out.println("Dog not found.");
            return false;
        }

        boolean removed = repo.removeDog(id.trim());

        if (removed) {
            System.out.println("Dog deleted successfully.");
        }

        return removed;
    }

    // ========== CAR TRANSACTION (READ ONLY) ==========

    public static CarTransaction getCarTransactionById(
            String id, Repository repo) {

        System.out.println("Welcome to GET CAR TRANSACTION BY ID");

        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return null;
        }

        CarTransaction ct = repo.getCarTransactionById(id.trim());

        if (ct == null) {
            System.out.println("CarTransaction not found with id: " + id);
        } else {
            System.out.println("Found: " + ct);
        }

        return ct;
    }

    public static ArrayList<CarTransaction> getAllCarTransactions(
            Repository repo) {

        System.out.println("Welcome to LIST ALL CAR TRANSACTIONS");

        ArrayList<CarTransaction> list = repo.getAllCarTransactions();

        System.out.println("Total transactions: " + list.size());

        for (CarTransaction ct : list) {
            System.out.println(ct);
        }

        return list;
    }

    // ========== BUY CAR (PERSON TO PERSON) ==========

    public static boolean buyCar(
            Person buyer, Person seller, int price, Repository repo) {

        System.out.println("Welcome to BUY MENU");

        // Check buyer exists in repository
        if (!repo.getAllPeople().contains(buyer)) {
            System.out.println("Buyer not found in repository.");
            return false;
        }

        System.out.println("Buyer exists: " + buyer);

        // Check seller exists in repository
        if (!repo.getAllPeople().contains(seller)) {
            System.out.println("Seller not found in repository.");
            return false;
        }

        System.out.println("Seller exists: " + seller);

        // Check seller has a car
        if (seller.getCar() == null) {
            System.out.println("Seller does not have a car to sell.");
            return false;
        }

        Car car = seller.getCar();
        System.out.println("Seller can sell a car: " + car);

        // Check buyer does not already have a car
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car.");
            return false;
        }

        System.out.println("Buyer can buy a car.");

        // Transfer ownership
        buyer.setCar(car);
        seller.setCar(null);

        System.out.println("Settings done, now creating CarTransaction ...");

        CarTransaction transaction = new CarTransaction(
                buyer,
                seller,
                new Date(),
                car,
                "Car sold for " + price
        );

        repo.addCarTransaction(transaction);

        System.out.println(transaction);
        return true;
    }
}