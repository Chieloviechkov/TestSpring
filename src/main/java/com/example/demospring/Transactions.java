package com.example.demospring;

import javax.persistence.*;

@Entity
@Table(name = "Operation")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id ;

    private String giver;

    private String receiver;

    private double amount;

    private String currency;

    private double amount2;

    private String currency2;

    public Transactions(){}

    public Transactions(String giver, String receiver, double amount, String currency){
        this.giver = giver;
        this.receiver = receiver;
        this.amount = amount;
        this.currency = currency;
    }

    public Transactions(String receiver, double amount, String currency){
        this.receiver = receiver;
        this.amount = amount;
        this.currency = currency;
    }

    public Transactions(String receiver, double amount, String currency, double amount2, String currency2){
        this.receiver = receiver;
        this.amount = amount;
        this.currency = currency;
        this.amount2 = amount2;
        this.currency2 = currency2;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGiver() {
        return giver;
    }

    public void setGiver(String giver) {
        this.giver = giver;
    }

    @Override
    public String toString() {
        return "Operation{" +
                "id=" + id +
                ", giver='" + giver + '\'' +
                ", receiver='" + receiver + '\'' +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", amount2=" + amount2 +
                ", currency2='" + currency2 + '\'' +
                '}';
    }
}