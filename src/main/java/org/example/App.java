package org.example;

import java.util.Scanner;


public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello to Transaction Cars Person to Person!" );

        // Person a = new Person();
        // Person b = new Person();
        // Car bmw = new Car();
        // Repository repo = new Repository();
        // we need to set all objects ...

        Scanner scan = new Scanner(System.in);

        while (true) {
            mainMenu();

            String option = askMenuOption(scan);

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
                    // call to buy
                    break;
                case "5":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
    }

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
