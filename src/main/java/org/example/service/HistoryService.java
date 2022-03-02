package org.example.service;

import org.example.entities.Conversion;
import org.example.entities.Currency;
import org.example.entities.History;
import org.example.repos.ConversionRepos;
import org.example.repos.HistoryRepos;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HistoryService {

    private final HistoryRepos historyRepos;
    private final ConversionRepos conversionRepos;

    public HistoryService(HistoryRepos historyRepos, ConversionRepos conversionRepos) {
        this.historyRepos = historyRepos;
        this.conversionRepos = conversionRepos;
    }

    // Расчет среднего курса конвертации для пары валют
    public List<Double> getAverageConversionCurrencyValue(Currency currencyFrom, Currency currencyTo) {
        // Получили список конвертаций для пары валют
        List<Conversion> conversionList = conversionRepos.findByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);

        double sumFrom = 0;
        double sumTo = 0;
        int count = 0;
        History history;
        // Проход по списку конвертаций
        // Запрос в базу на получение истории по действию
        // Сумирование ставок
        for (Conversion conversion : conversionList) {
            history = historyRepos.findByConversion(conversion);
            sumFrom += history.getCurrencyValuesFrom().getRubValue();
            sumTo += history.getCurrencyValuesTo().getRubValue();
            count++;
        }

        List<Double> results = new ArrayList<>(); // todo заменить на сущность
        results.add(sumFrom / count);
        results.add(sumTo / count);

        return results;
    }

    // Расчет суммарного объёма конвертаций для пары валют за неделю
    public List<Double> getWeeklySumConversionCurrencyValue(Currency currencyFrom,
                                                             Currency currencyTo,
                                                             LocalDate dateFrom) {
        LocalDate dateTo = dateFrom.plusDays(7); //todo может упасть если от стартовой даты до текущей нет 7 дней
        List<Conversion> conversionList = conversionRepos.findByCurrencyFromAndCurrencyToAndDateBetween
                (currencyFrom, currencyTo, dateFrom, dateTo);

        double sumFrom = 0;
        double sumTo = 0;
        History history;
        // Проход по списку конвертаций
        // Запрос в базу на получение истории по действию
        // Сумирование ставок
        for (Conversion conversion : conversionList) {
            history = historyRepos.findByConversion(conversion);
            sumFrom += history.getCurrencyValuesFrom().getRubValue();
            sumTo += history.getCurrencyValuesTo().getRubValue();
        }

        List<Double> results = new ArrayList<>(); // todo заменить на сущность
        results.add(sumFrom);
        results.add(sumTo);

        return results;
    }
}
