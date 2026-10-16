package org.example.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
import org.example.model.DogAdoption;
import org.example.model.Person;
import org.example.persistence.RepositorySnapshot.CarDto;
import org.example.persistence.RepositorySnapshot.CarTransactionDto;
import org.example.persistence.RepositorySnapshot.DogAdoptionDto;
import org.example.persistence.RepositorySnapshot.DogDto;
import org.example.persistence.RepositorySnapshot.PersonDto;
import org.example.repository.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

// Saves/loads the Repository to/from a JSON file.
// Equivalent role to the database in Spring Boot (Repository = in-memory DB, JsonStore = disk).
public class JsonStore {

    public static final Path DEFAULT_PATH = Path.of("data", "repo.json");

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    // ===== SAVE =====

    public static void save(Repository repo, Path path) throws IOException {
        RepositorySnapshot snapshot = toSnapshot(repo);
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        MAPPER.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), snapshot);
    }

    private static RepositorySnapshot toSnapshot(Repository repo) {
        RepositorySnapshot s = new RepositorySnapshot();

        for (Car c : repo.getAllCars()) {
            CarDto dto = new CarDto();
            dto.id = c.getId();
            dto.make = c.getMake();
            dto.model = c.getModel();
            dto.year = c.getYear();
            s.cars.add(dto);
        }

        for (Person p : repo.getAllPeople()) {
            PersonDto dto = new PersonDto();
            dto.id = p.getId();
            dto.name = p.getName();
            dto.age = p.getAge();
            dto.carId = idOf(p.getCar());
            dto.dogId = idOf(p.getDog());
            s.people.add(dto);
        }

        for (Dog d : repo.getAllDogs()) {
            DogDto dto = new DogDto();
            dto.id = d.getId();
            dto.name = d.getName();
            dto.breed = d.getBreed();
            dto.age = d.getAge();
            dto.ownerId = idOf(d.getOwner());
            s.dogs.add(dto);
        }

        for (CarTransaction t : repo.getAllCarTransactions()) {
            CarTransactionDto dto = new CarTransactionDto();
            dto.id = t.getId();
            dto.buyerId = idOf(t.getBuyer());
            dto.sellerId = idOf(t.getSeller());
            dto.carId = idOf(t.getCar());
            dto.date = t.getDate();
            dto.contract = t.getContract();
            s.carTransactions.add(dto);
        }

        for (DogAdoption a : repo.getAllDogAdoptions()) {
            DogAdoptionDto dto = new DogAdoptionDto();
            dto.id = a.getId();
            dto.adopterId = idOf(a.getAdopter());
            dto.dogId = idOf(a.getDog());
            dto.date = a.getDate();
            dto.fee = a.getFee();
            dto.contract = a.getContract();
            s.dogAdoptions.add(dto);
        }

        return s;
    }

    // ===== LOAD =====

    // Fills the given repo (which must be empty) from the JSON file.
    public static void load(Repository repo, Path path) throws IOException {
        RepositorySnapshot s = MAPPER.readValue(path.toFile(), RepositorySnapshot.class);
        fromSnapshot(s, repo);
    }

    private static void fromSnapshot(RepositorySnapshot s, Repository repo) {
        // Pass 1: create entities with their persisted IDs, index them by ID
        Map<String, Car> cars = new HashMap<>();
        for (CarDto dto : s.cars) {
            Car c = new Car(dto.make, dto.model, dto.year);
            c.setId(dto.id);
            cars.put(c.getId(), c);
            repo.addCar(c);
        }

        Map<String, Person> people = new HashMap<>();
        for (PersonDto dto : s.people) {
            Person p = new Person(dto.name, dto.age);
            p.setId(dto.id);
            people.put(p.getId(), p);
            repo.addPerson(p);
        }

        Map<String, Dog> dogs = new HashMap<>();
        for (DogDto dto : s.dogs) {
            Dog d = new Dog(dto.name, dto.breed, dto.age);
            d.setId(dto.id);
            dogs.put(d.getId(), d);
            repo.addDog(d);
        }

        // Pass 2: wire references so every entity is a single shared instance
        for (PersonDto dto : s.people) {
            Person p = people.get(dto.id);
            p.setCar(cars.get(dto.carId));
            p.setDog(dogs.get(dto.dogId));
        }

        for (DogDto dto : s.dogs) {
            dogs.get(dto.id).setOwner(people.get(dto.ownerId));
        }

        for (CarTransactionDto dto : s.carTransactions) {
            CarTransaction t = new CarTransaction(
                    people.get(dto.buyerId),
                    people.get(dto.sellerId),
                    dto.date,
                    cars.get(dto.carId),
                    dto.contract
            );
            t.setId(dto.id);
            repo.addCarTransaction(t);
        }

        for (DogAdoptionDto dto : s.dogAdoptions) {
            DogAdoption a = new DogAdoption(
                    people.get(dto.adopterId),
                    dogs.get(dto.dogId),
                    dto.date,
                    dto.fee,
                    dto.contract
            );
            a.setId(dto.id);
            repo.addDogAdoption(a);
        }
    }

    private static String idOf(Person p) { return p == null ? null : p.getId(); }
    private static String idOf(Car c)    { return c == null ? null : c.getId(); }
    private static String idOf(Dog d)    { return d == null ? null : d.getId(); }
}
