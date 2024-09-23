package aslmk.Servlets;

import aslmk.DAO.CurrencyDAO;
import aslmk.DAO.ExchangeDAO;
import aslmk.DAO.ExchangeRateDAO;
import aslmk.Database;
import aslmk.Models.Exchange;
import aslmk.Utils.ResponseHandlingUtil;
import aslmk.Utils.Utils;
import aslmk.Utils.ValidationException;
import aslmk.Utils.ValidationUtil;
import com.google.gson.Gson;
import jdk.jshell.execution.Util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ExchangeServlet extends HttpServlet {
    ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    CurrencyDAO currencyDAO = new CurrencyDAO();
    ExchangeDAO exchangeDAO = new ExchangeDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fromCurrency = req.getParameter("from");
        String toCurrency = req.getParameter("to");
        double amount = Double.parseDouble(req.getParameter("amount"));

        try {
            if (!ValidationUtil.isExchangeRateParameters(fromCurrency, toCurrency, amount)) {
                throw new ValidationException();
            } else {
                if (currencyDAO.findCurrencyByCode(fromCurrency) != null &&
                        currencyDAO.findCurrencyByCode(toCurrency) != null) {
                    Exchange exchange = exchangeDAO.exchange(fromCurrency, toCurrency, amount);
                    if (exchange != null) {
                        Utils.postResponse(resp, 201);
                        Utils.setResponse(resp, exchange);
                    }
                } else {
                    ResponseHandlingUtil.currencyNotFoundMessage(resp);
                }
            }
        } catch (ValidationException e) {
            ResponseHandlingUtil.notEnoughParametersMessage(resp);
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
