package org.example.service;

import org.example.entities.*;
import org.example.repos.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculateService {

    private final CurrencyRepos currencyRepos;
    private final CurrencyValuesRepos currencyValuesRepos;
    private final ConversionRepos conversionRepos;
    private final HistoryRepos historyRepos;
    private final AverageConversionCurrencyValuesRepos averageConversionCurrencyValuesRepos;


    public CalculateService(CurrencyRepos currencyRepos, CurrencyValuesRepos currencyValuesRepos, ConversionRepos conversionRepos, HistoryRepos historyRepos, AverageConversionCurrencyValuesRepos averageConversionCurrencyValuesRepos) {
        this.currencyRepos = currencyRepos;
        this.currencyValuesRepos = currencyValuesRepos;
        this.conversionRepos = conversionRepos;
        this.historyRepos = historyRepos;
        this.averageConversionCurrencyValuesRepos = averageConversionCurrencyValuesRepos;
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
            currencyValuesFrom = currencyValuesRepos.findByCurrency(currencyTo);
        }
        if (currencyTo.getCharCode().equals("RUB")) {
            currencyValuesTo = currencyValuesRepos.findByCurrency(currencyFrom);
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
        conversion.setDate(LocalDate.now());
        conversionRepos.save(conversion);

        // Запись истории запроса в базу с учетом ставки по которой производилась конвертация
        History history = new History();
        history.setConversion(conversion);
        history.setCurrencyValuesFrom(currencyValuesFrom);
        history.setCurrencyValuesTo(currencyValuesTo);
        historyRepos.save(history);

        averageConversionCurrencyValue(currencyFrom, currencyTo, currencyValuesFrom, currencyValuesTo);

        return result;
    }

    // Расчет среднего курса конвертации
    public void averageConversionCurrencyValue(Currency currencyOne, Currency currencyTwo, CurrencyValues currencyValuesOne, CurrencyValues currencyValuesTwo) {
        //Пытаемся получить пару валют из базы
        AverageConversionCurrencyValues averageConversionCurrencyValues = averageConversionCurrencyValuesRepos.findByCurrencyOneAndCurrencyTwo(currencyOne, currencyTwo);

        double resultCurrencyOne;
        double resultCurrencyTwo;
        int count;
        AverageConversionCurrencyValues getAverageConversionCurrencyValues;

        // Если пара найдена
        if (averageConversionCurrencyValues != null) {
            // Получаем значения и пересчитываем
            count = averageConversionCurrencyValues.getCount() + 1;
            resultCurrencyOne = (averageConversionCurrencyValues.getResultCurrencyOne() + currencyValuesOne.getRubValue()) / count;
            resultCurrencyTwo = (averageConversionCurrencyValues.getResultCurrencyTwo() + currencyValuesTwo.getRubValue()) / count;
            getAverageConversionCurrencyValues = averageConversionCurrencyValuesRepos.getOne(averageConversionCurrencyValues.getId());
            getAverageConversionCurrencyValues.setCount(count);
            averageConversionCurrencyValues.setResultCurrencyOne(resultCurrencyOne);
            averageConversionCurrencyValues.setResultCurrencyTwo(resultCurrencyTwo);
            averageConversionCurrencyValuesRepos.save(getAverageConversionCurrencyValues);
            return;
        }

        //Пытаемся получить пару валют из базы
        averageConversionCurrencyValues = averageConversionCurrencyValuesRepos.findByCurrencyOneAndCurrencyTwo(currencyTwo, currencyOne);

        // Если пара найдена
        if (averageConversionCurrencyValues != null) {
            // Получаем значения и пересчитываем
            count = averageConversionCurrencyValues.getCount() + 1;
            resultCurrencyOne = (averageConversionCurrencyValues.getResultCurrencyOne() + currencyValuesTwo.getRubValue()) / count;
            resultCurrencyTwo = (averageConversionCurrencyValues.getResultCurrencyTwo() + currencyValuesOne.getRubValue()) / count;
            getAverageConversionCurrencyValues = averageConversionCurrencyValuesRepos.getOne(averageConversionCurrencyValues.getId());
            getAverageConversionCurrencyValues.setCount(count);
            averageConversionCurrencyValues.setResultCurrencyOne(resultCurrencyOne);
            averageConversionCurrencyValues.setResultCurrencyTwo(resultCurrencyTwo);
            averageConversionCurrencyValuesRepos.save(getAverageConversionCurrencyValues);
            return;
        }

        // Если не завершилась раньше, создаем новую запись в базу
        averageConversionCurrencyValues = new AverageConversionCurrencyValues();
        averageConversionCurrencyValues.setCount(1);
        averageConversionCurrencyValues.setCurrencyOne(currencyOne);
        averageConversionCurrencyValues.setCurrencyTwo(currencyTwo);
        averageConversionCurrencyValues.setResultCurrencyOne(currencyValuesOne.getRubValue());
        averageConversionCurrencyValues.setResultCurrencyTwo(currencyValuesTwo.getRubValue());
        averageConversionCurrencyValuesRepos.save(averageConversionCurrencyValues);

    }

    // Расчет суммарного объёма конвертаций для пары валют за неделю
    public double getWeeklySumConversionCurrencyValue(Currency currencyFrom,
                                                      Currency currencyTo,
                                                      LocalDate dateFrom) {
        LocalDate dateTo = dateFrom.plusDays(7); //todo может упасть если от стартовой даты до текущей нет 7 дней
        List<Conversion> conversionList = conversionRepos.findByCurrencyFromAndCurrencyToAndDateBetween
                (currencyFrom, currencyTo, dateFrom, dateTo);

        double sum = 0;

        // Проход по списку конвертаций
        for (Conversion c : conversionList) {
            sum += c.getAmount();
        }

        return sum;
    }
}
