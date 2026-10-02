package org.example;

import java.util.UUID;

public class Person {

    private String id;
    private String name;
    private int age;
    private Car car;

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

    @Override
    public String toString() {
        return "Person{id='" + id + "', name='" + name + "', age=" + age + "}";
    }
}
