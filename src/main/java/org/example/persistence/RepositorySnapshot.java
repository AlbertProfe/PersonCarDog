package org.example.persistence;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Flat, JSON-friendly view of the Repository.
// Object references are stored as IDs (like foreign keys), so there are no cycles
// and no duplicated Person/Car/Dog objects inside transactions or adoptions.
public class RepositorySnapshot {

    public List<PersonDto> people = new ArrayList<>();
    public List<CarDto> cars = new ArrayList<>();
    public List<DogDto> dogs = new ArrayList<>();
    public List<CarTransactionDto> carTransactions = new ArrayList<>();
    public List<DogAdoptionDto> dogAdoptions = new ArrayList<>();

    public static class PersonDto {
        public String id;
        public String name;
        public int age;
        public String carId;
        public String dogId;
    }

    public static class CarDto {
        public String id;
        public String make;
        public String model;
        public int year;
    }

    public static class DogDto {
        public String id;
        public String name;
        public String breed;
        public int age;
        public String ownerId;
    }

    public static class CarTransactionDto {
        public String id;
        public String buyerId;
        public String sellerId;
        public String carId;
        public Date date;
        public String contract;
    }

    public static class DogAdoptionDto {
        public String id;
        public String adopterId;
        public String dogId;
        public Date date;
        public int fee;
        public String contract;
    }
}
