package org.example.repository;

import org.example.model.Dog;
import org.example.model.DogAdoption;
import org.example.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class DogAdoptionRepositoryTest {

    private Repository repo;

    @BeforeEach
    void setUp() {
        repo = new Repository();
    }

    @Test
    void addAndGetDogAdoption() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        DogAdoption adoption = new DogAdoption(adopter, dog, new Date(), 150, "Dog adopted for 150");

        repo.addDogAdoption(adoption);

        assertEquals(1, repo.getAllDogAdoptions().size());
        assertSame(adoption, repo.getDogAdoptionById(adoption.getId()));
    }

    @Test
    void getDogAdoptionById_notFound() {
        assertNull(repo.getDogAdoptionById("missing-id"));
    }

    @Test
    void getAllDogAdoptions_returnsList() {
        Person a1 = new Person("Ana", 30);
        Person a2 = new Person("Luis", 25);
        Dog d1 = new Dog("Rex", "Labrador", 5);
        Dog d2 = new Dog("Max", "Beagle", 3);

        repo.addDogAdoption(new DogAdoption(a1, d1, new Date(), 100, "c1"));
        repo.addDogAdoption(new DogAdoption(a2, d2, new Date(), 200, "c2"));

        ArrayList<DogAdoption> list = repo.getAllDogAdoptions();
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    void removeDogAdoption() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        DogAdoption adoption = new DogAdoption(adopter, dog, new Date(), 150, "Dog adopted for 150");

        repo.addDogAdoption(adoption);

        assertTrue(repo.removeDogAdoption(adoption.getId()));
        assertFalse(repo.removeDogAdoption(adoption.getId())); // already removed
        assertEquals(0, repo.getAllDogAdoptions().size());
        assertNull(repo.getDogAdoptionById(adoption.getId()));
    }

    @Test
    void dogAdoption_toStringSafe() {
        Person adopter = new Person("Ana", 30);
        Dog dog = new Dog("Rex", "Labrador", 5);
        DogAdoption adoption = new DogAdoption(adopter, dog, new Date(), 150, "Dog adopted for 150");

        assertNotNull(adoption.toString());
        assertDoesNotThrow(() -> adoption.toString());
    }
}