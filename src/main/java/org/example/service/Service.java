package org.example.service;

import org.example.repository.Repository;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.DogAdoption;
import org.example.model.Person;
import org.example.persistence.JsonStore;
import org.example.utils.ResultOperation;

import java.io.IOException;
import java.nio.file.Path;
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
        // Cascade m2m: remove this person from all their dogs' owners lists
        // and mark all related DogAdoptions as RETURNED (audit trail, do not delete records)
        for (Dog d : new ArrayList<>(p.getDogs())) {
            d.getOwners().remove(p);
            for (DogAdoption da : repo.getAllDogAdoptions()) {
                if (da.getAdopter() != null && da.getAdopter().getId().equals(p.getId())
                        && da.getDog() != null && da.getDog().getId().equals(d.getId())
                        && "ACTIVE".equals(da.getStatus())) {
                    da.setStatus("RETURNED");
                    String c = da.getContract();
                    if (c == null || !c.endsWith(" - RETURNED")) {
                        da.setContract((c == null ? "" : c) + " - RETURNED");
                    }
                }
            }
        }
        p.getDogs().clear();
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
        // Cascade m2m: remove this dog from all its owners' dogs lists
        // and mark all related DogAdoptions as RETURNED
        for (Person owner : new ArrayList<>(d.getOwners())) {
            owner.getDogs().remove(d);
            for (DogAdoption da : repo.getAllDogAdoptions()) {
                if (da.getDog() != null && da.getDog().getId().equals(d.getId())
                        && da.getAdopter() != null && da.getAdopter().getId().equals(owner.getId())
                        && "ACTIVE".equals(da.getStatus())) {
                    da.setStatus("RETURNED");
                    String c = da.getContract();
                    if (c == null || !c.endsWith(" - RETURNED")) {
                        da.setContract((c == null ? "" : c) + " - RETURNED");
                    }
                }
            }
        }
        d.getOwners().clear();
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

    // ========== DOG ADOPTION (READ ONLY) ==========

    public static ResultOperation getDogAdoptionById(String id, Repository repo) {
        System.out.println("Welcome to GET DOG ADOPTION BY ID");
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            System.out.println("ID cannot be empty.");
            return result.setter(false, "ID cannot be empty.", "DogAdoption", System.currentTimeMillis());
        }
        DogAdoption da = repo.getDogAdoptionById(id.trim());
        if (da == null) {
            System.out.println("DogAdoption not found with id: " + id);
            return result.setter(false, "DogAdoption not found.", "DogAdoption", System.currentTimeMillis());
        }
        System.out.println("Found: " + da);
        return result.setter(true, "DogAdoption found.", "DogAdoption", System.currentTimeMillis(), da);
    }

    public static ResultOperation getAllDogAdoptions(Repository repo) {
        System.out.println("Welcome to LIST ALL DOG ADOPTIONS");
        ResultOperation result = new ResultOperation();
        ArrayList<DogAdoption> list = repo.getAllDogAdoptions();
        System.out.println("Total adoptions: " + list.size());
        for (DogAdoption da : list) {
            System.out.println(da);
        }
        return result.setter(true, "Retrieved all dog adoptions.", "DogAdoption", System.currentTimeMillis(), list);
    }

    // ========== ADOPTDOG (MANY-TO-MANY SHARED CUSTODY) ==========

    public static ResultOperation adoptDog(Person adopter, Dog dog, int fee, Repository repo) {

        System.out.println("Welcome to ADOPT A DOG (shared custody)");
        ResultOperation result = new ResultOperation();

        // 1. adopter exists in repo
        if (adopter == null || !repo.getAllPeople().contains(adopter)) {
            System.out.println("Adopter not found in repository.");
            return result.setter(false, "Adopter not found in repository.", "DogAdoption", System.currentTimeMillis());
        }
        System.out.println("Adopter exists: " + adopter);

        // 2. dog exists in repo
        if (dog == null || !repo.getAllDogs().contains(dog)) {
            System.out.println("Dog not found in repository.");
            return result.setter(false, "Dog not found in repository.", "DogAdoption", System.currentTimeMillis());
        }
        System.out.println("Dog exists: " + dog);

        // 3. only adults can adopt
        if (adopter.getAge() < 18) {
            System.out.println("Adopter must be an adult.");
            return result.setter(false, "Adopter must be an adult.", "DogAdoption", System.currentTimeMillis());
        }

        // 4. fee validation
        if (fee < 0) {
            System.out.println("Fee cannot be negative.");
            return result.setter(false, "Fee cannot be negative.", "DogAdoption", System.currentTimeMillis());
        }

        // 5. no duplicate ACTIVE pair (person, dog)
        for (DogAdoption da : repo.getAllDogAdoptions()) {
            if (da.getAdopter() != null && da.getAdopter().getId().equals(adopter.getId())
                    && da.getDog() != null && da.getDog().getId().equals(dog.getId())
                    && "ACTIVE".equals(da.getStatus())) {
                System.out.println("This person already has an active adoption for this dog.");
                return result.setter(false, "Duplicate active adoption for this pair.", "DogAdoption", System.currentTimeMillis());
            }
        }

        // 6. max 3 active dogs per person
        long activeDogsForPerson = repo.getAllDogAdoptions().stream()
                .filter(da -> da.getAdopter() != null && da.getAdopter().getId().equals(adopter.getId()) && "ACTIVE".equals(da.getStatus()))
                .count();
        if (activeDogsForPerson >= 3) {
            System.out.println("Person already has the maximum number of dogs.");
            return result.setter(false, "Person already has the maximum number of dogs.", "DogAdoption", System.currentTimeMillis());
        }

        // 7. max 2 active owners per dog
        long activeOwnersForDog = repo.getAllDogAdoptions().stream()
                .filter(da -> da.getDog() != null && da.getDog().getId().equals(dog.getId()) && "ACTIVE".equals(da.getStatus()))
                .count();
        if (activeOwnersForDog >= 2) {
            System.out.println("Dog already has the maximum number of owners.");
            return result.setter(false, "Dog already has the maximum number of owners.", "DogAdoption", System.currentTimeMillis());
        }

        System.out.println("All checks passed, linking person and dog (shared custody) ...");

        // Maintain both sides of the many-to-many relation
        adopter.addDog(dog);
        dog.addOwner(adopter);

        // Record the adoption (read-only afterwards)
        DogAdoption adoption = new DogAdoption(adopter, dog, new Date(), fee, "Dog adopted for " + fee, "ACTIVE");
        repo.addDogAdoption(adoption);

        System.out.println(adoption);

        return result.setter(true, "Dog adopted successfully (shared custody).", "DogAdoption", System.currentTimeMillis(), adoption);
    }

    // ========== RETURN DOG (END ONE SIDE OF THE PAIR) ==========

    public static ResultOperation returnDog(Person person, Dog dog, Repository repo) {
        System.out.println("Welcome to RETURN A DOG");
        ResultOperation result = new ResultOperation();

        if (person == null || !repo.getAllPeople().contains(person)) {
            System.out.println("Person not found in repository.");
            return result.setter(false, "Person not found in repository.", "DogAdoption", System.currentTimeMillis());
        }
        if (dog == null || !repo.getAllDogs().contains(dog)) {
            System.out.println("Dog not found in repository.");
            return result.setter(false, "Dog not found in repository.", "DogAdoption", System.currentTimeMillis());
        }

        // Find the ACTIVE adoption for this exact pair
        DogAdoption target = null;
        for (DogAdoption da : repo.getAllDogAdoptions()) {
            if (da.getAdopter() != null && da.getAdopter().getId().equals(person.getId())
                    && da.getDog() != null && da.getDog().getId().equals(dog.getId())
                    && "ACTIVE".equals(da.getStatus())) {
                target = da;
                break;
            }
        }
        if (target == null) {
            System.out.println("No active adoption found for this person and dog.");
            return result.setter(false, "No active adoption for this pair.", "DogAdoption", System.currentTimeMillis());
        }

        // Remove only this side of the relationship
        person.removeDog(dog);
        dog.removeOwner(person);

        // Mark the adoption returned (history stays)
        target.setStatus("RETURNED");
        String c = target.getContract();
        if (c == null || !c.endsWith(" - RETURNED")) {
            target.setContract((c == null ? "" : c) + " - RETURNED");
        }

        System.out.println("Adoption ended for this pair: " + target);
        return result.setter(true, "Dog returned (this person's custody ended).", "DogAdoption", System.currentTimeMillis(), target);
    }

    // ========== QUERY: DOGS BY OWNER (ACTIVE) ==========

    public static ResultOperation getDogsByOwner(Person person, Repository repo) {
        System.out.println("Welcome to GET DOGS BY OWNER");
        ResultOperation result = new ResultOperation();
        if (person == null || !repo.getAllPeople().contains(person)) {
            System.out.println("Person not found.");
            return result.setter(false, "Person not found.", "Dog", System.currentTimeMillis());
        }
        ArrayList<Dog> owned = new ArrayList<>();
        for (DogAdoption da : repo.getAllDogAdoptions()) {
            if (da.getAdopter() != null && da.getAdopter().getId().equals(person.getId())
                    && "ACTIVE".equals(da.getStatus())
                    && da.getDog() != null) {
                if (!owned.contains(da.getDog())) owned.add(da.getDog());
            }
        }
        System.out.println("Active dogs for " + person.getName() + ": " + owned.size());
        for (Dog d : owned) {
            System.out.println(d);
        }
        return result.setter(true, "Retrieved active dogs for owner.", "Dog", System.currentTimeMillis(), owned);
    }

    // ========== QUERY: OWNERS BY DOG (ACTIVE) ==========

    public static ResultOperation getOwnersByDog(Dog dog, Repository repo) {
        System.out.println("Welcome to GET OWNERS BY DOG");
        ResultOperation result = new ResultOperation();
        if (dog == null || !repo.getAllDogs().contains(dog)) {
            System.out.println("Dog not found.");
            return result.setter(false, "Dog not found.", "Person", System.currentTimeMillis());
        }
        ArrayList<Person> owners = new ArrayList<>();
        for (DogAdoption da : repo.getAllDogAdoptions()) {
            if (da.getDog() != null && da.getDog().getId().equals(dog.getId())
                    && "ACTIVE".equals(da.getStatus())
                    && da.getAdopter() != null) {
                if (!owners.contains(da.getAdopter())) owners.add(da.getAdopter());
            }
        }
        System.out.println("Active owners for " + dog.getName() + ": " + owners.size());
        for (Person p : owners) {
            System.out.println(p);
        }
        return result.setter(true, "Retrieved active owners for dog.", "Person", System.currentTimeMillis(), owners);
    }

    // ========== PERSISTENCE (JSON FILE) ==========

    public static ResultOperation saveRepository(Path path, Repository repo) {
        System.out.println("Welcome to SAVE REPOSITORY");
        ResultOperation result = new ResultOperation();
        try {
            JsonStore.save(repo, path);
            System.out.println("Repository saved to " + path.toAbsolutePath());
            return result.setter(true, "Repository saved.", "Repository", System.currentTimeMillis(), path.toString());
        } catch (IOException e) {
            System.out.println("Could not save repository: " + e.getMessage());
            return result.setter(false, "Could not save repository: " + e.getMessage(), "Repository", System.currentTimeMillis());
        }
    }

    public static ResultOperation loadRepository(Path path, Repository repo) {
        System.out.println("Welcome to LOAD REPOSITORY");
        ResultOperation result = new ResultOperation();
        if (!JsonStore.exists(path)) {
            System.out.println("File not found: " + path.toAbsolutePath());
            return result.setter(false, "File not found.", "Repository", System.currentTimeMillis());
        }
        try {
            repo.clear();
            JsonStore.load(repo, path);
            System.out.println("Repository loaded from " + path.toAbsolutePath());
            System.out.println("People: " + repo.getAllPeople().size()
                    + " | Cars: " + repo.getAllCars().size()
                    + " | Dogs: " + repo.getAllDogs().size()
                    + " | Transactions: " + repo.getAllCarTransactions().size()
                    + " | Adoptions: " + repo.getAllDogAdoptions().size());
            return result.setter(true, "Repository loaded.", "Repository", System.currentTimeMillis(), path.toString());
        } catch (IOException e) {
            System.out.println("Could not load repository: " + e.getMessage());
            return result.setter(false, "Could not load repository: " + e.getMessage(), "Repository", System.currentTimeMillis());
        }
    }
}
