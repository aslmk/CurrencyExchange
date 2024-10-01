package aslmk.Servlets;

import aslmk.Models.Currency;
import aslmk.Service.Impl.CurrencyServiceImpl;
import aslmk.Utils.Exceptions.CurrencyNotFoundException;
import aslmk.Utils.Exceptions.DatabaseException;
import aslmk.Utils.Exceptions.ValidationException;
import aslmk.Utils.ResponseHandlingUtil;
import aslmk.Utils.Utils;
import aslmk.Utils.ValidationUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class CurrencyServlet extends HttpServlet {
    private CurrencyServiceImpl currencyService = new CurrencyServiceImpl();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo().toUpperCase().trim();
        String currencyCode = Utils.extractCodeFromURL(pathInfo);

        try {
            if (!ValidationUtil.isCurrencyCodeValid(currencyCode)) {
                throw new ValidationException("Invalid currency code.");
            }
            Currency targetCurrency = currencyService.findCurrencyByCode(currencyCode);
            if (targetCurrency != null) {
                Utils.setResponse(resp, targetCurrency);
            } else {
                throw new CurrencyNotFoundException("Currency not found.");
            }
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        } catch (ValidationException e) {
            ResponseHandlingUtil.sendError(resp, resp.SC_BAD_REQUEST, e.getMessage());
        } catch (CurrencyNotFoundException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
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
