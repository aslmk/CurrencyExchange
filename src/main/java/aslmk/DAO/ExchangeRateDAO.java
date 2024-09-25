package aslmk.DAO;

import aslmk.Database;
import aslmk.Models.Currency;
import aslmk.Models.ExchangeRate;
import aslmk.Utils.Exceptions.CurrencyNotFoundException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ExchangeRateDAO {
    Database database = Database.getInstance();
    CurrencyDAO currencyDAO = new CurrencyDAO();

    public void addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws SQLException {
        String query = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?);";

        Currency baseCurrency = currencyDAO.findCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDAO.findCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null) {
            throw new CurrencyNotFoundException("Base currency not found: " + baseCurrencyCode);
        }
        if (targetCurrency == null) {
            throw new CurrencyNotFoundException("Target currency not found: " + targetCurrencyCode);
        }

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            int baseCurrencyId = baseCurrency.id();
            int targetCurrencyId = targetCurrency.id();

            prStm.setInt(1, baseCurrencyId);
            prStm.setInt(2, targetCurrencyId);
            prStm.setDouble(3, rate);

            prStm.execute();
        }
    }
    public ArrayList<ExchangeRate> getExchangeRates() throws SQLException {
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = "SELECT * FROM ExchangeRates";
        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                int exchangeRateId = rs.getInt("id");
                Currency baseCurrency = currencyDAO.findCurrencyById(rs.getInt("BaseCurrencyId"));
                Currency targetCurrency = currencyDAO.findCurrencyById(rs.getInt("TargetCurrencyId"));
                double rate = rs.getDouble("Rate");
                exchangeRates.add(new ExchangeRate(exchangeRateId, baseCurrency, targetCurrency, rate));
            }
        }
        return exchangeRates;
    }
    public ExchangeRate findExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        int baseCurrencyId = currencyDAO.findCurrencyByCode(baseCurrencyCode).id();
        int targetCurrencyId = currencyDAO.findCurrencyByCode(targetCurrencyCode).id();

        String query = "SELECT * FROM ExchangeRates " +
                "WHERE BaseCurrencyId="+baseCurrencyId+" AND TargetCurrencyId="+targetCurrencyId+";";

        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            if (rs.next()) {
                return new ExchangeRate(rs.getInt("id"),
                        currencyDAO.findCurrencyById(rs.getInt("BaseCurrencyId")),
                        currencyDAO.findCurrencyById(rs.getInt("TargetCurrencyId")),
                        rs.getDouble("Rate"));
            }
        }
        return null;
    }
    public void updateRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException {
        Currency baseCurrency = currencyDAO.findCurrencyByCode(baseCurrencyCode);
        Currency targetCurrency = currencyDAO.findCurrencyByCode(targetCurrencyCode);

        if (baseCurrency == null) {
            throw new CurrencyNotFoundException("Base currency not found: " + baseCurrencyCode);
        }
        if (targetCurrency == null) {
            throw new CurrencyNotFoundException("Target currency not found: " + targetCurrencyCode);
        }

        int baseCurrencyId = baseCurrency.id();
        int targetCurrencyId = targetCurrency.id();

        String query = "UPDATE ExchangeRates SET Rate=? " +
                "WHERE BaseCurrencyId=? AND TargetCurrencyId=?;";

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            prStm.setDouble(1, rate);
            prStm.setInt(2, baseCurrencyId);
            prStm.setInt(3, targetCurrencyId);
            prStm.execute();
        }
    }
}
