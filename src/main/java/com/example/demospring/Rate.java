package com.example.demospring;

import javax.persistence.*;
@Entity
@Table(name = "Rate")
public class Rate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private double exchangeRate;

    public Rate(){};

    public Rate(String name, double exchangeRate){
        this.name = name;
        this.exchangeRate = exchangeRate;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", exchangeRate='" + exchangeRate + '\'' +
                '}';
    }
}

