package aslmk.DAO;

import aslmk.Database;
import aslmk.Models.Currency;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CurrencyDAO {
    private static Database database = Database.getInstance();

    public void addCurrency(String fullName, String code, String sign) throws SQLException {
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
    public Currency findCurrencyById(int id) throws SQLException {
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

}
