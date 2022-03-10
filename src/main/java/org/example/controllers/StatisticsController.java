package org.example.controllers;

import org.example.entities.AverageConversionCurrencyValues;
import org.example.repos.AverageConversionCurrencyValuesRepos;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StatisticsController {

    private final AverageConversionCurrencyValuesRepos averageConversionCurrencyValuesRepos;

    public StatisticsController(AverageConversionCurrencyValuesRepos averageConversionCurrencyValuesRepos) {
        this.averageConversionCurrencyValuesRepos = averageConversionCurrencyValuesRepos;
    }

    @GetMapping("/statistics")
    public List<AverageConversionCurrencyValues> getStatistics() {
        return averageConversionCurrencyValuesRepos.findAll();
    }
}
