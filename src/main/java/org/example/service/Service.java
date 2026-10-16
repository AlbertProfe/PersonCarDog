package org.example.service;

import jdk.jshell.Snippet;
import jdk.swing.interop.SwingInterOpUtils;
import org.example.model.*;
import org.example.repository.Repository;
import org.example.utils.ResultOperation;

import java.util.ArrayList;
import java.util.Date;

public class Service {

    // ========== PERSON CRUD ==========

    public static ResultOperation createPerson(String name, int age, Repository repo) {
        System.out.println("Welcome to CREATE PERSON");
        ResultOperation result = new ResultOperation();
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return result.setter(false, "Name cannot be empty.", "Person", System.currentTimeMillis());
        }
        if (age < 0) {
            System.out.println("Age cannot be negative.");
            return result.setter(false, "Age cannot be negative.", "Person", System.currentTimeMillis());
        }
        Person p = new Person(name.trim(), age);
        repo.addPerson(p);
        System.out.println("Person created: " + p);
        return result.setter(true, "Person created successfully.", "Person", System.currentTimeMillis(), p);
    }

    public static ResultOperation getPersonById(String id, Repository repo) {
        System.out.println("Welcome to GET PERSON BY ID");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "Person", System.currentTimeMillis());
        }
        Person p = repo.getPersonById(id.trim());
        if (p == null) {
            System.out.println("Person not found with id: " + id);
            return result.setter(false, "Person not found.", "Person", System.currentTimeMillis());
        }
        System.out.println("Found: " + p);
        return result.setter(true, "Person found.", "Person", System.currentTimeMillis(), p);
    }

    public static ResultOperation getAllPeople(Repository repo) {
        System.out.println("Welcome to LIST ALL PEOPLE");
        ResultOperation result = new ResultOperation();
        ArrayList<Person> list = repo.getAllPeople();
        System.out.println("Total people: " + list.size());
        for (Person p : list) {
            System.out.println(p);
        }
        return result.setter(true, "Retrieved all people.", "Person", System.currentTimeMillis(), list);
    }

    public static ResultOperation updatePerson(String id, String newName, int newAge, Repository repo) {
        // introduction message
        System.out.println("Welcome to UPDATE PERSON");
        // create resultUpdateOperation object
        // it will contain the type of the object updated, the timestamp, the success status, the message and the updated object
        // we will use it to return the result of the update operation
        // it will be used in the controller to display the result of the update operation
        ResultOperation result = new ResultOperation();
        // Defensive programming (null checks)
        // if id is null or empty, return false and set the resultUpdateOperation object
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            // return the set resultUpdateOperation object
            return result.setter(false, "ID cannot be empty.", "Person", System.currentTimeMillis());
        }

        Person personFromDB = repo.getPersonById(id.trim());
        if (personFromDB == null) {
            System.out.println("Person not found.");
            // return the set resultUpdateOperation object
            return result.setter(false, "Person not found.", "Person", System.currentTimeMillis());
        }

        // update the person
        if (newName != null && !newName.trim().isEmpty()) {
            personFromDB.setName(newName.trim());
        }
        if (newAge >= 0) {
            personFromDB.setAge(newAge);
        }

        System.out.println("Person updated: " + personFromDB);
        // return the set resultUpdateOperation object
        return result.setter(true, "Person updated successfully.", "Person", System.currentTimeMillis(), personFromDB);
    }

    public static ResultOperation deletePerson(String id, Repository repo) {
        System.out.println("Welcome to DELETE PERSON");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "Person", System.currentTimeMillis());
        }
        Person p = repo.getPersonById(id.trim());
        if (p == null) {
            System.out.println("Person not found.");
            return result.setter(false, "Person not found.", "Person", System.currentTimeMillis());
        }
        // Clear car reference if owns one
        if (p.getCar() != null) {
            p.setCar(null);
            System.out.println("Cleared car reference from person before delete.");
        }
        boolean removed = repo.removePerson(id.trim());
        if (removed) {
            System.out.println("Person deleted successfully.");
            return result.setter(true, "Person deleted successfully.", "Person", System.currentTimeMillis());
        }
        return result.setter(false, "Failed to delete person.", "Person", System.currentTimeMillis());
    }

    // ========== CAR CRUD ==========

    public static ResultOperation createCar(String make, String model, int year, Repository repo) {
        System.out.println("Welcome to CREATE CAR");
        ResultOperation result = new ResultOperation();
        if (make == null || make.trim().isEmpty()) {
            System.out.println("Make cannot be empty.");
            return result.setter(false, "Make cannot be empty.", "Car", System.currentTimeMillis());
        }
        if (model == null || model.trim().isEmpty()) {
            System.out.println("Model cannot be empty.");
            return result.setter(false, "Model cannot be empty.", "Car", System.currentTimeMillis());
        }
        if (year < 1886) {
            System.out.println("Year looks invalid.");
            return result.setter(false, "Year looks invalid.", "Car", System.currentTimeMillis());
        }
        Car c = new Car(make.trim(), model.trim(), year);
        repo.addCar(c);
        System.out.println("Car created: " + c);
        return result.setter(true, "Car created successfully.", "Car", System.currentTimeMillis(), c);
    }

    public static ResultOperation getCarById(String id, Repository repo) {
        System.out.println("Welcome to GET CAR BY ID");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "Car", System.currentTimeMillis());
        }
        Car c = repo.getCarById(id.trim());
        if (c == null) {
            System.out.println("Car not found with id: " + id);
            return result.setter(false, "Car not found.", "Car", System.currentTimeMillis());
        }
        System.out.println("Found: " + c);
        return result.setter(true, "Car found.", "Car", System.currentTimeMillis(), c);
    }

    public static ResultOperation getAllCars(Repository repo) {
        System.out.println("Welcome to LIST ALL CARS");
        ResultOperation result = new ResultOperation();
        ArrayList<Car> list = repo.getAllCars();
        System.out.println("Total cars: " + list.size());
        for (Car c : list) {
            System.out.println(c);
        }
        return result.setter(true, "Retrieved all cars.", "Car", System.currentTimeMillis(), list);
    }

    public static ResultOperation updateCar(String id, String newMake, String newModel, int newYear, Repository repo) {
        System.out.println("Welcome to UPDATE CAR");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "Car", System.currentTimeMillis());
        }
        Car c = repo.getCarById(id.trim());
        if (c == null) {
            System.out.println("Car not found.");
            return result.setter(false, "Car not found.", "Car", System.currentTimeMillis());
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
        return result.setter(true, "Car updated successfully.", "Car", System.currentTimeMillis(), c);
    }

    public static ResultOperation deleteCar(String id, Repository repo) {
        System.out.println("Welcome to DELETE CAR");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "Car", System.currentTimeMillis());
        }
        Car c = repo.getCarById(id.trim());
        if (c == null) {
            System.out.println("Car not found.");
            return result.setter(false, "Car not found.", "Car", System.currentTimeMillis());
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
            return result.setter(true, "Car deleted successfully.", "Car", System.currentTimeMillis());
        }
        return result.setter(false, "Failed to delete car.", "Car", System.currentTimeMillis());
    }

    // ========== DOG CRUD ==========

    public static ResultOperation createDog(String name, String breed, int age, Repository repo) {
        System.out.println("Welcome to CREATE DOG");
        ResultOperation result = new ResultOperation();
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name cannot be empty.");
            return result.setter(false, "Name cannot be empty.", "Dog", System.currentTimeMillis());
        }
        if (breed == null || breed.trim().isEmpty()) {
            System.out.println("Breed cannot be empty.");
            return result.setter(false, "Breed cannot be empty.", "Dog", System.currentTimeMillis());
        }
        if (age < 0) {
            System.out.println("Age cannot be negative.");
            return result.setter(false, "Age cannot be negative.", "Dog", System.currentTimeMillis());
        }
        Dog d = new Dog(name.trim(), breed.trim(), age);
        repo.addDog(d);
        System.out.println("Dog created: " + d);
        return result.setter(true, "Dog created successfully.", "Dog", System.currentTimeMillis(), d);
    }

    public static ResultOperation getDogById(String id, Repository repo) {
        System.out.println("Welcome to GET DOG BY ID");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "Dog", System.currentTimeMillis());
        }
        Dog d = repo.getDogById(id.trim());
        if (d == null) {
            System.out.println("Dog not found with id: " + id);
            return result.setter(false, "Dog not found.", "Dog", System.currentTimeMillis());
        }
        System.out.println("Found: " + d);
        return result.setter(true, "Dog found.", "Dog", System.currentTimeMillis(), d);
    }

    public static ResultOperation getAllDogs(Repository repo) {
        System.out.println("Welcome to LIST ALL DOGS");
        ResultOperation result = new ResultOperation();
        ArrayList<Dog> list = repo.getAllDogs();
        System.out.println("Total dogs: " + list.size());
        for (Dog d : list) {
            System.out.println(d);
        }
        return result.setter(true, "Retrieved all dogs.", "Dog", System.currentTimeMillis(), list);
    }

    public static ResultOperation updateDog(String id, String newName, String newBreed, int newAge, Repository repo) {
        System.out.println("Welcome to UPDATE DOG");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "Dog", System.currentTimeMillis());
        }
        Dog d = repo.getDogById(id.trim());
        if (d == null) {
            System.out.println("Dog not found.");
            return result.setter(false, "Dog not found.", "Dog", System.currentTimeMillis());
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
        return result.setter(true, "Dog updated successfully.", "Dog", System.currentTimeMillis(), d);
    }

    public static ResultOperation deleteDog(String id, Repository repo) {
        System.out.println("Welcome to DELETE DOG");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "Dog", System.currentTimeMillis());
        }
        Dog d = repo.getDogById(id.trim());
        if (d == null) {
            System.out.println("Dog not found.");
            return result.setter(false, "Dog not found.", "Dog", System.currentTimeMillis());
        }
        boolean removed = repo.removeDog(id.trim());
        if (removed) {
            System.out.println("Dog deleted successfully.");
            return result.setter(true, "Dog deleted successfully.", "Dog", System.currentTimeMillis());
        }
        return result.setter(false, "Failed to delete dog.", "Dog", System.currentTimeMillis());
    }

    // ========== CAR TRANSACTION (READ ONLY) ==========

    public static ResultOperation getCarTransactionById(String id, Repository repo) {
        System.out.println("Welcome to GET CAR TRANSACTION BY ID");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "CarTransaction", System.currentTimeMillis());
        }
        CarTransaction ct = repo.getCarTransactionById(id.trim());
        if (ct == null) {
            System.out.println("CarTransaction not found with id: " + id);
            return result.setter(false, "CarTransaction not found.", "CarTransaction", System.currentTimeMillis());
        }
        System.out.println("Found: " + ct);
        return result.setter(true, "CarTransaction found.", "CarTransaction", System.currentTimeMillis(), ct);
    }

    public static ResultOperation getAllCarTransactions(Repository repo) {
        System.out.println("Welcome to LIST ALL CAR TRANSACTIONS");
        ResultOperation result = new ResultOperation();
        ArrayList<CarTransaction> list = repo.getAllCarTransactions();
        System.out.println("Total transactions: " + list.size());
        for (CarTransaction ct : list) {
            System.out.println(ct);
        }
        return result.setter(true, "Retrieved all car transactions.", "CarTransaction", System.currentTimeMillis(), list);
    }

    // ========== BUYCAR (PERSON TO PERSON > CARTRANSACTION) ==========

    public static ResultOperation buyCar(Person buyer, Person seller, int price, Repository repo){

        System.out.println("Welcome to BUY MENU");
        ResultOperation result = new ResultOperation();
        // person buyer a exists at array
        if (!repo.getAllPeople().contains(buyer)) {
            System.out.println("Buyer not found in repository.");
            return result.setter(false, "Buyer not found in repository.", "CarTransaction", System.currentTimeMillis());
        }
        System.out.println("Buyer exits: " + buyer);

        // person seller b exists at array
        if (!repo.getAllPeople().contains(seller)) {
            System.out.println("Seller not found in repository.");
            return result.setter(false, "Seller not found in repository.", "CarTransaction", System.currentTimeMillis());
        }
        System.out.println("Seller exits: " + seller);

        // car exists??
        if (seller.getCar() == null) {
            System.out.println("Seller does not have a car to sell.");
            return result.setter(false, "Seller does not have a car to sell.", "CarTransaction", System.currentTimeMillis());
        }

        // person a has a car
        Car car = seller.getCar();
        System.out.println("Seller can sell a car: "  + car);

        // person b does not have a car
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car.");
            return result.setter(false, "Buyer already has a car.", "CarTransaction", System.currentTimeMillis());
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

        return result.setter(true, "Car purchased successfully.", "CarTransaction", System.currentTimeMillis(), transaction);
    }

    // ==========  ADOPT A DOG (PERSON <- DOG) ==========
    public static ResultOperation adoptDog (Person adopter, Dog dog, int fee, Repository repo){

        System.out.println("Welcome to BUY MENU");
        ResultOperation result = new ResultOperation();
        // person adopter a exists at array
        if (!repo.getAllPeople().contains(adopter)) {
            System.out.println("Adopter wasn´t found in repository.");
            return result.setter(false, "Adopter was not found in repository.", "DogAdoption", System.currentTimeMillis());
        }
        System.out.println("Adopter exits: " + adopter);

        // dog adopted exist?
        if (!repo.getAllDogs().contains(dog)) {
            System.out.println("dog not found in repository.");
            return result.setter(false, "dog not found in repository.", "DogAdoption", System.currentTimeMillis());
        }

        // adopter has dog??
        if (adopter.getDog() != null) {
            System.out.println("Adopter has dog.");
            return result.setter(false, "Adopter has dog.", "DogAdoption", System.currentTimeMillis());
        }
        // dog has adopter??
        if (dog.getOwner() != null) {
            System.out.println("dog has adopter");
            return result.setter(false, "Dog has adopter.", "DogAdoption", System.currentTimeMillis());
        }
        //Adult is minor?
        if (adopter.getAge() < 18) {
            System.out.println("Adopter is not an adult" );
            return result.setter(false, "adopter is not an adult.", "DogAdoption", System.currentTimeMillis());
        }

        // is the fee >=0?
        if (fee <0){
            System.out.println("Fee is negative");
            return result.setter(false, "Fee is negative", "DogAdoption", System.currentTimeMillis());
        }

        // Check ACTIVE status
        ArrayList<DogAdoption> list = repo.getDogAdoptionsByPersonId(adopter.getId());
        for (DogAdoption i: list){
            i.getDog().equals(dog);
            System.out.println(" Person" + adopter + " yet has this dog");
            return result.setter(false, "Person yet has this dog", "DogAdoption", System.currentTimeMillis());
        }

       if (adopter.getDog().size()>=3) {
           System.out.println(" Person" + adopter + " yet has 3 dog");
           return result.setter(false, "Person yet has 3 dog", "DogAdoption", System.currentTimeMillis());
       }
       if (dog.getOwner().size()>=2) {
            System.out.println(" Dog" + dog + " yet has 2 owners");
            return result.setter(false, "Dog yet has 2 owners", "DogAdoption", System.currentTimeMillis());
        }

        System.out.println("Adopter can adopt a dog.");

        // adopt dog to adopter
        adopter.setDog(dog);
        dog.setOwner(adopter);

        System.out.println("Settings done, now creating DogAdoption ...");

        // create object DogAdoption
        DogAdoption dogAdoption = new DogAdoption(adopter, dog, new Date(), fee, "Dog adopted for " + fee + " status is ACTIVATE");
        // save object dogAdoption at repo
        repo.addDogAdoption(dogAdoption);

        // PRINT
        System.out.println(dogAdoption);

        return result.setter(true, "Dog adopted successfully.", "DogAdoption", System.currentTimeMillis(), dogAdoption);
    }


    public ResultOperation getDogAdoptionById(String id, Repository repo) {
        System.out.println("Welcome to GET adoption BY ID");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "DogAdoption", System.currentTimeMillis());
        }
        DogAdoption d = repo.getDogAdoptionById(id.trim());
        if (d == null) {
            System.out.println("adoption not found with id: " + id);
            return result.setter(false, "adoption not found.", "DogAdoption", System.currentTimeMillis());
        }
        System.out.println("Found: " + d);
        return result.setter(true, "Person found.", "Person", System.currentTimeMillis(), d);
    }

    public ResultOperation getAllDogAdoptions(Repository repo) {
        System.out.println("Welcome to LIST ALL DOG ADOPTIONS");
        ResultOperation result = new ResultOperation();
        ArrayList<DogAdoption> list = repo.getAllDogAdoptions();
        System.out.println("Total Dog Adoptions: " + list.size());
        for (DogAdoption exercise: list) {
            System.out.println(exercise);
        }
        return result.setter(true, "Retrieved all DogAdoption.", "DogAdoption", System.currentTimeMillis(), list);
    }

    // Lists DogsByOwner and OwnersByDog
    public ResultOperation getDogsByOwner (Person person, Repository repo) {
        ResultOperation result = new ResultOperation();
        if(repo.getPersonById(person.getId())==null){
            System.out.println("repository doesn't have person with id: " + person.getId());
            return result.setter(false, "repository doesn't have person with id.", "DogAdoption", System.currentTimeMillis(), repo);
        }
        ArrayList<Person> dogs = person.getDog();
        if (dogs==null){
            System.out.println(" Dog doesn't have owners");
            return result.setter(false, "Dog doesn't have owners for list", "DogAdoption", System.currentTimeMillis(), repo);
        }
        System.out.println(dogs);
        return result.setter(true, "list dogs by owner ", "DogAdoption", System.currentTimeMillis(), repo);
    }
    public ResultOperation getOwnersByDog (Dog dog, Repository repo) {
        ResultOperation result = new ResultOperation();
        if(repo.getDogById(dog.getId())==null){
            System.out.println("Repository doesn't have person with id: " + dog.getId());
            return result.setter(false, "repository doesn't have dog with id.", "DogAdoption", System.currentTimeMillis(), repo);
        }
        ArrayList<Person> owners = dog.getOwner();
        if (owners==null){
            System.out.println(" Dog doesn't have owners");
            return result.setter(false, "Dog doesn't have owners for list", "DogAdoption", System.currentTimeMillis(), repo);
        }
        System.out.println(owners);
        return result.setter(true, "list owners by dog ", "DogAdoption", System.currentTimeMillis(), repo);
            }
    public static ResultOperation returnDog (Person person, Dog dog, Repository repo){
        ResultOperation result = new ResultOperation();
        ArrayList<DogAdoption> list = repo.getDogAdoptionsByPersonId(person.getId());
        if (list == null){
            System.out.println("There aren't dog adoptions with person");
            return result.setter(false, "There aren't dog adoptions with person", "DogAdoption", System.currentTimeMillis(), list);
        }
        for (DogAdoption dA: list){
            if(dA.getDog().equals(dog)) {
                dA.getStatus();
                person.removeDog(dog);
                dog.removeOwner(person);
                dA.setContract(dA.getContract() + " - RETURNED");
                System.out.println(dA);
                return result.setter(false, "There aren't dog adoptions with person", "DogAdoption", System.currentTimeMillis(), list);
            }
        }
        System.out.println("Person doesn't have this dog");
        return result.setter(false, "Person doesn't have this dog", "DogAdoption", System.currentTimeMillis(), list);
    }
}