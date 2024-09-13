package aslmk;

import aslmk.Models.Currency;
import aslmk.Models.Exchange;
import aslmk.Models.ExchangeRate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Repository {
    private Database database;
    public Repository (Database database) {
        this.database = database;
    }
    public Currency findCurrencyByCode(String code) {
        String query = "SELECT * FROM Currencies WHERE code='"+code+"';";

        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            if (rs.next()) {
                return new Currency(rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("sign"),
                        rs.getString("fullName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public ArrayList<Currency> getCurrencies() {
        ArrayList<Currency> currencies = new ArrayList<>();
        String query = "SELECT * FROM Currencies;";
        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                currencies.add(new Currency(rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("sign"),
                        rs.getString("fullName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }
    public boolean addCurrency(String fullName, String code, String sign) {
        String query = "INSERT INTO Currencies (fullName,code,sign) VALUES (?, ?, ?);";

        if (currencyExists(fullName, code, sign)) return false;

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {

            prStm.setString(1, fullName);
            prStm.setString(2, code);
            prStm.setString(3, sign);

            int affectedRows = prStm.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Insert successful!");
            } else {
                System.out.println("Insert failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) {
        if (exchangeRateExists(baseCurrencyCode, targetCurrencyCode)) {
            return false;
        } else {
            String query = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?);";
            try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
                int baseCurrencyId = findCurrencyByCode(baseCurrencyCode).id();
                int targetCurrencyId = findCurrencyByCode(targetCurrencyCode).id();
                prStm.setInt(1, baseCurrencyId);
                prStm.setInt(2, targetCurrencyId);
                prStm.setDouble(3, rate);

                int affectedRows = prStm.executeUpdate();

                if (affectedRows > 0) {
                    System.out.println("Insert successful!");
                } else {
                    System.out.println("Insert failed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
    private boolean currencyExists(String fullName, String code, String sign) {
        String query = "SELECT * FROM Currencies WHERE fullName = ? AND code = ? AND sign = ? LIMIT 1;";

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            prStm.setString(1, fullName);
            prStm.setString(2, code);
            prStm.setString(3, sign);
            try (ResultSet rs = prStm.executeQuery()) {
                // Если запись найдена, вернётся хотя бы одна строка
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Если произошла ошибка, можно также вернуть false
        }
    }
    private boolean exchangeRateExists(String baseCurrencyCode, String targetCurrencyCode) {
        String query = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId = ? AND TargetCurrencyId = ? LIMIT 1;";

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            prStm.setInt(1, findCurrencyByCode(baseCurrencyCode).id());
            prStm.setInt(2, findCurrencyByCode(targetCurrencyCode).id());
            try (ResultSet rs = prStm.executeQuery()) {
                // Если запись найдена, вернётся хотя бы одна строка
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Если произошла ошибка, можно также вернуть false
        }
    }
    public ArrayList<ExchangeRate> getExchangeRates() {
        ArrayList<ExchangeRate> exchangeRates = new ArrayList<>();
        String query = "SELECT * FROM ExchangeRates";
        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                int exchangeRateId = rs.getInt("id");
                Currency baseCurrency = findCurrencyById(rs.getInt("BaseCurrencyId"));
                Currency targetCurrency = findCurrencyById(rs.getInt("TargetCurrencyId"));
                double rate = rs.getDouble("Rate");
                exchangeRates.add(new ExchangeRate(exchangeRateId, baseCurrency, targetCurrency, rate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }
    private Currency findCurrencyById(int id) {
        String query = "SELECT * FROM Currencies WHERE id="+id+";";

        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            if (rs.next()) {
                return new Currency(rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("sign"),
                        rs.getString("fullName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public ExchangeRate findExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) {
        int baseCurrencyId = findCurrencyByCode(baseCurrencyCode).id();
        int targetCurrencyId = findCurrencyByCode(targetCurrencyCode).id();
        String query = "SELECT * FROM ExchangeRates " +
                "WHERE BaseCurrencyId="+baseCurrencyId+" AND TargetCurrencyId="+targetCurrencyId+";";

        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            if (rs.next()) {
                return new ExchangeRate(rs.getInt("id"),
                        findCurrencyById(rs.getInt("BaseCurrencyId")),
                        findCurrencyById(rs.getInt("TargetCurrencyId")),
                        rs.getDouble("Rate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Exchange exchange(String baseCurrencyCode, String targetCurrencyCode, double amount) {
        double rate = 0;
        if (exchangeRateExists(baseCurrencyCode, targetCurrencyCode)) {
            rate = getRate(baseCurrencyCode, targetCurrencyCode);

        } else if (exchangeRateExists(targetCurrencyCode, baseCurrencyCode)) {
            rate = getRate(targetCurrencyCode, baseCurrencyCode);
        } else if (exchangeRateExists("USD", baseCurrencyCode) &&
                exchangeRateExists("USD", targetCurrencyCode)) {
            double usdBaseCurr = 1/getRate("USD", baseCurrencyCode);
            double usdTargetCurr = getRate("USD", targetCurrencyCode);
            rate = Math.round((usdBaseCurr * usdTargetCurr)*100.0) / 100.0;
        }
        double convertedAmount = Math.round((rate*amount) * 100.0) / 100.0;

        return new Exchange(findCurrencyByCode(baseCurrencyCode),
                findCurrencyByCode(targetCurrencyCode),
                rate,
                amount,
                convertedAmount);

    }
    private double getRate(String baseCurrencyCode, String targetCurrencyCode) {
        int baseCurrencyId = findCurrencyByCode(baseCurrencyCode).id();
        int targetCurrencyId = findCurrencyByCode(targetCurrencyCode).id();
        String query = "SELECT Rate FROM ExchangeRates WHERE BaseCurrencyId="+baseCurrencyId+
                " AND TargetCurrencyId="+targetCurrencyId;

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            try (ResultSet rs = prStm.executeQuery()) {
                return rs.getDouble("Rate");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Double.NaN;
    }

    public boolean updateRate(String baseCurrencyCode, String targetCurrencyCode, double rate) {
        int baseCurrencyId = findCurrencyByCode(baseCurrencyCode).id();
        int targetCurrencyId = findCurrencyByCode(targetCurrencyCode).id();
        final String query = "UPDATE ExchangeRates SET Rate=? " +
                "WHERE BaseCurrencyId=? AND TargetCurrencyId=?;";

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {
            prStm.setDouble(1, rate);
            prStm.setInt(2, baseCurrencyId);
            prStm.setInt(3, targetCurrencyId);
            int affectedRows = prStm.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Insert successful!");
            } else {
                System.out.println("Insert failed.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

