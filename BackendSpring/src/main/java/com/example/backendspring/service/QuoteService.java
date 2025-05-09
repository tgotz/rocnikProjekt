package com.example.backendspring.service;

import com.example.backendspring.model.Quote;
import com.example.backendspring.model.Character;
import com.example.backendspring.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepository quoteRepository;


    public void insertQuote(String textQuote, Character character) {
        Quote quote = new Quote();
        quote.setTextQuote(textQuote);
        quote.setCharacter(character);
        quoteRepository.save(quote);
    }

    public List<Quote> getQuotes(Character character) {
        return quoteRepository.findByCharacter(character);
    }

    public void deleteQuotes(Character character) {
        quoteRepository.deleteByCharacter(character);
    }
}
