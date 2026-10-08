package org.example.service;
import java.util.ArrayList;
import org.example.repository.Repository;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Person;

import java.util.Date;
import java.util.ArrayList;
import java.util.ArrayList;
public class Service {

    public Person createPerson(String name, int age, Repository repo){
        if (name == null||name.isEmpty()){
            System.out.println("Error");
            return null;
        }
        if (age<0||age>110){
            System.out.println("Error");
            return null;
        } else {
            Person person = new Person (name, age);
            repo.addPerson(person);
            System.out.println("Person created!");
            return person;

        }
    }

    public Person getPersonById(String id, Repository repo){

        if (id==null||id.isEmpty()){
            System.out.println("Error");
            return null;
        }else {
            return repo.getPersonById(id);
        }
    }

    public ArrayList<Person> getAllPeople(Repository repo){
        if (repo.getAllPeople() != null && !repo.getAllPeople().isEmpty()) {
            return repo.getAllPeople();
        }else {
            return null;
        }
    }

    public boolean deletePerson(String id, Repository repo){
        if (id != null && !id.isEmpty()){
            Person person = getPersonById(id,repo);

            if (person != null){
            return repo.removePerson(id);
            } else {
                return false;
            }
        } else {
            return false;
        }

    }




    public static boolean buyCar(Person buyer, Person seller, int price, Repository repo){

        System.out.println("Welcome to BUY MENU");
        // person buyer a exists at array
        if (!repo.getAllPeople().contains(buyer)) {
            System.out.println("Buyer not found in repository.");
            return false;
        }
        System.out.println("Buyer exits: " + buyer);

        // person seller b exists at array
        if (!repo.getAllPeople().contains(seller)) {
            System.out.println("Seller not found in repository.");
            return false;
        }
        System.out.println("Seller exits: " + seller);

        // car exists??
        if (seller.getCar() == null) {
            System.out.println("Seller does not have a car to sell.");
            return false;
        }

        // person a has a car
        Car car = seller.getCar();
        System.out.println("Seller can sell a car: "  + car);

        // person b does not have a car
        if (buyer.getCar() != null) {
            System.out.println("Buyer already has a car.");
            return false;
        }

        System.out.println("Buyer can buy a car.");

        // personB.setCar (bmw)
        buyer.setCar(car);
        // personA.car = null
        seller.setCar(null);

        System.out.println("Settings done, now creating CarTransaction ...");

        // create object CarTransaction
        CarTransaction transaction = new CarTransaction(buyer, seller, new Date(), car, "Car sold for " + price);
        // save object CarTransaction at repo
        repo.addCarTransaction(transaction);

        // PRINT
        System.out.println(transaction);

        return true;
    }
}
