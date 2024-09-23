package aslmk.Models;

import java.math.BigDecimal;

public record ExchangeRate(int id, Currency baseCurrency, Currency targetCurrency, double rate) {}
