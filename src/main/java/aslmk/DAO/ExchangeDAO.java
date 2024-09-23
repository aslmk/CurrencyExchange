package aslmk.DAO;

import aslmk.Database;
import aslmk.Models.Exchange;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeDAO {
    CurrencyDAO currencyDAO = new CurrencyDAO();
    private static Database database = new Database();
    public Exchange exchange(String baseCurrencyCode, String targetCurrencyCode, double amount) throws SQLException {
        double rate = getExchangeRate(baseCurrencyCode, targetCurrencyCode);
        double convertedAmount = calculateConvertedAmount(rate, amount);

        return new Exchange(
                currencyDAO.findCurrencyByCode(baseCurrencyCode),
                currencyDAO.findCurrencyByCode(targetCurrencyCode),
                rate,
                amount,
                convertedAmount
        );
    }

    private double getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        if (exchangeRateExists(baseCurrencyCode, targetCurrencyCode)) {
            return getRate(baseCurrencyCode, targetCurrencyCode);
        } else if (exchangeRateExists(targetCurrencyCode, baseCurrencyCode)) {
            return 1 / getRate(targetCurrencyCode, baseCurrencyCode); // обратный курс
        } else if (exchangeRateExists("USD", baseCurrencyCode) && exchangeRateExists("USD", targetCurrencyCode)) {
            // Курс через USD
            double usdToBase = 1 / getRate("USD", baseCurrencyCode);
            double usdToTarget = getRate("USD", targetCurrencyCode);
            return Math.round((usdToBase * usdToTarget) * 100.0) / 100.0;
        } else {
            throw new SQLException("Курс валют для данной пары не найден");
        }
    }

    private double calculateConvertedAmount(double rate, double amount) {
        return Math.round((rate * amount) * 100.0) / 100.0;
    }

    private double getRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        int baseCurrencyId = currencyDAO.findCurrencyByCode(baseCurrencyCode).id();
        int targetCurrencyId = currencyDAO.findCurrencyByCode(targetCurrencyCode).id();
        database.openConnection();
        String query = "SELECT Rate FROM ExchangeRates WHERE BaseCurrencyId="+baseCurrencyId+
                " AND TargetCurrencyId="+targetCurrencyId;

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            try (ResultSet rs = prStm.executeQuery()) {
                return rs.getDouble("Rate");
            }
        }
    }
    private boolean exchangeRateExists(String baseCurrencyCode, String targetCurrencyCode) {
        String query = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ? LIMIT 1;";
        database.openConnection();
        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            prStm.setInt(1, currencyDAO.findCurrencyByCode(baseCurrencyCode).id());
            prStm.setInt(2, currencyDAO.findCurrencyByCode(targetCurrencyCode).id());
            try (ResultSet rs = prStm.executeQuery()) {
                // Если запись найдена, вернётся хотя бы одна строка
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Если произошла ошибка, можно также вернуть false
        }
    }
}
