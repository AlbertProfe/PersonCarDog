package org.example.service;

import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.utils.ResultOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

    private Repository repo;

    @BeforeEach
    void setUp() {
        repo = new Repository();
    }

    @Test
    void createPerson_ok() {
        ResultOperation res = Service.createPerson("Alice", 30, repo);
        assertTrue(res.isSuccess());
        assertEquals("Person", res.getType());
        Person p = (Person) res.getUpdatedObject();
        assertNotNull(p);
        assertEquals("Alice", p.getName());
        assertEquals(30, p.getAge());
        assertEquals(1, repo.getAllPeople().size());
    }

    @Test
    void createPerson_invalidName() {
        ResultOperation res = Service.createPerson("", 30, repo);
        assertFalse(res.isSuccess());
        assertEquals("Person", res.getType());
        assertNull(res.getUpdatedObject());
    }

    @Test
    void createPerson_invalidAge() {
        ResultOperation res = Service.createPerson("Bob", -1, repo);
        assertFalse(res.isSuccess());
        assertEquals("Person", res.getType());
    }

    @Test
    void getPersonById_found() {
        ResultOperation created = Service.createPerson("Alice", 30, repo);
        Person p = (Person) created.getUpdatedObject();
        ResultOperation found = Service.getPersonById(p.getId(), repo);
        assertTrue(found.isSuccess());
        assertEquals(p.getId(), ((Person) found.getUpdatedObject()).getId());
    }

    @Test
    void getPersonById_notFound() {
        ResultOperation found = Service.getPersonById("nope", repo);
        assertFalse(found.isSuccess());
    }

    @Test
    void getAllPeople() {
        Service.createPerson("A", 20, repo);
        Service.createPerson("B", 25, repo);
        ResultOperation res = Service.getAllPeople(repo);
        assertTrue(res.isSuccess());
        assertTrue(res.getUpdatedObject() instanceof ArrayList);
        @SuppressWarnings("unchecked")
        ArrayList<Person> list = (ArrayList<Person>) res.getUpdatedObject();
        assertEquals(2, list.size());
    }

    @Test
    void updatePerson_ok() {
        ResultOperation c = Service.createPerson("Alice", 30, repo);
        String id = ((Person) c.getUpdatedObject()).getId();
        ResultOperation u = Service.updatePerson(id, "Alicia", 31, repo);
        assertTrue(u.isSuccess());
        Person up = (Person) u.getUpdatedObject();
        assertEquals("Alicia", up.getName());
        assertEquals(31, up.getAge());
    }

    @Test
    void updatePerson_partial() {
        ResultOperation c = Service.createPerson("Alice", 30, repo);
        String id = ((Person) c.getUpdatedObject()).getId();
        ResultOperation u = Service.updatePerson(id, "", -1, repo);
        assertTrue(u.isSuccess());
        Person up = (Person) u.getUpdatedObject();
        assertEquals("Alice", up.getName());
        assertEquals(30, up.getAge());
    }

    @Test
    void deletePerson_ok() {
        ResultOperation c = Service.createPerson("Alice", 30, repo);
        String id = ((Person) c.getUpdatedObject()).getId();
        ResultOperation d = Service.deletePerson(id, repo);
        assertTrue(d.isSuccess());
        assertNull(d.getUpdatedObject());
        assertNull(repo.getPersonById(id));
    }

    // Car
    @Test
    void createCar_ok() {
        ResultOperation res = Service.createCar("Tesla", "Model 3", 2020, repo);
        assertTrue(res.isSuccess());
        assertEquals("Car", res.getType());
        assertNotNull(res.getUpdatedObject());
    }

    @Test
    void createCar_invalid() {
        assertFalse(Service.createCar("", "X", 2020, repo).isSuccess());
        assertFalse(Service.createCar("X", "", 2020, repo).isSuccess());
        assertFalse(Service.createCar("X", "Y", 1800, repo).isSuccess());
    }

    @Test
    void carCrud_flow() {
        ResultOperation c = Service.createCar("Toyota", "Corolla", 2015, repo);
        String id = ((Car) c.getUpdatedObject()).getId();
        assertTrue(Service.getCarById(id, repo).isSuccess());
        assertEquals(1, ((ArrayList<?>) Service.getAllCars(repo).getUpdatedObject()).size());
        assertTrue(Service.updateCar(id, "Toyota", "Corolla Hybrid", 2018, repo).isSuccess());
        assertTrue(Service.deleteCar(id, repo).isSuccess());
        assertNull(repo.getCarById(id));
    }

    // Dog
    @Test
    void createDog_ok() {
        ResultOperation res = Service.createDog("Fido", "Labrador", 5, repo);
        assertTrue(res.isSuccess());
        assertEquals("Dog", res.getType());
        assertNotNull(res.getUpdatedObject());
    }

    @Test
    void dogCrud_flow() {
        ResultOperation c = Service.createDog("Max", "Beagle", 3, repo);
        Dog d = (Dog) c.getUpdatedObject();
        assertTrue(Service.getDogById(d.getId(), repo).isSuccess());
        assertEquals(1, ((ArrayList<?>) Service.getAllDogs(repo).getUpdatedObject()).size());
        assertTrue(Service.updateDog(d.getId(), "Max", "Beagle", 4, repo).isSuccess());
        assertTrue(Service.deleteDog(d.getId(), repo).isSuccess());
    }

    // CarTransaction
    @Test
    void getCarTransactions_empty() {
        ResultOperation res = Service.getAllCarTransactions(repo);
        assertTrue(res.isSuccess());
        assertEquals(0, ((ArrayList<?>) res.getUpdatedObject()).size());
    }

    @Test
    void buyCar_ok() {
        Person seller = new Person("Seller", 40);
        Person buyer = new Person("Buyer", 30);
        Car car = new Car("Ford", "Focus", 2019);
        repo.addPerson(seller);
        repo.addPerson(buyer);
        repo.addCar(car);
        seller.setCar(car);

        ResultOperation res = Service.buyCar(buyer, seller, 5000, repo);
        assertTrue(res.isSuccess());
        assertEquals("CarTransaction", res.getType());
        CarTransaction tx = (CarTransaction) res.getUpdatedObject();
        assertNotNull(tx);
        assertSame(buyer, tx.getBuyer());
        assertSame(seller, tx.getSeller());
        assertSame(car, tx.getCar());
        assertNull(seller.getCar());
        assertSame(car, buyer.getCar());
        assertEquals(1, repo.getAllCarTransactions().size());
    }

    @Test
    void buyCar_samePerson() {
        Person p = new Person("P", 30);
        repo.addPerson(p);
        ResultOperation res = Service.buyCar(p, p, 1000, repo);
        assertFalse(res.isSuccess());
    }

    @Test
    void buyCar_sellerHasNoCar() {
        Person s = new Person("S", 30);
        Person b = new Person("B", 25);
        repo.addPerson(s);
        repo.addPerson(b);
        ResultOperation res = Service.buyCar(b, s, 1000, repo);
        assertFalse(res.isSuccess());
    }

    @Test
    void buyCar_buyerAlreadyHasCar() {
        Person s = new Person("S", 30);
        Person b = new Person("B", 25);
        Car c1 = new Car("A", "A1", 2020);
        Car c2 = new Car("B", "B1", 2021);
        repo.addPerson(s);
        repo.addPerson(b);
        repo.addCar(c1);
        repo.addCar(c2);
        s.setCar(c1);
        b.setCar(c2);
        ResultOperation res = Service.buyCar(b, s, 1000, repo);
        assertFalse(res.isSuccess());
    }

    @Test
    void buyCar_buyerNotInRepo() {
        Person s = new Person("S", 30);
        Person b = new Person("B", 25);
        Car c = new Car("A", "A1", 2020);
        repo.addPerson(s);
        repo.addCar(c);
        s.setCar(c);
        ResultOperation res = Service.buyCar(b, s, 1000, repo);
        assertFalse(res.isSuccess());
    }
}