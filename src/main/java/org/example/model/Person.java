package org.example.model;

import java.util.ArrayList;
import java.util.UUID;

public class Person {

    private String id;
    private String name;
    private int age;
    private Car car;
    private ArrayList<Dog> dog;

    public Person(){
        this.id = UUID.randomUUID().toString();
    }

    public Person(String name, int age) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public ArrayList getDog(){
        return this.dog;
    }

    public  void setDog(Dog dog){
        if (this.dog == null){
            this.dog = new ArrayList<>();
            this.dog.add(dog);
        } else if (this.dog.size()<3){
            if (this.dog.contains (dog)){
                System.out.println("Person yet has " + dog );
            }
            this.dog.add(dog);
        } else if (this.dog.size()>=3){
            System.out.println(" Person yet has 3 dogs");
        }
    }
    public void removeDog( Dog dog){
        if (this.dog.contains(dog)){
            this.dog.remove(dog);
        }
    }
    @Override
    public String toString() {
        ArrayList<String> namesDog = new ArrayList<>();
        for (Dog i : this.dog){
            namesDog.add(i.getName());
        }
        return "Person{id='" + id + "', name='" + name + "', age=" + age + "', dog" + namesDog+ "}";
    }
}
