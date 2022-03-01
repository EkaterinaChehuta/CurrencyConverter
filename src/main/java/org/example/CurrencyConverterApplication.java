package org.example;

import org.example.repos.CurrencyRepos;
import org.example.repos.CurrencyValuesRepos;
import org.example.service.CurrencyService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@SpringBootApplication
public class CurrencyConverterApplication {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterApplication.class, args);
    }

    @Bean
    ApplicationRunner init(CurrencyRepos currencyRepos, CurrencyValuesRepos currencyValuesRepos) throws ParserConfigurationException, IOException, SAXException {
        CurrencyService.parse(currencyRepos, currencyValuesRepos);
        return args -> {
        };
    }
}
