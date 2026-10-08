package org.example.utils;

import java.util.Scanner;

public class Utils {

    public static String askMenuOption(Scanner scan){
        System.out.print("Select an option: ");
        String option = scan.nextLine();
        return option;
    }

    public static String askString(Scanner scan, String prompt) {
        System.out.print(prompt);
        return scan.nextLine();
    }

    public static int askInt(Scanner scan, String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scan.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void mainMenu(){
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Person operations");
        System.out.println("2. Dog operations");
        System.out.println("3. Car operations");
        System.out.println("4. Buy a car (person to person)");
        System.out.println("5. View car transactions (read only)");
        System.out.println("6. Quit");
    }

    public static void personMenu() {
        System.out.println("\n===== PERSON MENU =====");
        System.out.println("1. Create person");
        System.out.println("2. List all people");
        System.out.println("3. Find person by ID");
        System.out.println("4. Update person");
        System.out.println("5. Delete person");
        System.out.println("6. Back");
    }

    public static void dogMenu() {
        System.out.println("\n===== DOG MENU =====");
        System.out.println("1. Create dog");
        System.out.println("2. List all dogs");
        System.out.println("3. Find dog by ID");
        System.out.println("4. Update dog");
        System.out.println("5. Delete dog");
        System.out.println("6. Back");
    }

    public static void carMenu() {
        System.out.println("\n===== CAR MENU =====");
        System.out.println("1. Create car");
        System.out.println("2. List all cars");
        System.out.println("3. Find car by ID");
        System.out.println("4. Update car");
        System.out.println("5. Delete car");
        System.out.println("6. Back");
    }

    public static void carTransactionMenu() {
        System.out.println("\n===== CAR TRANSACTIONS (READ ONLY) =====");
        System.out.println("1. List all transactions");
        System.out.println("2. Find transaction by ID");
        System.out.println("3. Back");
    }
}
