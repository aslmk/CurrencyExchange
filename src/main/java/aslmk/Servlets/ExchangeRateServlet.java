package aslmk.Servlets;

import aslmk.DAO.CurrencyDAO;
import aslmk.DAO.ExchangeRateDAO;
import aslmk.Models.ExchangeRate;
import aslmk.Utils.Exceptions.DatabaseException;
import aslmk.Utils.Exceptions.ExchangeRateNotFoundException;
import aslmk.Utils.Exceptions.ParamNotFoundException;
import aslmk.Utils.ResponseHandlingUtil;
import aslmk.Utils.Utils;
import aslmk.Utils.Exceptions.ValidationException;
import aslmk.Utils.ValidationUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.NumberFormat;

public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateDAO exchangeRateDao = new ExchangeRateDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String exchangeRateCode = Utils.extractCodeFromURL(pathInfo);

        try {
            if (!ValidationUtil.isExchangeRateCodeValid(exchangeRateCode)) {
                throw new ValidationException("Incorrect exchange rate code!");
            } else {
                String baseCurrencyCode = exchangeRateCode.substring(0,3);
                String targetCurrencyCode = exchangeRateCode.substring(3, 6);
                ExchangeRate exchangeRate = exchangeRateDao.findExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
                if (exchangeRate != null) {
                    Utils.setResponse(resp, exchangeRate);
                } else {
                    throw new ExchangeRateNotFoundException("Exchange rate not found.");
                }
            }
        } catch (ValidationException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        } catch (ExchangeRateNotFoundException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        } else super.service(req, resp);
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo().toUpperCase().trim();
        String exchangeRateCode = Utils.extractCodeFromURL(pathInfo);

        try {
            double rate = Double.parseDouble(req.getParameter("rate"));

            if (!ValidationUtil.isExchangeRateCodeValid(exchangeRateCode)) {
                throw new ValidationException("Incorrect parameters!");
            }

            String baseCurrencyCode = exchangeRateCode.substring(0,3);
            String targetCurrencyCode = exchangeRateCode.substring(3, 6);

            exchangeRateDao.updateRate(baseCurrencyCode, targetCurrencyCode, rate);
            ExchangeRate exchangeRate = exchangeRateDao.findExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate != null) {
                Utils.setResponse(resp,
                        exchangeRate,
                        HttpServletResponse.SC_OK,
                        "application/x-www-form-urlencoded",
                        "Rate successfully updated!");
            }

        } catch (ValidationException | NumberFormatException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        } catch (DatabaseException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
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
