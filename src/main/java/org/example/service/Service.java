package org.example.service;

import org.example.repository.Repository;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.utils.ResultOperation;

import java.util.ArrayList;
import java.util.Date;

/**
 * All business logic lives here.
 * v4: EVERY method returns a ResultOperation (no more raw boolean / entity / ArrayList returns).
 */
public class Service {

    // ========== PERSON CRUD ==========

    public static ResultOperation createPerson(String name, int age, Repository repo) {
        System.out.println("Welcome to CREATE PERSON");
        ResultOperation result = new ResultOperation();

        if (name == null || name.trim().isEmpty()) {
            return result.setter(false, "Name cannot be empty.", "Person", System.currentTimeMillis());
        }
        if (age < 0) {
            return result.setter(false, "Age cannot be negative.", "Person", System.currentTimeMillis());
        }

        Person p = new Person(name.trim(), age);
        repo.addPerson(p);
        return result.setter(true, "Person created successfully.", "Person", System.currentTimeMillis(), p);
    }

    public static ResultOperation getPersonById(String id, Repository repo) {
        System.out.println("Welcome to GET PERSON BY ID");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Person", System.currentTimeMillis());
        }

        Person p = repo.getPersonById(id.trim());
        if (p == null) {
            return result.setter(false, "Person not found with id: " + id, "Person", System.currentTimeMillis());
        }
        return result.setter(true, "Person found.", "Person", System.currentTimeMillis(), p);
    }

    public static ResultOperation getAllPeople(Repository repo) {
        System.out.println("Welcome to LIST ALL PEOPLE");
        ResultOperation result = new ResultOperation();

        ArrayList<Person> list = repo.getAllPeople();
        return result.setter(true, "Total people: " + list.size(), "Person", System.currentTimeMillis(), list);
    }

    public static ResultOperation updatePerson(String id, String newName, int newAge, Repository repo) {
        System.out.println("Welcome to UPDATE PERSON");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Person", System.currentTimeMillis());
        }

        Person p = repo.getPersonById(id.trim());
        if (p == null) {
            return result.setter(false, "Person not found with id: " + id, "Person", System.currentTimeMillis());
        }

        if (newName != null && !newName.trim().isEmpty()) {
            p.setName(newName.trim());
        }
        if (newAge >= 0) {
            p.setAge(newAge);
        }

        return result.setter(true, "Person updated successfully.", "Person", System.currentTimeMillis(), p);
    }

    public static ResultOperation deletePerson(String id, Repository repo) {
        System.out.println("Welcome to DELETE PERSON");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Person", System.currentTimeMillis());
        }

        Person p = repo.getPersonById(id.trim());
        if (p == null) {
            return result.setter(false, "Person not found with id: " + id, "Person", System.currentTimeMillis());
        }

        // Clear car reference if owns one
        if (p.getCar() != null) {
            p.setCar(null);
        }

        boolean removed = repo.removePerson(id.trim());
        if (!removed) {
            return result.setter(false, "Person could not be deleted.", "Person", System.currentTimeMillis());
        }
        // Successful delete returns no payload
        return result.setter(true, "Person deleted successfully.", "Person", System.currentTimeMillis());
    }

    // ========== CAR CRUD ==========

    public static ResultOperation createCar(String make, String model, int year, Repository repo) {
        System.out.println("Welcome to CREATE CAR");
        ResultOperation result = new ResultOperation();

        if (make == null || make.trim().isEmpty()) {
            return result.setter(false, "Make cannot be empty.", "Car", System.currentTimeMillis());
        }
        if (model == null || model.trim().isEmpty()) {
            return result.setter(false, "Model cannot be empty.", "Car", System.currentTimeMillis());
        }
        if (year < 1886) {
            return result.setter(false, "Year looks invalid.", "Car", System.currentTimeMillis());
        }

        Car c = new Car(make.trim(), model.trim(), year);
        repo.addCar(c);
        return result.setter(true, "Car created successfully.", "Car", System.currentTimeMillis(), c);
    }

    public static ResultOperation getCarById(String id, Repository repo) {
        System.out.println("Welcome to GET CAR BY ID");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Car", System.currentTimeMillis());
        }

        Car c = repo.getCarById(id.trim());
        if (c == null) {
            return result.setter(false, "Car not found with id: " + id, "Car", System.currentTimeMillis());
        }
        return result.setter(true, "Car found.", "Car", System.currentTimeMillis(), c);
    }

    public static ResultOperation getAllCars(Repository repo) {
        System.out.println("Welcome to LIST ALL CARS");
        ResultOperation result = new ResultOperation();

        ArrayList<Car> list = repo.getAllCars();
        return result.setter(true, "Total cars: " + list.size(), "Car", System.currentTimeMillis(), list);
    }

    public static ResultOperation updateCar(String id, String newMake, String newModel, int newYear, Repository repo) {
        System.out.println("Welcome to UPDATE CAR");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Car", System.currentTimeMillis());
        }

        Car c = repo.getCarById(id.trim());
        if (c == null) {
            return result.setter(false, "Car not found with id: " + id, "Car", System.currentTimeMillis());
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

        return result.setter(true, "Car updated successfully.", "Car", System.currentTimeMillis(), c);
    }

    public static ResultOperation deleteCar(String id, Repository repo) {
        System.out.println("Welcome to DELETE CAR");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Car", System.currentTimeMillis());
        }

        Car c = repo.getCarById(id.trim());
        if (c == null) {
            return result.setter(false, "Car not found with id: " + id, "Car", System.currentTimeMillis());
        }

        // Clear from any person owning it
        for (Person p : repo.getAllPeople()) {
            if (p.getCar() != null && p.getCar().getId().equals(id.trim())) {
                p.setCar(null);
            }
        }

        boolean removed = repo.removeCar(id.trim());
        if (!removed) {
            return result.setter(false, "Car could not be deleted.", "Car", System.currentTimeMillis());
        }
        return result.setter(true, "Car deleted successfully.", "Car", System.currentTimeMillis());
    }

    // ========== DOG CRUD ==========

    public static ResultOperation createDog(String name, String breed, int age, Repository repo) {
        System.out.println("Welcome to CREATE DOG");
        ResultOperation result = new ResultOperation();

        if (name == null || name.trim().isEmpty()) {
            return result.setter(false, "Name cannot be empty.", "Dog", System.currentTimeMillis());
        }
        if (breed == null || breed.trim().isEmpty()) {
            return result.setter(false, "Breed cannot be empty.", "Dog", System.currentTimeMillis());
        }
        if (age < 0) {
            return result.setter(false, "Age cannot be negative.", "Dog", System.currentTimeMillis());
        }

        Dog d = new Dog(name.trim(), breed.trim(), age);
        repo.addDog(d);
        return result.setter(true, "Dog created successfully.", "Dog", System.currentTimeMillis(), d);
    }

    public static ResultOperation getDogById(String id, Repository repo) {
        System.out.println("Welcome to GET DOG BY ID");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Dog", System.currentTimeMillis());
        }

        Dog d = repo.getDogById(id.trim());
        if (d == null) {
            return result.setter(false, "Dog not found with id: " + id, "Dog", System.currentTimeMillis());
        }
        return result.setter(true, "Dog found.", "Dog", System.currentTimeMillis(), d);
    }

    public static ResultOperation getAllDogs(Repository repo) {
        System.out.println("Welcome to LIST ALL DOGS");
        ResultOperation result = new ResultOperation();

        ArrayList<Dog> list = repo.getAllDogs();
        return result.setter(true, "Total dogs: " + list.size(), "Dog", System.currentTimeMillis(), list);
    }

    public static ResultOperation updateDog(String id, String newName, String newBreed, int newAge, Repository repo) {
        System.out.println("Welcome to UPDATE DOG");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Dog", System.currentTimeMillis());
        }

        Dog d = repo.getDogById(id.trim());
        if (d == null) {
            return result.setter(false, "Dog not found with id: " + id, "Dog", System.currentTimeMillis());
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

        return result.setter(true, "Dog updated successfully.", "Dog", System.currentTimeMillis(), d);
    }

    public static ResultOperation deleteDog(String id, Repository repo) {
        System.out.println("Welcome to DELETE DOG");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Dog", System.currentTimeMillis());
        }

        Dog d = repo.getDogById(id.trim());
        if (d == null) {
            return result.setter(false, "Dog not found with id: " + id, "Dog", System.currentTimeMillis());
        }

        boolean removed = repo.removeDog(id.trim());
        if (!removed) {
            return result.setter(false, "Dog could not be deleted.", "Dog", System.currentTimeMillis());
        }
        return result.setter(true, "Dog deleted successfully.", "Dog", System.currentTimeMillis());
    }

    // ========== CAR TRANSACTION (READ ONLY) ==========

    public static ResultOperation getCarTransactionById(String id, Repository repo) {
        System.out.println("Welcome to GET CAR TRANSACTION BY ID");
        ResultOperation result = new ResultOperation();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "CarTransaction", System.currentTimeMillis());
        }

        CarTransaction ct = repo.getCarTransactionById(id.trim());
        if (ct == null) {
            return result.setter(false, "CarTransaction not found with id: " + id, "CarTransaction", System.currentTimeMillis());
        }
        return result.setter(true, "CarTransaction found.", "CarTransaction", System.currentTimeMillis(), ct);
    }

    public static ResultOperation getAllCarTransactions(Repository repo) {
        System.out.println("Welcome to LIST ALL CAR TRANSACTIONS");
        ResultOperation result = new ResultOperation();

        ArrayList<CarTransaction> list = repo.getAllCarTransactions();
        return result.setter(true, "Total transactions: " + list.size(), "CarTransaction", System.currentTimeMillis(), list);
    }

    // ========== BUYCAR (PERSON TO PERSON > CARTRANSACTION) ==========

    public static ResultOperation buyCar(Person buyer, Person seller, int price, Repository repo) {
        System.out.println("Welcome to BUY MENU");
        ResultOperation result = new ResultOperation();

        // person buyer exists at array
        if (buyer == null || !repo.getAllPeople().contains(buyer)) {
            return result.setter(false, "Buyer not found in repository.", "CarTransaction", System.currentTimeMillis());
        }

        // person seller exists at array
        if (seller == null || !repo.getAllPeople().contains(seller)) {
            return result.setter(false, "Seller not found in repository.", "CarTransaction", System.currentTimeMillis());
        }

        if (buyer == seller) {
            return result.setter(false, "Buyer and seller must be different people.", "CarTransaction", System.currentTimeMillis());
        }

        // seller must have a car to sell
        if (seller.getCar() == null) {
            return result.setter(false, "Seller does not have a car to sell.", "CarTransaction", System.currentTimeMillis());
        }

        Car car = seller.getCar();

        // buyer must not have a car
        if (buyer.getCar() != null) {
            return result.setter(false, "Buyer already has a car.", "CarTransaction", System.currentTimeMillis());
        }

        // transfer ownership
        buyer.setCar(car);
        seller.setCar(null);

        // create and persist CarTransaction
        CarTransaction transaction = new CarTransaction(buyer, seller, new Date(), car, "Car sold for " + price);
        repo.addCarTransaction(transaction);

        return result.setter(true, "Car bought successfully: " + transaction, "CarTransaction", System.currentTimeMillis(), transaction);
    }

    // ========== DOG ADOPTION ==========
    public static ResultOperation adoptDog(org.example.model.Person adopter, org.example.model.Dog dog, int fee, Repository repo) {
        ResultOperation result = new ResultOperation();
        return result.setter(false, "Not implemented yet", "DogAdoption", System.currentTimeMillis());
    }

    public static ResultOperation getDogAdoptionById(String id, Repository repo) {
        ResultOperation result = new ResultOperation();
        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "DogAdoption", System.currentTimeMillis());
        }
        org.example.model.DogAdoption da = repo.getDogAdoptionById(id.trim());
        if (da == null) {
            return result.setter(false, "DogAdoption not found with id: " + id, "DogAdoption", System.currentTimeMillis());
        }
        return result.setter(true, "DogAdoption found.", "DogAdoption", System.currentTimeMillis(), da);
    }

    public static ResultOperation getAllDogAdoptions(Repository repo) {
        ResultOperation result = new ResultOperation();
        java.util.ArrayList<org.example.model.DogAdoption> list = repo.getAllDogAdoptions();
        return result.setter(true, "Total dog adoptions: " + list.size(), "DogAdoption", System.currentTimeMillis(), list);
    }
}
