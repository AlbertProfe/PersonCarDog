package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Person {

    private String id;
    private String name;
    private int age;
    private Car car;
    // Many-to-many: a person can have multiple dogs (shared custody via DogAdoption)
    private List<Dog> dogs = new ArrayList<>();

    public Person(){
        this.id = UUID.randomUUID().toString();
    }

    public Person(String name, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs != null ? dogs : new ArrayList<>();
    }

    // Convenience for Service to maintain both sides safely
    public void addDog(Dog dog) {
        if (dog != null && !dogs.contains(dog)) {
            dogs.add(dog);
        }
    }

    public void removeDog(Dog dog) {
        dogs.remove(dog);
    }

    @Override
    public String toString() {
        // Avoid recursion: list only dog ids/names
        String dogInfo = dogs == null || dogs.isEmpty() ? "none" :
                dogs.stream().map(d -> d.getName() + "(" + d.getId().substring(0, 4) + ")").reduce((a, b) -> a + ", " + b).orElse("none");
        return "Person{id='" + id + "', name='" + name + "', age=" + age + ", dogs=[" + dogInfo + "]}";
    }
}
