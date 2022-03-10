package org.example.entities;

import javax.persistence.*;

@Entity
@Table(name = "history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(targetEntity = Conversion.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "conversion_id", referencedColumnName = "id")
    private Conversion conversion;

    @OneToOne(targetEntity = CurrencyValues.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_values_from_id", referencedColumnName = "id")
    private CurrencyValues currencyValuesFrom;

    @OneToOne(targetEntity = CurrencyValues.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_values_to_id", referencedColumnName = "id")
    private CurrencyValues currencyValuesTo;

    public History() {
    }

    public History(int id, Conversion conversion, CurrencyValues currencyValuesFrom, CurrencyValues currencyValuesTo) {
        this.id = id;
        this.conversion = conversion;
        this.currencyValuesFrom = currencyValuesFrom;
        this.currencyValuesTo = currencyValuesTo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Conversion getConversion() {
        return conversion;
    }

    public void setConversion(Conversion conversion) {
        this.conversion = conversion;
    }

    public CurrencyValues getCurrencyValuesFrom() {
        return currencyValuesFrom;
    }

    public void setCurrencyValuesFrom(CurrencyValues currencyValuesFrom) {
        this.currencyValuesFrom = currencyValuesFrom;
    }

    public CurrencyValues getCurrencyValuesTo() {
        return currencyValuesTo;
    }

    public void setCurrencyValuesTo(CurrencyValues currencyValuesTo) {
        this.currencyValuesTo = currencyValuesTo;
    }
}
