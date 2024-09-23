package aslmk.DAO;

import aslmk.Database;
import aslmk.Models.Currency;
import aslmk.Models.ExchangeRate;

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
        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            int baseCurrencyId = currencyDAO.findCurrencyByCode(baseCurrencyCode).id();
            int targetCurrencyId = currencyDAO.findCurrencyByCode(targetCurrencyCode).id();

            prStm.setInt(1, baseCurrencyId);
            prStm.setInt(2, targetCurrencyId);
            prStm.setDouble(3, rate);

            prStm.execute();
        }
    }
    private boolean exchangeRateExists(String baseCurrencyCode, String targetCurrencyCode) throws SQLException {
        String query = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ? LIMIT 1;";

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
        int baseCurrencyId = currencyDAO.findCurrencyByCode(baseCurrencyCode).id();
        int targetCurrencyId = currencyDAO.findCurrencyByCode(targetCurrencyCode).id();
        final String query = "UPDATE ExchangeRates SET Rate=? " +
                "WHERE BaseCurrencyId=? AND TargetCurrencyId=?;";

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            prStm.setDouble(1, rate);
            prStm.setInt(2, baseCurrencyId);
            prStm.setInt(3, targetCurrencyId);
            prStm.execute();
        }
    }
}
