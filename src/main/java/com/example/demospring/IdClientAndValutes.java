package com.example.demospring;

import javax.persistence.*;

@Entity
@Table(name = "ClientBalances")
public class IdClientAndValutes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(nullable = false)
    private String owner;

    private double uan;

    private double usd;

    private double eur;
    @Transient
    private static final double USD_TO_UAN_RATE = 27.5;
    @Transient
    private static final double EUR_TO_UAN_RATE = 40.0;

    public IdClientAndValutes() {
    }

    public IdClientAndValutes(String owner, double uan, double usd, Double eur) {
        this.owner = owner;
        this.uan = uan;
        this.usd = usd;
        this.eur = eur;
    }

    public long getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getUan() {
        return uan;
    }

    public void setUan(double uan) {
        this.uan = uan;
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getEur() {
        return eur;
    }

    public void setEur(double eur) {
        this.eur = eur;
    }

    public double convertCurrencyToUan(String currency, double amount) {
        switch (currency.toLowerCase()) {
            case "uan":
                return amount;
            case "usd":
                return amount * USD_TO_UAN_RATE;
            case "eur":
                return amount * EUR_TO_UAN_RATE;
            default:
                throw new IllegalArgumentException("Invalid currency");
        }
    }
    @Override
    public String toString() {
        return "IdClientAndValutes{" +
                "id=" + id +
                ", owner='" + owner + '\'' +
                ", uan=" + uan +
                ", usd=" + usd +
                ", eur=" + eur +
                '}';
    }
}
