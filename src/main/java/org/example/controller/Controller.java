package org.example.controller;

import org.example.utils.Utils;
import org.example.model.Car;
import org.example.model.Person;
import org.example.repository.Repository;
import org.example.service.Service;

import java.util.Scanner;

public class Controller {

    public static void run() {

        System.out.println( "Hello to Transaction Cars Person to Person!" );

        Person a = new Person();
        Person b = new Person();
        Car bmw = new Car();
        Repository repo = new Repository();
        // we need to set all objects ...
        repo.addPerson(a);
        repo.addPerson(b);
        repo.addCar(bmw);
        a.setCar(bmw);

        Scanner scan = new Scanner(System.in);

        while (true) {
            Utils.mainMenu();

            String option = Utils.askMenuOption(scan);

            switch (option) {
                case "1":
                    System.out.println("Person - not implemented yet.");
                    break;
                case "2":
                    System.out.println("Dog - not implemented yet.");
                    break;
                case "3":
                    System.out.println("Car - not implemented yet.");
                    break;
                case "4":
                    Service.buyCar(b, a , 100, repo);
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }
}
