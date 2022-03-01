package org.example.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "conversion")
public class Conversion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = Currency.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_from_id", referencedColumnName = "id")
    private Currency currencyFrom;

    @OneToOne(targetEntity = Currency.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_to_id", referencedColumnName = "id")
    private Currency currencyTo;

    @Column(name = "value_date")
    private LocalDate date;

    private int amount; //todo заменить на большие числа
    private double result; //todo заменить на большие числа

    public Conversion() {
    }

    public Conversion(int id, Currency currencyFrom, Currency currencyTo, LocalDate date, int amount, double result) {
        this.id = id;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.date = date;
        this.amount = amount;
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(Currency currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public Currency getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(Currency currencyTo) {
        this.currencyTo = currencyTo;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
