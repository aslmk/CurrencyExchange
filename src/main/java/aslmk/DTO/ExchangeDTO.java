package aslmk.DTO;

import aslmk.Models.Currency;

public record ExchangeDTO(Currency baseCurrency, Currency targetCurrency,
                          double rate, double amount, double convertedAmount) {}
