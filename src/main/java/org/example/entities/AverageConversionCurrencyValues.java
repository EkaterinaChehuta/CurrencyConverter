package org.example.entities;

import javax.persistence.*;

@Entity
public class AverageConversionCurrencyValues {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = Currency.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_one_id", referencedColumnName = "id")
    private Currency currencyOne;

    @OneToOne(targetEntity = Currency.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_two_id", referencedColumnName = "id")
    private Currency currencyTwo;

    @Column(name = "result_currency_one")
    private double resultCurrencyOne;

    @Column(name = "result_currency_two")
    private double resultCurrencyTwo;

    private int count;

    public AverageConversionCurrencyValues() {
    }

    public AverageConversionCurrencyValues(int id, Currency currencyOne, Currency currencyTwo, double resultCurrencyOne, double resultCurrencyTwo, int count) {
        this.id = id;
        this.currencyOne = currencyOne;
        this.currencyTwo = currencyTwo;
        this.resultCurrencyOne = resultCurrencyOne;
        this.resultCurrencyTwo = resultCurrencyTwo;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Currency getCurrencyOne() {
        return currencyOne;
    }

    public void setCurrencyOne(Currency currencyOne) {
        this.currencyOne = currencyOne;
    }

    public Currency getCurrencyTwo() {
        return currencyTwo;
    }

    public void setCurrencyTwo(Currency currencyTwo) {
        this.currencyTwo = currencyTwo;
    }

    public double getResultCurrencyOne() {
        return resultCurrencyOne;
    }

    public void setResultCurrencyOne(double resultCurrencyOne) {
        this.resultCurrencyOne = resultCurrencyOne;
    }

    public double getResultCurrencyTwo() {
        return resultCurrencyTwo;
    }

    public void setResultCurrencyTwo(double resultCurrencyTwo) {
        this.resultCurrencyTwo = resultCurrencyTwo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
