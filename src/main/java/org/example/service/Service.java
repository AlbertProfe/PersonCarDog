package org.example.service;

import org.example.repository.Repository;
import org.example.model.Car;
import org.example.model.CarTransaction;
import org.example.model.Person;

import java.util.Date;

public class Service {


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
