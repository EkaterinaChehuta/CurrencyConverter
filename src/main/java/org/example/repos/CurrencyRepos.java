package org.example.repos;

import org.example.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepos extends JpaRepository<Currency, Integer> {
    Currency findByNumCodeAndCharCodeAndNameCurrency(String numCode, String charCode, String nameCurrency);
}
