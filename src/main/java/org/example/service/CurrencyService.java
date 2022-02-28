package org.example.service;

import org.example.entities.Currency;
import org.example.entities.CurrencyValues;
import org.example.repos.CurrencyRepos;
import org.example.repos.CurrencyValuesRepos;
import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class CurrencyService {
    private static LocalDate localDateFromDocument;

    public static void parse(CurrencyRepos currencyRepos, CurrencyValuesRepos currencyValuesRepos) throws ParserConfigurationException, IOException, SAXException {
        String url = "https://www.cbr-xml-daily.ru/daily_utf8.xml";

        // Получение фабрики, чтобы после получить билдер документов.
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);

        // Получили из фабрики билдер, который парсит XML,
        // создает структуру Document в виде иерархического дерева.
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Запарсили XML, создав структуру Document.
        // Теперь у нас есть доступ ко всем элементам, каким нам нужно.
        Document document = builder.parse(url);
        document.getDocumentElement().normalize();

        // получение даты из документа
        localDateFromDocument = getDateFromDocument(document);

        // Получение списка всех элементов Valute внутри корневого элемента
        NodeList currencyNode = document.getElementsByTagName("Valute");

        saveRUB(currencyRepos, currencyValuesRepos);

        // Перебор всех элементов
        for (int i = 0; i < currencyNode.getLength(); i++) {
            Node node = currencyNode.item(i);
            NodeList nodeList = node.getChildNodes();

            String numCode = nodeList.item(0).getTextContent();
            System.out.println("......" + numCode);
            String charCode = nodeList.item(1).getTextContent();
            System.out.println("......" + charCode);
            String name = nodeList.item(3).getTextContent();
            System.out.println("......" + name);
            int nominal = Integer.parseInt(nodeList.item(2).getTextContent());
            System.out.println("......" + nominal);
            double rubValue = Double.parseDouble(nodeList.item(4).getTextContent().replace(",", "."));
            System.out.println("......" + rubValue);

            // Проверка на наличие валюты в базе
            Currency currency = currencyRepos.findByNumCodeAndCharCodeAndNameCurrency(numCode, charCode, name);
            if (currency == null) {
                currency = new Currency();
                currency.setNumCode(numCode);
                currency.setCharCode(charCode);
                currency.setNominal(nominal);
                currency.setNameCurrency(name);
                currencyRepos.save(currency);
            }

            // Проверка актуальности данных в базе CurrencyValuesRepos по дате
            CurrencyValues currencyValues = currencyValuesRepos.findByDateAndCurrency(localDateFromDocument, currency);
            if (currencyValues == null) {
                currencyValues = new CurrencyValues();
                currencyValues.setRubValue(rubValue);
                currencyValues.setDate(localDateFromDocument);
                currencyValues.setCurrency(currency);
                currencyValuesRepos.save(currencyValues);
            }
        }
    }

    private static void saveRUB(CurrencyRepos currencyRepos, CurrencyValuesRepos currencyValuesRepos) {
        String numCode = "111";
        String charCode = "RUB";
        int nominal = 1;
        String name = "Российский рубль";
        double rubValue = 1.0;

        Currency currencyRUB = currencyRepos.findByNumCodeAndCharCodeAndNameCurrency(numCode, charCode, name);
        if (currencyRUB == null) {
            currencyRUB = new Currency();
            currencyRUB.setNumCode(numCode);
            currencyRUB.setCharCode(charCode);
            currencyRUB.setNominal(nominal);
            currencyRUB.setNameCurrency(name);
            currencyRepos.save(currencyRUB);
        }

        CurrencyValues currencyValuesRUB = currencyValuesRepos.findByCurrency(currencyRUB);
        if (currencyValuesRUB == null) {
            currencyValuesRUB = new CurrencyValues();
            currencyValuesRUB.setDate(localDateFromDocument);
            currencyValuesRUB.setCurrency(currencyRUB);
            currencyValuesRUB.setRubValue(rubValue);
            currencyValuesRepos.save(currencyValuesRUB);
        }
    }

    // Получение текущей даты для запроса
    private static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(new Date());
    }

    // Получение даты из документа
    private static LocalDate getDateFromDocument(Document document) {
        Node node = document.getElementsByTagName("ValCurs").item(0);
        LocalDate date = null;
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            date = LocalDate.parse((element.getAttribute("Date")), format);
        }
        return date;
    }
}
