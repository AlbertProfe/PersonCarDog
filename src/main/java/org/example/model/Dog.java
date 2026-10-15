package org.example.model;

import java.util.UUID;

public class Dog {

    private String id;
    private String name;
    private String breed;
    private int age;
    // Back-reference to the adopter (bidirectional with Person.dog)
    private Person owner;

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

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        // Print only the owner's name, never the full Person, to avoid infinite recursion
        String ownerName = (owner == null) ? "none" : owner.getName();
        return "Dog{id='" + id + "', name='" + name + "', breed='" + breed + "', age=" + age + ", owner='" + ownerName + "'}";
    }
}
