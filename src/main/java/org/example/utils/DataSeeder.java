package org.example.utils;

import com.github.javafaker.Faker;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.DogAdoption;
import org.example.model.Person;
import org.example.repository.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataSeeder {

    public static void seedRepository(Repository repo) {
        Faker faker = new Faker();

        List<Car> temporaryCars = new ArrayList<>();
        List<Person> temporaryPeople = new ArrayList<>();

        //  1. Generate 3 Cars (Objects 1, 2 y 3)
        for (int i = 0; i < 3; i++) {
            Car car = new Car(
                    faker.company().name(),
                    faker.commerce().productName(),
                    faker.number().numberBetween(1995, 2025)
            );
            repo.addCar(car);
            temporaryCars.add(car);
        }

        // 2. Generate 3 People (Objects 4, 5 y 6)
        for (int i = 0; i < 3; i++) {
            Person person = new Person(
                    faker.name().fullName(),
                    faker.number().numberBetween(18, 80)
            );
            temporaryPeople.add(person);
        }

        // Service.buyCar needs a valid seller and buyer
        // so we assign the first car to the first person
        temporaryPeople.get(0).setCar(temporaryCars.get(0));

        // Person 1 will not have a car
        // Person 2 will have a random car
        temporaryPeople.get(2).setCar(temporaryCars.get(1));

        // Add people to repository
        for (Person p : temporaryPeople) {
            repo.addPerson(p);
        }

        // 3. Generate 2 Dogs (Objects 7 and 8)
        List<Dog> temporaryDogs = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Dog dog = new Dog(
                    faker.dog().name(),
                    faker.dog().breed(),
                    faker.number().numberBetween(1, 15)
            );
            repo.addDog(dog);
            temporaryDogs.add(dog);
        }

        // 3b. Pre-adopt using many-to-many shared custody.
        //     Seed a shared-custody example:
        //       - Dog0 adopted by Person0 and Person1 (two owners)
        //       - Person0 also adopts Dog1 (person with two active dogs)
        //     People are adults; fee >=0.
        Person p0 = temporaryPeople.get(0);
        Person p1 = temporaryPeople.get(1);
        Dog d0 = temporaryDogs.get(0);
        Dog d1 = temporaryDogs.get(1);

        // p0 adopts d0
        p0.addDog(d0);
        d0.addOwner(p0);
        repo.addDogAdoption(new DogAdoption(p0, d0, new Date(), faker.number().numberBetween(0, 300), faker.lorem().sentence()));

        // p1 also adopts d0 (shared custody)
        p1.addDog(d0);
        d0.addOwner(p1);
        repo.addDogAdoption(new DogAdoption(p1, d0, new Date(), faker.number().numberBetween(0, 300), faker.lorem().sentence()));

        // p0 adopts d1 (p0 now has two dogs)
        p0.addDog(d1);
        d1.addOwner(p0);
        repo.addDogAdoption(new DogAdoption(p0, d1, new Date(), faker.number().numberBetween(0, 300), faker.lorem().sentence()));

        // 4. Generate 2 fake transactions (Objects 9 and 10)
        // We use car 2, people 1 and 2
        CarTransaction tx1 = new CarTransaction(
                temporaryPeople.get(1), // buyer
                temporaryPeople.get(2), // seller
                new Date(),
                temporaryCars.get(2),
                faker.lorem().sentence()
        );
        // Add transaction to repository
        repo.addCarTransaction(tx1);

        // Generate transaction 2
        CarTransaction tx2 = new CarTransaction(
                temporaryPeople.get(2),
                temporaryPeople.get(0),
                new Date(),
                temporaryCars.get(2),
                faker.lorem().sentence()
        );
        // Add transaction to repository
        repo.addCarTransaction(tx2);

        // Print verification
        System.out.println("=== DATA SEEDING COMPLETE ===");
        System.out.println("People: " + repo.getAllPeople().size()
                + " | Cars: " + repo.getAllCars().size()
                + " | Dogs: " + repo.getAllDogs().size()
                + " | Transactions: " + repo.getAllCarTransactions().size()
                + " | Adoptions: " + repo.getAllDogAdoptions().size());
        System.out.println("=============================");
    }
}
