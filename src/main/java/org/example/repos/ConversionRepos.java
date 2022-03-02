package org.example.repos;

import org.example.entities.Conversion;
import org.example.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConversionRepos extends JpaRepository<Conversion, Integer> {
    List<Conversion> findByCurrencyFrom(Currency currencyFrom);
    List<Conversion> findByCurrencyTo(Currency currencyTo);
    List<Conversion> findByCurrencyFromAndCurrencyTo(Currency currencyFrom, Currency currencyTo);
    List<Conversion> findByCurrencyFromAndCurrencyToAndDateBetween(Currency currencyFrom, Currency currencyTo,
                                                                   LocalDate dateFrom, LocalDate dateTo);
}
