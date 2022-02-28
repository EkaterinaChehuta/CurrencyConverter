package org.example.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "currency_values")
public class CurrencyValues {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private double rubValue;

    @Column(name = "value_date")
    private LocalDate date;

    @OneToOne(targetEntity = Currency.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    public CurrencyValues() {
    }

    public CurrencyValues(double rubValue, LocalDate date, Currency currency) {
        this.rubValue = rubValue;
        this.date = date;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRubValue() {
        return rubValue;
    }

    public void setRubValue(double rubValue) {
        this.rubValue = rubValue;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}
