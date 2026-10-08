package org.example.utils;

import com.github.javafaker.Faker;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Dog;
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

        // 1. Generar 3 Coches (Objetos 1, 2 y 3)
        for (int i = 0; i < 3; i++) {
            Car car = new Car(
                    faker.company().name(),
                    faker.commerce().productName(),
                    faker.number().numberBetween(1995, 2025)
            );
            repo.addCar(car);
            temporaryCars.add(car);
        }

        // 2. Generar 3 Personas (Objetos 4, 5 y 6)
        for (int i = 0; i < 3; i++) {
            Person person = new Person(
                    faker.name().fullName(),
                    faker.number().numberBetween(18, 80)
            );
            temporaryPeople.add(person);
        }

        // Para garantizar que Service.buyCar pueda ejecutarse con éxito:
        // - Persona 0 será un Vendedor Válido (Le asignamos el Coche 0)
        temporaryPeople.get(0).setCar(temporaryCars.get(0));

        // - Persona 1 será un Comprador Válido (Se queda SIN coche)
        // - Persona 2 le asignamos otro coche aleatorio
        temporaryPeople.get(2).setCar(temporaryCars.get(1));

        // Insertar las personas en el repositorio
        for (Person p : temporaryPeople) {
            repo.addPerson(p);
        }

        // 3. Generar 2 Perros (Objetos 7 y 8)
        for (int i = 0; i < 2; i++) {
            Dog dog = new Dog(
                    faker.dog().name(),
                    faker.dog().breed(),
                    faker.number().numberBetween(1, 15)
            );
            repo.addDog(dog);
        }

        // 4. Generar 2 Transacciones iniciales ficticias (Objetos 9 y 10)
        // Usamos al Coche 2 para las transacciones del historial previo
        CarTransaction tx1 = new CarTransaction(
                temporaryPeople.get(1), // comprador
                temporaryPeople.get(2), // vendedor
                new Date(),
                temporaryCars.get(2),
                faker.lorem().sentence()
        );
        repo.addCarTransaction(tx1);

        CarTransaction tx2 = new CarTransaction(
                temporaryPeople.get(2),
                temporaryPeople.get(0),
                new Date(),
                temporaryCars.get(2),
                faker.lorem().sentence()
        );
        repo.addCarTransaction(tx2);

        // Imprimir en consola la verificación de carga completa
        System.out.println("=== DATA SEEDING COMPLETE ===");
        System.out.println("Personas: 3 | Coches: 3 | Perros: 2 | Transacciones: 2");
        System.out.println("TOTAL DE INSTANCIAS NUEVAS EN EL REPOSITORIO: 10");
        System.out.println("=============================");
    }
}
