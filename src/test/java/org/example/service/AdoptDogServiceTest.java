package org.example.service;

import org.example.model.Car;
import org.example.model.Dog;
import org.example.model.DogAdoption;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.utils.ResultOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AdoptDogServiceTest {

    private Repository repo;

    @BeforeEach
    void setUp() {
        repo = new Repository();
    }

    @Test
    void adoptDog_success() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, 150, repo);

        assertTrue(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNotNull(res.getUpdatedObject());
        assertTrue(res.getUpdatedObject() instanceof DogAdoption);

        DogAdoption adoption = (DogAdoption) res.getUpdatedObject();
        assertSame(adopter, adoption.getAdopter());
        assertSame(dog, adoption.getDog());
        assertEquals(150, adoption.getFee());
        assertNotNull(adoption.getContract());
        assertNotNull(adoption.getDate());
        assertNotNull(adoption.getId());

        // Bidirectional consistency
        assertSame(dog, adopter.getDog());
        assertSame(adopter, dog.getOwner());

        // Stored in repository
        assertEquals(1, repo.getAllDogAdoptions().size());
        assertSame(adoption, repo.getDogAdoptionById(adoption.getId()));
    }

    @Test
    void adoptDog_success_feeZero() {
        Person adopter = new Person("Luis", 25);
        Dog dog = new Dog("Max", "Beagle", 3);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, 0, repo);

        assertTrue(res.isSuccess());
        DogAdoption adoption = (DogAdoption) res.getUpdatedObject();
        assertEquals(0, adoption.getFee());
        assertSame(dog, adopter.getDog());
        assertSame(adopter, dog.getOwner());
    }

    @Test
    void adoptDog_fail_adopterNotInRepo() {
        Person adopter = new Person("Ana", 30); // not added
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, 100, repo);

        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
        assertEquals(0, repo.getAllDogAdoptions().size());
        assertNull(dog.getOwner());
    }

    @Test
    void adoptDog_fail_dogNotInRepo() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5); // not added
        repo.addPerson(adopter);

        ResultOperation res = Service.adoptDog(adopter, dog, 100, repo);

        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
        assertNull(adopter.getDog());
        assertEquals(0, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_adopterAlreadyOwnsDog() {
        Person adopter = new Person("Ana", 30);
        Dog dog1 = new Dog("Rex", "Labrador", 5);
        Dog dog2 = new Dog("Toby", "Mix", 4);
        repo.addPerson(adopter);
        repo.addDog(dog1);
        repo.addDog(dog2);

        // First adoption succeeds
        ResultOperation first = Service.adoptDog(adopter, dog1, 100, repo);
        assertTrue(first.isSuccess());
        assertSame(dog1, adopter.getDog());

        // Second adoption must fail
        ResultOperation second = Service.adoptDog(adopter, dog2, 120, repo);
        assertFalse(second.isSuccess());
        assertEquals("DogAdoption", second.getType());
        assertNull(second.getUpdatedObject());
        assertSame(dog1, adopter.getDog()); // unchanged
        assertSame(adopter, dog1.getOwner());
        assertNull(dog2.getOwner());
        assertEquals(1, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_dogAlreadyHasOwner() {
        Person adopter1 = new Person("Ana", 30);
        Person adopter2 = new Person("Luis", 25);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter1);
        repo.addPerson(adopter2);
        repo.addDog(dog);

        ResultOperation first = Service.adoptDog(adopter1, dog, 100, repo);
        assertTrue(first.isSuccess());
        assertSame(adopter1, dog.getOwner());

        ResultOperation second = Service.adoptDog(adopter2, dog, 110, repo);
        assertFalse(second.isSuccess());
        assertEquals("DogAdoption", second.getType());
        assertNull(second.getUpdatedObject());
        assertSame(adopter1, dog.getOwner());
        assertNull(adopter2.getDog());
        assertEquals(1, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_notAdult() {
        Person adopter = new Person("Mina", 17); // under 18
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, 100, repo);

        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
        assertNull(adopter.getDog());
        assertNull(dog.getOwner());
        assertEquals(0, repo.getAllDogAdoptions().size());
    }

    @Test
    void adoptDog_fail_feeNegative() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation res = Service.adoptDog(adopter, dog, -10, repo);

        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
        assertNull(adopter.getDog());
        assertNull(dog.getOwner());
        assertEquals(0, repo.getAllDogAdoptions().size());
    }

    @Test
    void getDogAdoptionById_found() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);

        ResultOperation created = Service.adoptDog(adopter, dog, 200, repo);
        DogAdoption adoption = (DogAdoption) created.getUpdatedObject();

        ResultOperation found = Service.getDogAdoptionById(adoption.getId(), repo);
        assertTrue(found.isSuccess());
        assertEquals("DogAdoption", found.getType());
        assertSame(adoption, found.getUpdatedObject());
    }

    @Test
    void getDogAdoptionById_notFound() {
        ResultOperation res = Service.getDogAdoptionById("nonexistent-id", repo);
        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
    }

    @Test
    void getDogAdoptionById_emptyId() {
        ResultOperation res = Service.getDogAdoptionById("", repo);
        assertFalse(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNull(res.getUpdatedObject());
    }

    @Test
    void getAllDogAdoptions_empty() {
        ResultOperation res = Service.getAllDogAdoptions(repo);
        assertTrue(res.isSuccess());
        assertEquals("DogAdoption", res.getType());
        assertNotNull(res.getUpdatedObject());
        assertTrue(res.getUpdatedObject() instanceof ArrayList);
        @SuppressWarnings("unchecked")
        ArrayList<DogAdoption> list = (ArrayList<DogAdoption>) res.getUpdatedObject();
        assertEquals(0, list.size());
    }

    @Test
    void getAllDogAdoptions_multiple() {
        Person a1 = new Person("Ana", 30);
        Person a2 = new Person("Luis", 25);
        Dog d1 = new Dog("Rex", "Labrador", 5);
        Dog d2 = new Dog("Max", "Beagle", 3);
        repo.addPerson(a1);
        repo.addPerson(a2);
        repo.addDog(d1);
        repo.addDog(d2);

        Service.adoptDog(a1, d1, 100, repo);
        Service.adoptDog(a2, d2, 150, repo);

        ResultOperation res = Service.getAllDogAdoptions(repo);
        assertTrue(res.isSuccess());
        @SuppressWarnings("unchecked")
        ArrayList<DogAdoption> list = (ArrayList<DogAdoption>) res.getUpdatedObject();
        assertEquals(2, list.size());
    }

    @Test
    void deletePerson_clearsDogOwnerRelation() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);
        Service.adoptDog(adopter, dog, 100, repo);

        assertSame(adopter, dog.getOwner());
        assertSame(dog, adopter.getDog());

        ResultOperation del = Service.deletePerson(adopter.getId(), repo);
        assertTrue(del.isSuccess());

        assertNull(repo.getPersonById(adopter.getId()));
        assertNotNull(repo.getDogById(dog.getId())); // dog remains
        assertNull(dog.getOwner()); // relation cleared
    }

    @Test
    void deleteDog_clearsPersonDogRelation() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);
        Service.adoptDog(adopter, dog, 100, repo);

        assertSame(dog, adopter.getDog());
        assertSame(adopter, dog.getOwner());

        ResultOperation del = Service.deleteDog(dog.getId(), repo);
        assertTrue(del.isSuccess());

        assertNull(repo.getDogById(dog.getId()));
        assertNotNull(repo.getPersonById(adopter.getId())); // person remains
        assertNull(adopter.getDog()); // relation cleared
    }

    @Test
    void buyCar_stillWorks_afterAdoptionLogic() {
        // Regression: existing flow unchanged
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
        assertNull(seller.getCar());
        assertSame(car, buyer.getCar());
    }

    @Test
    void dogToString_noInfiniteRecursion() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        repo.addPerson(adopter);
        repo.addDog(dog);
        Service.adoptDog(adopter, dog, 100, repo);

        assertNotNull(dog.toString());
        // Should not throw StackOverflowError
        assertDoesNotThrow(() -> dog.toString());
    }
}