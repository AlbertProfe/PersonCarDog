package org.example.model;

import java.util.UUID;

public class DogAdoption {
    private String id;
    private Person person;
    private Dog dog;
    private int fee;
    private String contract;

    public DogAdoption() {
    }

    public DogAdoption(Person person, Dog dog) {
        this.id = UUID.randomUUID().toString();
        this.person = person;
        this.dog = dog;

    }

}