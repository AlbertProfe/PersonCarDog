package org.example.repository;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;

import java.util.ArrayList;
import java.util.UUID;

public class Repository {

    private String id;
    private ArrayList<Person> people;
    private ArrayList<Car> cars;
    private ArrayList<Dog> dogs;
    private ArrayList<CarTransaction> carTransactions;

    public Repository(){
        this.id = UUID.randomUUID().toString();
        this.people = new ArrayList<>();
        this.cars = new ArrayList<>();
        this.dogs = new ArrayList<>();
        this.carTransactions = new ArrayList<>();
    }

    // ===== Person =====
    public void addPerson(Person person) {
        people.add(person);
    }

    public Person getPersonById(String id) {
        for (Person p : people) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Person> getAllPeople() {
        return people;
    }

    public boolean removePerson(String id) {
        return people.removeIf(p -> p.getId().equals(id));
    }

    // ===== Car =====
    public void addCar(Car car) {
        cars.add(car);
    }

    public Car getCarById(String id) {
        for (Car c : cars) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    public ArrayList<Car> getAllCars() {
        return cars;
    }

    public boolean removeCar(String id) {
        return cars.removeIf(c -> c.getId().equals(id));
    }

    // ===== Dog =====
    public void addDog(Dog dog) {
        dogs.add(dog);
    }

    public Dog getDogById(String id) {
        for (Dog d : dogs) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }

    public ArrayList<Dog> getAllDogs() {
        return dogs;
    }

    public boolean removeDog(String id) {
        return dogs.removeIf(d -> d.getId().equals(id));
    }

    // ===== CarTransaction =====
    public void addCarTransaction(CarTransaction carTransaction) {
        carTransactions.add(carTransaction);
    }

    public CarTransaction getCarTransactionById(String id) {
        for (CarTransaction ct : carTransactions) {
            if (ct.getId().equals(id)) {
                return ct;
            }
        }
        return null;
    }

    public ArrayList<CarTransaction> getAllCarTransactions() {
        return carTransactions;
    }

    public boolean removeCarTransaction(String id) {
        return carTransactions.removeIf(ct -> ct.getId().equals(id));
    }
}
