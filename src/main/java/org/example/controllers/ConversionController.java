package org.example.controllers;

import org.example.repos.CurrencyRepos;
import org.example.repos.CurrencyValuesRepos;
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
    private CurrencyValuesRepos currencyValuesRepos;

    @GetMapping("/convert")
    public String showConversionForm(Model model) {
        model.addAttribute("currencies", currencyRepos.findAll());
        return "index";
    }

    @PostMapping("/convert")
    public String convert() {
        return "redirect:/";
    }
}
