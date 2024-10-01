package aslmk.Service.Impl;

import aslmk.DAO.ExchangeRateDAO;
import aslmk.Models.ExchangeRate;
import aslmk.Service.ExchangeRateService;
import aslmk.Utils.Exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    @Override
    public void addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws SQLException, DatabaseException {
        exchangeRateDAO.addExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
    }

    @Override
    public ArrayList<ExchangeRate> getExchangeRates() throws SQLException, DatabaseException {
        return exchangeRateDAO.getExchangeRates();
    }

    @Override
    public ExchangeRate findExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, DatabaseException {
        return exchangeRateDAO.findExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
    }

    @Override
    public void updateRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException, DatabaseException {
        exchangeRateDAO.updateRate(baseCurrencyCode, targetCurrencyCode, rate);
    }
}
