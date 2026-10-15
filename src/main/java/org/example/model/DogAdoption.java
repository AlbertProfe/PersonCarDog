package org.example.model;

import java.util.Date;
import java.util.UUID;

public class DogAdoption {
    private String id;
    private Person adopter;
    private Dog dog;
    private Date date;
    private int fee;
    private String contract;

    public DogAdoption(){}

    public DogAdoption(Person person, Dog dog, Date date, int fee, String contract) {
        this.id = UUID.randomUUID().toString();
        this.adopter = person;
        this.date = date;
        this.dog = dog;
        this.contract = contract;
        this.fee = fee;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Person getAdopter() {
        return adopter;
    }

    public void setAdopter(Person person) {
        this.adopter = person;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public int getFee() {
        return fee;
    }
    public  void setFee(int fee) {
        this.fee = fee;
    }

    @Override
    public String toString() {
        return "DogAdoption{id='" + id + "', adopter=" + adopter + ", dog=" +
                dog + ", date=" + date + ", fee=" + fee + ", contract='" + contract + "'}";
    }


}
