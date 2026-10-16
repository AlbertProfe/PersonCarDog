package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dog {

    private String id;
    private String name;
    private String breed;
    private int age;
    // Many-to-many: a dog can have multiple owners (shared custody via DogAdoption)
    private List<Person> owners = new ArrayList<>();

    public Dog() {}

    public Dog(String name, String breed, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.breed = breed;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
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

    public List<Person> getOwners() {
        return owners;
    }

    public void setOwners(List<Person> owners) {
        this.owners = owners != null ? owners : new ArrayList<>();
    }

    // Convenience for Service to maintain both sides safely
    public void addOwner(Person person) {
        if (person != null && !owners.contains(person)) {
            owners.add(person);
        }
    }

    public void removeOwner(Person person) {
        owners.remove(person);
    }

    @Override
    public String toString() {
        // Avoid recursion: list only owner names
        String ownersInfo = owners == null || owners.isEmpty() ? "none" :
                owners.stream().map(Person::getName).reduce((a, b) -> a + ", " + b).orElse("none");
        return "Dog{id='" + id + "', name='" + name + "', breed='" + breed + "', age=" + age + ", owners=[" + ownersInfo + "]}";
    }
}
