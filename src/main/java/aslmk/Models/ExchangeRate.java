package aslmk.Models;

public record ExchangeRate(int id, Currency baseCurrency, Currency targetCurrency, double rate) {}
