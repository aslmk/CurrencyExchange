package aslmk.DAO;

import aslmk.Database;
import aslmk.Models.Currency;
import aslmk.Utils.Exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;

public class CurrencyDAO {
    public void addCurrency(String fullName, String code, String sign) throws SQLException, DatabaseException {
        String query = "INSERT INTO Currencies (fullName,code,sign) VALUES (?, ?, ?);";
        Database.isConnectionPoolCreated();
        Connection connection = Database.getConnection();

        try (PreparedStatement prStm =  connection.prepareStatement(query)) {
            prStm.setString(1, fullName);
            prStm.setString(2, code);
            prStm.setString(3, sign);
            prStm.executeUpdate();
        } finally {
            Database.releaseConnection(connection);
        }
    }
    public Currency findCurrencyByCode(String code) throws SQLException, DatabaseException {
        String query = "SELECT * FROM Currencies WHERE code='"+code+"';";
        Database.isConnectionPoolCreated();
        Connection connection = Database.getConnection();

        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(query)) {

            if (rs.next()) {
                return new Currency(rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("sign"),
                        rs.getString("fullName"));
            }
        } finally {
            Database.releaseConnection(connection);
        }
        return null;
    }
    public ArrayList<Currency> getCurrencies() throws SQLException, DatabaseException {
        ArrayList<Currency> currencies = new ArrayList<>();
        String query = "SELECT * FROM Currencies;";
        Database.isConnectionPoolCreated();
        Connection connection = Database.getConnection();

        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                currencies.add(new Currency(rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("sign"),
                        rs.getString("fullName")));
            }
        } finally {
            Database.releaseConnection(connection);
        }
        return currencies;
    }
    public Currency findCurrencyById(int id) throws SQLException, DatabaseException {
        String query = "SELECT * FROM Currencies WHERE id="+id+";";
        Database.isConnectionPoolCreated();
        Connection connection = Database.getConnection();
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(query)) {
            if (rs.next()) {
                return new Currency(rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("sign"),
                        rs.getString("fullName"));
            }
        } finally {
            Database.releaseConnection(connection);
        }
        return null;
    }

}
