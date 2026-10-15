package org.example.model;

import java.util.UUID;

public class Person {

    private String id;
    private String name;
    private int age;
    private Car car;
    private Dog dog;

    public Person(){
        this.id = UUID.randomUUID().toString();
    }
    public Person(String name, int age){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, Dog dog) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.age = age;
        this.dog= dog;
    }

    public String getName() {
        return name;
    }

    public Dog getDog() {
        return dog;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
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

    @Override
    public String toString() {
        return "Person{id='" + id + "', name='" + name + "', age=" + age + "}";
    }
}
