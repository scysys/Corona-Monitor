package com.sunsolution.coronamonitor.models;

public class City {

    private String name;
    private String counter;

    public City(String name, String counter) {
        this.name = name;
        this.counter = counter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
