package aslmk.Service;

import aslmk.Models.Currency;
import aslmk.Utils.Exceptions.DatabaseException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CurrencyService {
    void addCurrency(String fullName, String code, String sign) throws SQLException, DatabaseException;
    Currency findCurrencyByCode(String code) throws SQLException, DatabaseException;
    ArrayList<Currency> getCurrencies() throws SQLException, DatabaseException;
    Currency findCurrencyById(int id) throws SQLException, DatabaseException;
}
