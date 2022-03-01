package org.example.controllers;

import org.example.entities.Conversion;
import org.example.repos.ConversionRepos;
import org.example.repos.CurrencyRepos;
import org.example.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ConversionController {

    @Autowired
    private CurrencyRepos currencyRepos;

    @Autowired
    private ConversionRepos conversionRepos;

    @Autowired
    CalculateService calculateService;

    @GetMapping("/convert")
    public String showConversionForm(Model model) {
        model.addAttribute("currencies", currencyRepos.findAll());
        model.addAttribute("conversion", new Conversion());
        model.addAttribute("conversions", conversionRepos.findAll());
        return "converter";
    }

    @PostMapping("/convert")
    public String convert(Conversion conversion) {
        double result = calculateService.getCalculationResult(conversion);

        return "redirect:/convert";
    }
}
