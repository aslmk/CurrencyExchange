package aslmk.DAO;

import aslmk.Database;
import aslmk.Models.Currency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CurrencyDAO {
    //Database database = database.getInstance();
    private static Database database = new Database();

    public static void addCurrency(String fullName, String code, String sign) throws SQLException {
        String query = "INSERT INTO Currencies (fullName,code,sign) VALUES (?, ?, ?);";

        try (PreparedStatement prStm = database.getConnection().prepareStatement(query)) {

            prStm.setString(1, fullName);
            prStm.setString(2, code);
            prStm.setString(3, sign);

            prStm.execute();
        }
    }
    public Currency findCurrencyByCode(String code) throws SQLException {
        String query = "SELECT * FROM Currencies WHERE code='"+code+"';";

        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            if (rs.next()) {
                return new Currency(rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("sign"),
                        rs.getString("fullName"));
            }
        }
        return null;
    }
    public ArrayList<Currency> getCurrencies() throws SQLException {
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
        }
        return currencies;
    }
    private Currency findCurrencyById(int id) throws SQLException {
        String query = "SELECT * FROM Currencies WHERE id="+id+";";

        try (Statement stm = database.getConnection().createStatement()) {
            ResultSet rs = stm.executeQuery(query);
            if (rs.next()) {
                return new Currency(rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("sign"),
                        rs.getString("fullName"));
            }
        }

        return null;
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

}
