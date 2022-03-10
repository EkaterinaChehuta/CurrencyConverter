package org.example.controllers;

import org.example.entities.Conversion;
import org.example.entities.History;
import org.example.repos.ConversionRepos;
import org.example.repos.HistoryRepos;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class HistoryController {

    private final ConversionRepos conversionRepos;
    private final HistoryRepos historyRepos;

    public HistoryController(ConversionRepos conversionRepos,
                             HistoryRepos historyRepos) {
        this.conversionRepos = conversionRepos;
        this.historyRepos = historyRepos;
    }

    @GetMapping("/history/{from_id}/{to_id}")
    public List<History> getHistory(@PathVariable("from_id") Integer currencyFromId,
                                    @PathVariable("to_id") Integer currencyToId) {
        List<Conversion> conversionList = conversionRepos.findAll();

        if (currencyFromId != 0) {
            conversionList.removeIf(conversion -> (conversion.getCurrencyFrom().getId() != currencyFromId));
        }

        if (currencyToId != 0) {
            conversionList.removeIf(conversion -> (conversion.getCurrencyTo().getId() != currencyToId));
        }

        List<History> historyList = new ArrayList<>();

        for (Conversion conversion : conversionList) {
            History history = historyRepos.findByConversion(conversion);
            if (history != null) {
                historyList.add(history);
            }
        }

        return historyList;
    }
}
