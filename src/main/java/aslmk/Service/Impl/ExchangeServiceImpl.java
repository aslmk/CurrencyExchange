package aslmk.Service.Impl;

import aslmk.DAO.CurrencyDAO;
import aslmk.DTO.ExchangeDTO;
import aslmk.Database;
import aslmk.Service.ExchangeService;
import aslmk.Utils.Exceptions.DatabaseException;
import aslmk.Utils.Exceptions.ExchangeRateNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeServiceImpl implements ExchangeService {
    CurrencyDAO currencyDAO = new CurrencyDAO();

    @Override
    public ExchangeDTO exchange(String baseCurrencyCode, String targetCurrencyCode, double amount) throws SQLException, ExchangeRateNotFoundException, DatabaseException {
        double rate = getExchangeRate(baseCurrencyCode, targetCurrencyCode);
        double convertedAmount = calculateConvertedAmount(rate, amount);

        return new ExchangeDTO(
                currencyDAO.findCurrencyByCode(baseCurrencyCode),
                currencyDAO.findCurrencyByCode(targetCurrencyCode),
                rate,
                amount,
                convertedAmount
        );
    }
    private double getExchangeRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, ExchangeRateNotFoundException, DatabaseException {
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
            throw new ExchangeRateNotFoundException("The exchange rate for this pair was not found");
        }
    }

    private double calculateConvertedAmount(double rate, double amount) {
        return Math.round((rate * amount) * 100.0) / 100.0;
    }

    private double getRate(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, DatabaseException {
        int baseCurrencyId = currencyDAO.findCurrencyByCode(baseCurrencyCode).id();
        int targetCurrencyId = currencyDAO.findCurrencyByCode(targetCurrencyCode).id();
        String query = "SELECT Rate FROM ExchangeRates WHERE BaseCurrencyId="+baseCurrencyId+
                " AND TargetCurrencyId="+targetCurrencyId;

        Database.isConnectionPoolCreated();
        Connection connection = Database.getConnection();

        try (PreparedStatement prStm = connection.prepareStatement(query);
             ResultSet rs = prStm.executeQuery()) {
            return rs.getDouble("Rate");
        } finally {
            Database.releaseConnection(connection);
        }
    }
    private boolean exchangeRateExists(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        String query = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ? LIMIT 1;";
        Database.isConnectionPoolCreated();
        Connection connection = Database.getConnection();

        try (PreparedStatement prStm = connection.prepareStatement(query)) {
            prStm.setInt(1, currencyDAO.findCurrencyByCode(baseCurrencyCode).id());
            prStm.setInt(2, currencyDAO.findCurrencyByCode(targetCurrencyCode).id());
            try (ResultSet rs = prStm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            Database.releaseConnection(connection);
        }
    }
}
