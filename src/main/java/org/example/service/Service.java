package org.example.service;

import java.util.ArrayList;
import java.util.Date;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.repository.Repository;
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

    // ========== CAR CRUD (MIGRADO A V4) ==========

    /**
     * MOOC Helsinki - Parte 1 & 3: Validaciones condicionales complejas y manejo de tipos primitivos (int).
     */
    public static ResultOperation createCar(String make, String model, int year, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (make == null || make.trim().isEmpty() || model == null || model.trim().isEmpty()) {
            return result.setter(false, "Make and Model cannot be empty.", "Car", now);
        }

        if (year < 1886) { // 1886: Invención del automóvil moderno
            return result.setter(false, "Year looks invalid (must be 1886 or later).", "Car", now);
        }

        Car c = new Car(make.trim(), model.trim(), year);
        repo.addCar(c);

        return result.setter(true, "Welcome to CREATE CAR\nCar created successfully.", "Car", now, c);
    }

    /**
     * MOOC Helsinki - Parte 5: Búsqueda indexada en base a identificadores de objetos.
     */
    public static ResultOperation getCarById(String id, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Car", now);
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            return result.setter(false, "Welcome to GET CAR BY ID\nCar not found with id: " + id, "Car", now);
        }

        return result.setter(true, "Welcome to GET CAR BY ID\nFound: " + c, "Car", now, c);
    }

    /**
     * MOOC Helsinki - Parte 6: Composición de texto dinámico con bucles For-Each sobre listas dinámicas.
     */
    public static ResultOperation getAllCars(Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        ArrayList<Car> list = repo.getAllCars();
        StringBuilder sb = new StringBuilder("Welcome to LIST ALL CARS\nTotal cars: " + list.size());
        for (Car c : list) {
            sb.append("\n").append(c);
        }

        return result.setter(true, sb.toString(), "Car", now, list);
    }

    /**
     * MOOC Helsinki - Parte 4 & 5: Modificación del estado interno de una entidad existente (Mutación).
     * Reemplaza de forma definitiva el wrapper antiguo en la firma del método.
     */
    public static ResultOperation updateCar(String id, String newMake, String newModel, int newYear, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Car", now);
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            return result.setter(false, "Car not found.", "Car", now);
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

        return result.setter(true, "Car updated: " + c, "Car", now, c);
    }

    /**
     * MOOC Helsinki - Parte 5 & 6: Desvinculación de referencias en cascada en colecciones dinámicas.
     */
    public static ResultOperation deleteCar(String id, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Car", now);
        }

        Car c = repo.getCarById(id.trim());

        if (c == null) {
            return result.setter(false, "Car not found.", "Car", now);
        }

        // Antes de eliminar el coche, recorremos las personas para limpiar la propiedad compartida
        for (Person p : repo.getAllPeople()) {
            if (p.getCar() != null && p.getCar().getId().equals(id.trim())) {
                p.setCar(null);
            }
        }

        boolean removed = repo.removeCar(id.trim());

        if (removed) {
            return result.setter(true, "Welcome to DELETE CAR\nCar deleted successfully.", "Car", now);
        }
        return result.setter(false, "Failed to delete car from storage.", "Car", now);
    }

    // ========== DOG CRUD (MIGRADO A V4) ==========

    public static ResultOperation createDog(String name, String breed, int age, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (name == null || name.trim().isEmpty() || breed == null || breed.trim().isEmpty()) {
            return result.setter(false, "Name and Breed cannot be empty.", "Dog", now);
        }

        if (age < 0) {
            return result.setter(false, "Age cannot be negative.", "Dog", now);
        }

        Dog d = new Dog(name.trim(), breed.trim(), age);
        repo.addDog(d);

        return result.setter(true, "Welcome to CREATE DOG\nDog created successfully.", "Dog", now, d);
    }

    public static ResultOperation getDogById(String id, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Dog", now);
        }

        Dog d = repo.getDogById(id.trim());

        if (d == null) {
            return result.setter(false, "Welcome to GET DOG BY ID\nDog not found with id: " + id, "Dog", now);
        }

        return result.setter(true, "Welcome to GET DOG BY ID\nFound: " + d, "Dog", now, d);
    }

    public static ResultOperation getAllDogs(Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        ArrayList<Dog> list = repo.getAllDogs();
        StringBuilder sb = new StringBuilder("Welcome to LIST ALL DOGS\nTotal dogs: " + list.size());
        for (Dog d : list) {
            sb.append("\n").append(d);
        }

        return result.setter(true, sb.toString(), "Dog", now, list);
    }

    public static ResultOperation updateDog(String id, String newName, String newBreed, int newAge, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Dog", now);
        }

        Dog d = repo.getDogById(id.trim());

        if (d == null) {
            return result.setter(false, "Dog not found.", "Dog", now);
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

        return result.setter(true, "Dog updated: " + d, "Dog", now, d);
    }

    public static ResultOperation deleteDog(String id, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "Dog", now);
        }

        Dog d = repo.getDogById(id.trim());

        if (d == null) {
            return result.setter(false, "Dog not found.", "Dog", now);
        }

        boolean removed = repo.removeDog(id.trim());

        if (removed) {
            return result.setter(true, "Welcome to DELETE DOG\nDog deleted successfully.", "Dog", now);
        }
        return result.setter(false, "Failed to delete dog from storage.", "Dog", now);
    }

    // ========== CAR TRANSACTION (MIGRADO A V4 - READ ONLY) ==========

    /**
     * MOOC Helsinki - Parte 5: Búsqueda y recuperación de registros históricos inmutables.
     */
    public static ResultOperation getCarTransactionById(String id, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        if (id == null || id.trim().isEmpty()) {
            return result.setter(false, "ID cannot be empty.", "CarTransaction", now);
        }

        CarTransaction ct = repo.getCarTransactionById(id.trim());

        if (ct == null) {
            return result.setter(false, "Welcome to GET CAR TRANSACTION BY ID\nCarTransaction not found with id: " + id, "CarTransaction", now);
        }

        return result.setter(true, "Welcome to GET CAR TRANSACTION BY ID\nFound: " + ct, "CarTransaction", now, ct);
    }

    /**
     * MOOC Helsinki - Parte 6: Recorrido y recolección de historiales complejos en listas dinámicas.
     */
    public static ResultOperation getAllCarTransactions(Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        ArrayList<CarTransaction> list = repo.getAllCarTransactions();
        StringBuilder sb = new StringBuilder("Welcome to LIST ALL CAR TRANSACTIONS\nTotal transactions: " + list.size());
        for (CarTransaction ct : list) {
            sb.append("\n").append(ct);
        }

        return result.setter(true, sb.toString(), "CarTransaction", now, list);
    }

    // ========== BUY CAR (MIGRADO A V4 - PERSON TO PERSON) ==========

    /**
     * MOOC Helsinki - Parte 5 & 7: Caso de uso complejo. Validación de reglas de negocio cruzadas
     * entre múltiples instancias y mutación segura de referencias de objetos.
     */
    public static ResultOperation buyCar(Person buyer, Person seller, int price, Repository repo) {
        ResultOperation result = new ResultOperation();
        long now = System.currentTimeMillis();

        // 1. Validar que el comprador existe de forma real en el sistema
        if (!repo.getAllPeople().contains(buyer)) {
            return result.setter(false, "Buyer not found in repository.", "CarTransaction", now);
        }

        // 2. Validar que el vendedor existe de forma real en el sistema
        if (!repo.getAllPeople().contains(seller)) {
            return result.setter(false, "Seller not found in repository.", "CarTransaction", now);
        }

        // 3. Regla de negocio: El vendedor debe poseer un automóvil para realizar la venta
        if (seller.getCar() == null) {
            return result.setter(false, "Seller does not have a car to sell.", "CarTransaction", now);
        }

        // 4. Regla de negocio: El comprador NO debe poseer un automóvil asignado actualmente
        if (buyer.getCar() != null) {
            return result.setter(false, "Buyer already has a car.", "CarTransaction", now);
        }

        Car car = seller.getCar();

        // 5. Transferencia de propiedad en memoria (MOOC Parte 5: Mutación de referencias cruzadas)
        buyer.setCar(car);
        seller.setCar(null);

        // 6. Instanciación del registro transaccional inmutable (MOOC Parte 4: Clases nativas Date)
        CarTransaction transaction = new CarTransaction(
                buyer,
                seller,
                new Date(),
                car,
                "Car sold for " + price
        );

        repo.addCarTransaction(transaction);

        return result.setter(true, "Welcome to BUY MENU\nTransaction complete successfully.", "CarTransaction", now, transaction);
    }
}
