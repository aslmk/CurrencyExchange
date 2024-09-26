package aslmk.Servlets;

import aslmk.DAO.CurrencyDAO;
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

public class CurrenciesServlet extends HttpServlet {
    CurrencyDAO currencyDAO = new CurrencyDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Utils.setResponse(resp, currencyDAO.getCurrencies());
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String currencyFullName = req.getParameter("name");
        String currencyCode = req.getParameter("code").toUpperCase().trim();
        String currencySign = req.getParameter("sign");

        try {
            if (!ValidationUtil.isCurrencyParametersValid(currencyFullName, currencyCode, currencySign)) {
                throw new ValidationException("Incorrect parameters!");
            }
            currencyDAO.addCurrency(currencyFullName, currencyCode, currencySign);
            Utils.postResponse(resp, HttpServletResponse.SC_CREATED, "Currency successfully created.");
        } catch (ValidationException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 14) { // SQL Internal server error
                ResponseHandlingUtil.dataBaseMessage(resp);
            } else if (errorCode == 19) { // SQL constraint
                ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_CONFLICT, "Currency already exists.");
            }
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
