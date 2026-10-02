package org.example.utils;

import java.util.Scanner;

public class Utils {

    public static String askMenuOption(Scanner scan){
        System.out.print("Select an option: ");
        String option = scan.nextLine();
        return option;
    }

    public static void mainMenu(){
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Person operations");
        System.out.println("2. Dog operations");
        System.out.println("3. Car operations");
        System.out.println("4. Buy a car (person to person)");
        System.out.println("5. Quit");

    }
}
