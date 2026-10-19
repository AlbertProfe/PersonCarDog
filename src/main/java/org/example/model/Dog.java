package org.example.model;

import java.util.ArrayList;
import java.util.UUID;

public class Dog {

    private String id;
    private String name;
    private String breed;
    private int age;
    private ArrayList<Person> owners;

    public Dog() {}

    public Dog(String name, String breed, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.breed = breed;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList getOwner() {
        return owners;
    }
    public void setOwner(Person person){
        if (this.owners == null){
            this.owners = new ArrayList<>();
            this.owners.add(person);
        } else if (this.owners.size()<2){
            if (this.owners.contains (person)){
                System.out.println("Dog yet has adopter by " + person );
            }
            this.owners.add(person);
        } else if (this.owners.size()>=2){
            System.out.println("Dog yet has 2 owners");
        }
    }
    public void removeOwner ( Person person){
        if (this.owners.contains (person)){
            this.owners.remove(person);
        }
    }

    @Override
    public String toString() {
        ArrayList<String> namesOwners = new ArrayList<>();
        for (Person i : this.owners){
            namesOwners.add(i.getName());
        }
        return "Dog{id='" + id + "', name='" + name + "', breed='" + breed + "', age=" + age +  "', owners='" + namesOwners + "'}";
    }
}
