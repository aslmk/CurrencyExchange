package aslmk.Servlets;

import aslmk.DAO.CurrencyDAO;
import aslmk.DAO.ExchangeRateDAO;
import aslmk.Utils.*;
import aslmk.Utils.Exceptions.CurrencyNotFoundException;
import aslmk.Utils.Exceptions.ValidationException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ExchangeRatesServlet extends HttpServlet {
    ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    CurrencyDAO currencyDAO = new CurrencyDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Utils.setResponse(resp, exchangeRateDAO.getExchangeRates());
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        double rate = Double.parseDouble(req.getParameter("rate"));
        try {
            if (!ValidationUtil.isExchangeRateParametersValid(baseCurrencyCode, targetCurrencyCode, rate)) {
                throw new ValidationException("Incorrect parameters!");
            }

            exchangeRateDAO.addExchangeRate(baseCurrencyCode, targetCurrencyCode, rate);
            Utils.postResponse(resp, HttpServletResponse.SC_CREATED, "Exchange rate successfully added.");
            //Utils.setResponse(resp, HttpServletResponse.SC_CREATED, "application/x-www-form-urlencoded", "Exchange rate successfully added.");

        } catch (ValidationException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 14) { // SQL Internal server error
                ResponseHandlingUtil.dataBaseMessage(resp);
            } else if (errorCode == 19) { // SQL constraint
                ResponseHandlingUtil.alreadyExistsMessage(resp);
            }
        } catch (CurrencyNotFoundException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }
}
