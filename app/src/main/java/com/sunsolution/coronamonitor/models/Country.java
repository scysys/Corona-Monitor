package com.sunsolution.coronamonitor.models;

public class Country {

    private String name;
    private String counter;
    private String flag;

    public Country(String name, String counter, String flag) {
        this.name = name;
        this.counter = counter;
        this.flag = flag;
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

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
