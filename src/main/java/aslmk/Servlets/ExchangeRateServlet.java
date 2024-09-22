package aslmk.Servlets;

import aslmk.DAO.CurrencyDAO;
import aslmk.DAO.ExchangeRateDAO;
import aslmk.Database;
import aslmk.Models.ExchangeRate;
import aslmk.Repository;
import aslmk.Utils.ResponseHandlingUtil;
import aslmk.Utils.Utils;
import aslmk.Utils.ValidationException;
import aslmk.Utils.ValidationUtil;
import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateDAO exchangeRateDao = new ExchangeRateDAO();
    CurrencyDAO currencyDAO = new CurrencyDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String exchangeRateCode = Utils.getExchangeRateCodeFromURL(pathInfo);

        try {
            if (!ValidationUtil.isExchangeRateCode(exchangeRateCode)) {
                throw new ValidationException("Incorrect exchange rate code!");
            } else {
                String baseCurrencyCode = exchangeRateCode.substring(0,3);
                String targetCurrencyCode = exchangeRateCode.substring(3, 6);
                ExchangeRate exchangeRate = exchangeRateDao.findExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
                if (exchangeRate != null) {
                    Utils.setResponse(resp, exchangeRate);
                } else {
                    ResponseHandlingUtil.currencyNotFoundMessage(resp);
                }
            }
        } catch (ValidationException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        }


//        else {
//            String baseCurrencyCode = exchangeRateCode.substring(0,3);
//            String targetCurrencyCode = exchangeRateCode.substring(3, 6);
//            if (database.openConnection()) {
//                ExchangeRate exchangeRate = repository.findExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
//                if (exchangeRate != null) {
//                    resp.setStatus(200); // Успех
//                    jsonData = gson.toJson(exchangeRate);
//                    pw.write(jsonData);
//                } else {
//                    resp.setStatus(404);
//                }
//            } else {
//                resp.setStatus(500);
//            }
//            database.closeConnection();
//
//
//        }

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
        resp.setContentType("application/x-www-form-urlencoded");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        String exchangeRateCode = Utils.getExchangeRateCodeFromURL(pathInfo);

        double rate = Double.parseDouble(req.getParameter("rate"));

        try {
            if (!ValidationUtil.isExchangeRateCode(exchangeRateCode, rate)) {
                throw new ValidationException("Incorrect exchange rate or not enough parameters!");
            }
            String baseCurrencyCode = exchangeRateCode.substring(0,3);
            String targetCurrencyCode = exchangeRateCode.substring(3, 6);

            if (currencyDAO.findCurrencyByCode(baseCurrencyCode) != null &&
            currencyDAO.findCurrencyByCode(targetCurrencyCode) != null) {
                exchangeRateDao.updateRate(baseCurrencyCode, targetCurrencyCode, rate);
            } else {
                ResponseHandlingUtil.currencyNotFoundMessage(resp);
            }
        } catch (ValidationException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
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
