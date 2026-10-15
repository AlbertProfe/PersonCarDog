package org.example.model;

import java.util.UUID;

public class Dog {

    private String id;
    private String name;
    private String breed;
    private int age;
    private Person person;

    public Dog() {}

    public Dog(String name, String breed, int age, Person person) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.breed = breed;
        this.age = age;
        this.person=person;
    }

    public String getName() {
        return name;
    }

    public Person getPerson() {
        return person;
    }


    public void setName(String name) {
        this.name = name;
    }
    public void setPerson(Person person) {
        this.person = person;
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

    @Override
    public String toString() {
        return "Dog{id='" + id + "', name='" + name + "', breed='" + breed + "', age=" + age + "}";
    }
}
