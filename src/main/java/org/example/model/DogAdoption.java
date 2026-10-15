package org.example.model;

import java.util.Date;
import java.util.UUID;

// Read-only record created as a side effect of Service.adoptDog (same role as CarTransaction)
public class DogAdoption {

    private String id;
    private Person adopter;
    private Dog dog;
    private Date date;
    private int fee;
    private String contract;

    public DogAdoption() {}

    public DogAdoption(Person adopter, Dog dog, Date date, int fee, String contract) {
        this.id = UUID.randomUUID().toString();
        this.adopter = adopter;
        this.dog = dog;
        this.date = date;
        this.fee = fee;
        this.contract = contract;
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

    public void setAdopter(Person adopter) {
        this.adopter = adopter;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    @Override
    public String toString() {
        return "DogAdoption{id='" + id + "', adopter=" + adopter + ", dog=" + dog + ", date=" + date + ", fee=" + fee + ", contract='" + contract + "'}";
    }
}
