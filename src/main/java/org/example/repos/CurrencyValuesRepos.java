package org.example.repos;

import org.example.entities.Currency;
import org.example.entities.CurrencyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CurrencyValuesRepos extends JpaRepository<CurrencyValues, Integer> {
    CurrencyValues findByDateAndCurrency(LocalDate date, Currency currency);
    CurrencyValues findByCurrency(Currency currency);
}
