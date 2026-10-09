package org.example.service;

import org.example.repository.Repository;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.utils.resultUpdateOperation;

import java.util.ArrayList;
import java.util.Date;

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

    public static resultUpdateOperation updatePerson(String id, String newName, int newAge, Repository repo) {
        resultUpdateOperation result = new resultUpdateOperation();
        // Welcome message
        System.out.println("Welcome to UPDATE PERSON");
        // Input validation
        // Defensive programming (null checks)
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            result.setSuccess(false);
            result.setMessage("ID cannot be empty.");
            result.setType("Person");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }
        // Defensive programming (null checks)
        Person personFromDB = repo.getPersonById(id.trim());
        if (personFromDB == null) {
            System.out.println("Person not found.");
            result.setSuccess(false);
            result.setMessage("ID cannot be empty.");
            result.setType("Person");
            result.setTimestamp(System.currentTimeMillis());
            return result;
        }
        // Input validation for newName AND update newAge
        if (newName != null && !newName.trim().isEmpty()) {
            personFromDB.setName(newName.trim());
        }
        if (newAge >= 0) {
            personFromDB.setAge(newAge);
        }
        System.out.println("Person updated: " + personFromDB);
        // Finally we return true to signal success
        result.setSuccess(true);
        result.setMessage("Person updated successfully.");
        result.setType("Person");
        result.setTimestamp(System.currentTimeMillis());
        result.setUpdatedObject(personFromDB);
        return result;
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
            System.out.println("Cleared car reference from person before delete.");
        }
        boolean removed = repo.removePerson(id.trim());
        if (removed) {
            System.out.println("Person deleted successfully.");
        }
        return removed;
    }

    // ========== CAR CRUD ==========

    public static Car createCar(String make, String model, int year, Repository repo) {
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

    public static boolean updateCar(String id, String newMake, String newModel, int newYear, Repository repo) {
        System.out.println("Welcome to UPDATE CAR");
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }
        Car c = repo.getCarById(id.trim());
        if (c == null) {
            System.out.println("Car not found.");
            return false;
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
        System.out.println("Car updated: " + c);
        return true;
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
            if (p.getCar() != null && p.getCar().getId().equals(id.trim())) {
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

    public static Dog createDog(String name, String breed, int age, Repository repo) {
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

    public static boolean updateDog(String id, String newName, String newBreed, int newAge, Repository repo) {
        System.out.println("Welcome to UPDATE DOG");
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return false;
        }
        Dog d = repo.getDogById(id.trim());
        if (d == null) {
            System.out.println("Dog not found.");
            return false;
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
        System.out.println("Dog updated: " + d);
        return true;
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

    public static CarTransaction getCarTransactionById(String id, Repository repo) {
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

    public static ArrayList<CarTransaction> getAllCarTransactions(Repository repo) {
        System.out.println("Welcome to LIST ALL CAR TRANSACTIONS");
        ArrayList<CarTransaction> list = repo.getAllCarTransactions();
        System.out.println("Total transactions: " + list.size());
        for (CarTransaction ct : list) {
            System.out.println(ct);
        }
        return list;
    }

    // ========== BUYCAR (PERSON TO PERSON > CARTRANSACTION) ==========

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
