package org.example.repos;

import org.example.entities.AverageConversionCurrencyValues;
import org.example.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AverageConversionCurrencyValuesRepos extends JpaRepository<AverageConversionCurrencyValues, Integer> {
    AverageConversionCurrencyValues findByCurrencyOneAndCurrencyTwo(Currency currencyOne, Currency currencyTwo);
}
