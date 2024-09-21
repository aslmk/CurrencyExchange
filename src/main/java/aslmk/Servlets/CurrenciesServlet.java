package aslmk.Servlets;

import aslmk.DAO.CurrencyDAO;
import aslmk.Utils.ResponseHandlingUtil;
import aslmk.Utils.Utils;
import aslmk.Utils.ValidationException;
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
        String currencyCode = req.getParameter("code");
        String currencySign = req.getParameter("sign");

        try {
            if (!ValidationUtil.isParameters(currencyFullName, currencyCode, currencySign)) {
                throw new ValidationException();
            }
            currencyDAO.addCurrency(currencyFullName, currencyCode, currencySign);
            Utils.postResponse(resp, 201);
        } catch (ValidationException e) {
            ResponseHandlingUtil.notEnoughParametersMessage(resp);
        } catch (SQLException e) {
            int errorCode = e.getErrorCode();
            if (errorCode == 14) { // SQL Internal server error
                ResponseHandlingUtil.dataBaseMessage(resp);
            } else if (errorCode == 19) { // SQL constraint
                ResponseHandlingUtil.alreadyExistsMessage(resp);
            }
        }

//        else {
//            if (database.openConnection()) {
//                if (repository.addCurrency(currencyFullName, currencyCode, currencySign)) resp.setStatus(201);
//                else resp.setStatus(409);// if true then status 201 else 409
//            } else {
//                resp.setStatus(500);
//            }
//            database.closeConnection();
//        }

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
