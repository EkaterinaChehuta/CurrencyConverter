package org.example.service;

import org.example.entities.Conversion;
import org.example.entities.Currency;
import org.example.entities.CurrencyValues;
import org.example.entities.History;
import org.example.repos.ConversionRepos;
import org.example.repos.CurrencyRepos;
import org.example.repos.CurrencyValuesRepos;
import org.example.repos.HistoryRepos;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CalculateService {

    private final CurrencyRepos currencyRepos;
    private final CurrencyValuesRepos currencyValuesRepos;
    private final ConversionRepos conversionRepos;
    private final HistoryRepos historyRepos;


    public CalculateService(CurrencyRepos currencyRepos, CurrencyValuesRepos currencyValuesRepos, ConversionRepos conversionRepos, HistoryRepos historyRepos) {
        this.currencyRepos = currencyRepos;
        this.currencyValuesRepos = currencyValuesRepos;
        this.conversionRepos = conversionRepos;
        this.historyRepos = historyRepos;
    }

    public double getCalculationResult(Conversion conversion) {
        Currency currencyFrom = currencyRepos.getOne(conversion.getCurrencyFrom().getId());
        int nominalFrom = currencyFrom.getNominal();
        Currency currencyTo = currencyRepos.getOne(conversion.getCurrencyTo().getId());
        int nominalTo = currencyTo.getNominal();

        LocalDate date = LocalDate.now();

        // Запрос в БД на получение актуального курса на текущую дату
        CurrencyValues currencyValuesFrom = currencyValuesRepos.findByDateAndCurrency(date, currencyFrom);
        CurrencyValues currencyValuesTo = currencyValuesRepos.findByDateAndCurrency(date, currencyTo);

        // Проверка на рубль и получение ставки
        if (currencyFrom.getCharCode().equals("RUB")) {
            currencyValuesFrom = currencyValuesRepos.findByCurrency(currencyTo).get(0);
        }
        if (currencyTo.getCharCode().equals("RUB")) {
            currencyValuesTo = currencyValuesRepos.findByCurrency(currencyFrom).get(0);
        }

        // Если данные не актуальны
        if (currencyValuesFrom == null || currencyValuesTo == null) {
            try {
                // Получаем дату из базы
                LocalDate localDateFromData = currencyValuesRepos.findTopByOrderByDateDesc().getDate();
                // Получаем дату с сайта
                LocalDate localDateFromDocument = CurrencyService.getDateFromDocument(CurrencyService.getDocument());
                // Если не совпадают, то заполняем базу актуальными данными
                if (!localDateFromData.equals(localDateFromDocument)) {
                    CurrencyService.parse(currencyRepos, currencyValuesRepos);
                }

                // Запрос в БД на получение курса
                if (currencyValuesFrom == null) {
                    currencyValuesFrom = currencyValuesRepos.findByDateAndCurrency(localDateFromDocument, currencyFrom);
                }
                if (currencyValuesTo == null) {
                    currencyValuesTo = currencyValuesRepos.findByDateAndCurrency(localDateFromDocument, currencyTo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int amount = conversion.getAmount();
        double result;

        // Расчет результата конвертации
        if (currencyFrom.getCharCode().equals("RUB")) {
            result = amount / (currencyValuesTo.getRubValue() / nominalTo);
        } else if (currencyTo.getCharCode().equals("RUB")) {
            result = amount * (currencyValuesFrom.getRubValue() / nominalFrom);
        } else {
            result = ((currencyValuesFrom.getRubValue() / nominalFrom) * amount)
                    / (currencyValuesTo.getRubValue() / nominalTo);
        }

        // Запись запроса в базу
        conversion.setResult(result);
        conversion.setDate(LocalDate.now());  //todo заменить на timestamp?
        conversionRepos.save(conversion);

        // Запись истории запроса в базу с учетом ставки по которой производилась конвертация
        History history = new History();
        history.setConversion(conversion);
        history.setCurrencyValuesFrom(currencyValuesFrom);
        history.setCurrencyValuesTo(currencyValuesTo);
        historyRepos.save(history);

        return result;
    }
}
