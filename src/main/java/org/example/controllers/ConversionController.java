package org.example.controllers;

import org.example.entities.Conversion;
import org.example.entities.Currency;
import org.example.repos.ConversionRepos;
import org.example.repos.CurrencyRepos;
import org.example.service.CalculateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ConversionController {

    private final CurrencyRepos currencyRepos;
    private final ConversionRepos conversionRepos;
    private final CalculateService calculateService;

    public ConversionController(CurrencyRepos currencyRepos,
                                ConversionRepos conversionRepos,
                                CalculateService calculateService) {
        this.currencyRepos = currencyRepos;
        this.conversionRepos = conversionRepos;
        this.calculateService = calculateService;
    }

    @GetMapping("/currency")
    public List<Currency> findAllCurrencies() {
        return currencyRepos.findAll();
    }

    @GetMapping("/convert")
    public List<Conversion> findAllConversions() {
        return conversionRepos.findAll();
    }

    @PostMapping("/convert")
    public Conversion convert(@RequestBody Conversion conversion) {
        calculateService.getCalculationResult(conversion);
        return conversionRepos.getOne(conversion.getId());
    }
}
