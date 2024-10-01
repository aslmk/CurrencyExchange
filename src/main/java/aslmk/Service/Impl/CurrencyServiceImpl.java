package aslmk.Service.Impl;

import aslmk.DAO.CurrencyDAO;
import aslmk.Models.Currency;
import aslmk.Service.CurrencyService;
import aslmk.Utils.Exceptions.DatabaseException;
import aslmk.Utils.ResponseHandlingUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class CurrencyServiceImpl implements CurrencyService {
    CurrencyDAO currencyDAO = new CurrencyDAO();
    @Override
    public void addCurrency(String fullName, String code, String sign) throws SQLException, DatabaseException {
        currencyDAO.addCurrency(fullName, code, sign);
    }
    @Override
    public Currency findCurrencyByCode(String code) throws SQLException, DatabaseException {
        return currencyDAO.findCurrencyByCode(code);
    }
    @Override
    public ArrayList<Currency> getCurrencies() throws SQLException, DatabaseException {
        return currencyDAO.getCurrencies();
    }

    @Override
    public Currency findCurrencyById(int id) throws SQLException, DatabaseException {
        return currencyDAO.findCurrencyById(id);
    }
}
