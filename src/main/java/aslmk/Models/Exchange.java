package aslmk.Models;

import java.math.BigDecimal;

public record Exchange(Currency baseCurrency, Currency targetCurrency,
                       double rate, double amount, double convertedAmount) {}
