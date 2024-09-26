package aslmk.Servlets;

import aslmk.DAO.ExchangeDAO;
import aslmk.DTO.ExchangeDTO;
import aslmk.Utils.*;
import aslmk.Utils.Exceptions.ExchangeRateNotFoundException;
import aslmk.Utils.Exceptions.ValidationException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ExchangeServlet extends HttpServlet {
    ExchangeDAO exchangeDAO = new ExchangeDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fromCurrency = req.getParameter("from").toUpperCase().trim();
        String toCurrency = req.getParameter("to").toUpperCase().trim();

        try {
            double amount = Double.parseDouble(req.getParameter("amount"));

            if (!ValidationUtil.isExchangeRateParametersValid(fromCurrency, toCurrency, amount)) {
                throw new ValidationException("Incorrect parameters!");
            } else {
                ExchangeDTO exchangeDTO = exchangeDAO.exchange(fromCurrency, toCurrency, amount);
                if (exchangeDTO != null) {
                    Utils.setResponse(resp, exchangeDTO, HttpServletResponse.SC_OK, "application/x-www-form-urlencoded");
                }
            }
        } catch (ValidationException | NumberFormatException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
        } catch (ExchangeRateNotFoundException e) {
            ResponseHandlingUtil.sendError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
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
