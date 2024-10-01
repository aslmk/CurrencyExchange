package aslmk.Service;

import aslmk.Models.ExchangeRate;
import aslmk.Utils.Exceptions.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ExchangeRateService {
    void addExchangeRate(String baseCurrencyCode, String targetCurrencyCode, Double rate) throws SQLException, DatabaseException;
    ArrayList<ExchangeRate> getExchangeRates() throws SQLException, DatabaseException;
    ExchangeRate findExchangeRateByCode(String baseCurrencyCode, String targetCurrencyCode) throws SQLException, DatabaseException;
    void updateRate(String baseCurrencyCode, String targetCurrencyCode, double rate) throws SQLException, DatabaseException;
}
