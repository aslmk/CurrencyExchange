package aslmk.Service;

import aslmk.DTO.ExchangeDTO;
import aslmk.Utils.Exceptions.DatabaseException;
import aslmk.Utils.Exceptions.ExchangeRateNotFoundException;

import java.sql.SQLException;

public interface ExchangeService {
    ExchangeDTO exchange(String baseCurrencyCode, String targetCurrencyCode, double amount) throws SQLException, ExchangeRateNotFoundException, DatabaseException;
}
