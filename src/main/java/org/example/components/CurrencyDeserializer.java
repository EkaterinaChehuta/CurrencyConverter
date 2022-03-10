package org.example.components;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.example.entities.Currency;
import org.example.repos.CurrencyRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class CurrencyDeserializer  extends JsonDeserializer<Currency> {
    @Autowired
    private CurrencyRepos currencyRepos;

    @Override
    public Currency deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return currencyRepos.findById(Integer.parseInt(jsonParser.getValueAsString())).orElse(null);
    }
}
