package org.example;

import java.util.Date;

public class CarTransaction {

    private String id;
    private Person buyer;
    private Person seller;
    private Date date;
    private Car car;
    private String contract;

    public CarTransaction(String id, Person buyer, Person seller, Date date, Car car, String contract) {
        this.id = id;
        this.buyer = buyer;
        this.seller = seller;
        this.date = date;
        this.car = car;
        this.contract = contract;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Person getBuyer() {
        return buyer;
    }

    public void setBuyer(Person buyer) {
        this.buyer = buyer;
    }

    public Person getSeller() {
        return seller;
    }

    public void setSeller(Person seller) {
        this.seller = seller;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    @Override
    public String toString() {
        return "CarTransaction{id='" + id + "', buyer=" + buyer + ", seller=" + seller + ", date=" + date + ", car=" + car + ", contract='" + contract + "'}";
    }
}
