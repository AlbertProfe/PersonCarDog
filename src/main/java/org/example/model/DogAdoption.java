package org.example.model;

import java.util.Date;
import java.util.UUID;

public class DogAdoption {

    public enum Status {
        Active,
        Returned
    }

    private String id;
    private Person adopter;
    private Dog dog;
    private Date date;
    private Long fee;
    private String contract;
    private Status status;

    public DogAdoption(){}

    public DogAdoption(Person adopter, Dog dog, Date date, Long fee, String contract, Status status){
        this.id = UUID.randomUUID().toString();
        this.adopter = adopter;
        this.dog = dog;
        this.date = date;
        this.fee = fee;
        this.contract = contract;
        this.status = status;
    }

    // Getters y Setters para ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getters y Setters para Adopter
    public Person getAdopter() {
        return adopter;
    }

    public void setAdopter(Person adopter) {
        this.adopter = adopter;
    }

    // Getters y Setters para Dog
    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    // Getters y Setters para Date
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Getters y Setters para Fee
    public Long getFee() {
        return fee;
    }

    public void setFee(Long fee) {
        this.fee = fee;
    }

    // Getters y Setters para Contract
    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    // Getters y Setters para Status
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Adoption{" +
                "id='" + id + '\'' +
                ", adopter=" + (adopter != null ? adopter.getName() : "null") + // Evita problemas si el adoptante es null
                ", dog=" + (dog != null ? dog.getName() : "null") +       // Evita problemas si el perro es null
                ", date=" + date +
                ", fee=" + fee +
                ", contract='" + contract + '\'' +
                ", status=" + status +
                '}';
    }
}
