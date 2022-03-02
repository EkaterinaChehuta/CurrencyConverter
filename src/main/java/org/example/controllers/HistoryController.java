package org.example.controllers;

import org.example.entities.Conversion;
import org.example.entities.Currency;
import org.example.repos.ConversionRepos;
import org.example.repos.CurrencyRepos;
import org.example.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HistoryController {

    @Autowired
    private ConversionRepos conversionRepos;

    @Autowired
    private CurrencyRepos currencyRepos;

    @Autowired
    HistoryService historyService;

    @GetMapping("/history")
    public String historyFilter(@RequestParam(required = false) Integer currencyFromId,
                                @RequestParam(required = false) Integer currencyToId,
//                                @RequestParam(required = false) LocalDate dateFrom,
                                Model model) {
        List<Conversion> conversionList = conversionRepos.findAll();

        if (currencyFromId != null) {
            Currency currencyFrom = currencyRepos.getOne(currencyFromId);
            conversionList.removeIf(conversion -> (conversion.getCurrencyFrom().getId() != currencyFromId));
        }
        if (currencyToId != null) {
            Currency currencyTo = currencyRepos.getOne(currencyToId);
            conversionList.removeIf(conversion -> (conversion.getCurrencyTo().getId() != currencyToId));
        }

        model.addAttribute("conversions", conversionList);
        model.addAttribute("currencies", currencyRepos.findAll());

        return "history";
    }
}
