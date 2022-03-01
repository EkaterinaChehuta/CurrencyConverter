package org.example.repos;

import org.example.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CurrencyRepos extends JpaRepository<Currency, Integer> {
    Currency findByNumCodeAndCharCodeAndNameCurrency(String numCode, String charCode, String nameCurrency);
    Currency findByCharCode(String charCode);
}
