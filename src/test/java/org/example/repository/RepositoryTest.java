package org.example.repository;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class RepositoryTest {

    private Repository repo;

    @BeforeEach
    void setUp() {
        repo = new Repository();
    }

    @Test
    void addAndGetPerson() {
        Person p = new Person("Test", 25);
        repo.addPerson(p);
        assertEquals(1, repo.getAllPeople().size());
        assertSame(p, repo.getPersonById(p.getId()));
        assertNull(repo.getPersonById("missing"));
    }

    @Test
    void removePerson() {
        Person p = new Person("Test", 25);
        repo.addPerson(p);
        assertTrue(repo.removePerson(p.getId()));
        assertFalse(repo.removePerson(p.getId())); // already removed
        assertEquals(0, repo.getAllPeople().size());
    }

    @Test
    void addAndGetCar() {
        Car c = new Car("Ford", "Focus", 2019);
        repo.addCar(c);
        assertSame(c, repo.getCarById(c.getId()));
        assertEquals(1, repo.getAllCars().size());
    }

    @Test
    void removeCar() {
        Car c = new Car("Ford", "Focus", 2019);
        repo.addCar(c);
        assertTrue(repo.removeCar(c.getId()));
        assertFalse(repo.removeCar(c.getId()));
    }

    @Test
    void addAndGetDog() {
        Dog d = new Dog("Fido", "Lab", 5);
        repo.addDog(d);
        assertSame(d, repo.getDogById(d.getId()));
        assertEquals(1, repo.getAllDogs().size());
    }

    @Test
    void removeDog() {
        Dog d = new Dog("Fido", "Lab", 5);
        repo.addDog(d);
        assertTrue(repo.removeDog(d.getId()));
        assertFalse(repo.removeDog(d.getId()));
    }

    @Test
    void addAndGetCarTransaction() {
        Person b = new Person("B", 30);
        Person s = new Person("S", 40);
        Car c = new Car("X", "Y", 2020);
        CarTransaction tx = new CarTransaction(b, s, new Date(), c, "contract");
        repo.addCarTransaction(tx);
        assertSame(tx, repo.getCarTransactionById(tx.getId()));
        assertEquals(1, repo.getAllCarTransactions().size());
    }

    @Test
    void removeCarTransaction() {
        Person b = new Person("B", 30);
        Person s = new Person("S", 40);
        Car c = new Car("X", "Y", 2020);
        CarTransaction tx = new CarTransaction(b, s, new Date(), c, "contract");
        repo.addCarTransaction(tx);
        assertTrue(repo.removeCarTransaction(tx.getId()));
        assertFalse(repo.removeCarTransaction(tx.getId()));
    }
}