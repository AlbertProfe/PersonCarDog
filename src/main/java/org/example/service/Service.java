package org.example.service;

import java.util.ArrayList;
import java.util.Date;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.utils.ResultUpdateOperation;
import org.example.utils.ResultOperation;

public class Service {

    // ========== PERSON CRUD (MIGRADO A V4) ==========

    /**
     * MOOC Helsinki - Parte 3 & 5: Validación de Strings (.trim(), .isEmpty())
     * y encapsulación del estado del resultado de la creación.
     */
    public static ResultOperation createPerson(String name, int age, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis(); // Marca de tiempo en milisegundos

        if (name == null || name.trim().isEmpty()) {
            return result.setter(false, "Name cannot be empty.", "Person", now);
        }

        if (age < 0) {
            return result.setter(false, "Age cannot be negative.", "Person", now);
        }

        Person p = new Person(name.trim(), age);
        repo.addPerson(p);

        // Retornamos éxito pasando la entidad recién creada como payload (updatedObject)
        return result.setter(true, "Welcome to CREATE PERSON\nPerson created successfully.", "Person", now, p);
    }

    /**
     * MOOC Helsinki - Parte 5: Búsqueda y retorno seguro de objetos usando IDs.
     */
    public static ResultOperation getPersonById(String id, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Person", now);
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            return result.setter(false, "Welcome to GET PERSON BY ID\nPerson not found with id: " + id, "Person", now);
        }

        return result.setter(true, "Welcome to GET PERSON BY ID\nFound: " + p, "Person", now, p);
    }

    /**
     * MOOC Helsinki - Parte 3 & 6: Uso de ArrayList y bucles for-each para iterar colecciones.
     */
    public static ResultOperation getAllPeople(Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        ArrayList<Person> list = repo.getAllPeople();

        // Construimos el mensaje semántico de texto plano
        StringBuilder sb = new StringBuilder("Welcome to LIST ALL PEOPLE\nTotal people: " + list.size());
        for (Person p : list) {
            sb.append("\n").append(p);
        }

        // Devolvemos la lista completa empaquetada en el objeto de transporte
        return result.setter(true, sb.toString(), "Person", now, list);
    }

    /**
     * MOOC Helsinki - Parte 4 & 5: Modificación de los atributos de una instancia mediante setters.
     * ¡ATENCIÓN EQUIPO!: Aquí cambiamos el antiguo ResultUpdateOperation por el ResultOperation oficial.
     */
    public static ResultOperation updatePerson(String id, String newName, int newAge, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Person", now);
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            return result.setter(false, "Person not found.", "Person", now);
        }

        if (newName != null && !newName.trim().isEmpty()) {
            p.setName(newName.trim());
        }

        if (newAge >= 0) {
            p.setAge(newAge);
        }

        return result.setter(true, "Person updated: " + p, "Person", now, p);
    }

    /**
     * MOOC Helsinki - Parte 5: Gestión de referencias cruzadas. Desvinculamos el coche antes de eliminar.
     */
    public static ResultOperation deletePerson(String id, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Person", now);
        }

        Person p = repo.getPersonById(id.trim());

        if (p == null) {
            return result.setter(false, "Person not found.", "Person", now);
        }

        // Si la persona tiene coche asignado, rompemos el enlace para evitar inconsistencias
        if (p.getCar() != null) {
            p.setCar(null);
        }

        boolean removed = repo.removePerson(id.trim());

        if (removed) {
            return result.setter(true, "Welcome to DELETE PERSON\nPerson deleted successfully.", "Person", now);
        } else {
            return result.setter(false, "Failed to remove person from storage.", "Person", now);
        }
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