package aslmk.Servlets;

import aslmk.DAO.CurrencyDAO;
import aslmk.Models.Currency;
import aslmk.Service.CurrencyService;
import aslmk.Utils.ValidationException;
import aslmk.Utils.ResponseHandlingUtil;
import aslmk.Utils.Utils;
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

public class CurrencyServlet extends HttpServlet {
    private CurrencyDAO currencyDAO = new CurrencyDAO();
    private CurrencyService currencyService = new CurrencyService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String currencyCode = Utils.getCurrencyCodeFromURL(pathInfo);

        try {
            if (!ValidationUtil.isCurrencyCode(currencyCode)) {
                throw new ValidationException("It is not currency code!");
            }
            Currency targetCurrency = currencyDAO.findCurrencyByCode(currencyCode);
            if (targetCurrency != null) {
                Utils.setResponse(resp, targetCurrency);
            } else {
                ResponseHandlingUtil.currencyNotFoundMessage(resp);
            }
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        } catch (ValidationException e) {
            ResponseHandlingUtil.sendError(resp, resp.SC_BAD_REQUEST, e.getMessage());
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
