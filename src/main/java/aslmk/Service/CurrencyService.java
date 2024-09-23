package aslmk.Service;

import aslmk.DAO.CurrencyDAO;
import aslmk.Models.Currency;
import aslmk.Utils.ResponseHandlingUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CurrencyService {
    CurrencyDAO currencyDAO = new CurrencyDAO();
    ResponseHandlingUtil responseHandlingUtil = new ResponseHandlingUtil();
    public void addCurrency() {

    }

//    public Currency findCurrencyByCode(String code) {
//        Currency targetCurrency = currencyDAO.findCurrencyByCode(code);
//
//        return targetCurrency;
//    }
}
