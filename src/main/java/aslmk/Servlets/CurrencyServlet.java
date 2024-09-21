package aslmk.Servlets;

import aslmk.DAO.CurrencyDAO;
import aslmk.Database;
import aslmk.Models.Currency;
import aslmk.Repository;
import aslmk.Service.CurrencyService;
import aslmk.Utils.Exceptions.ValidationException;
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
    private Database database = new Database();
    private Repository repository = new Repository(database);
    private CurrencyDAO currencyDAO = new CurrencyDAO();
    private CurrencyService currencyService = new CurrencyService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        Gson gson = new Gson();
        String jsonData = "";
        String pathInfo = req.getPathInfo();
        String currencyCode = Utils.getCurrencyCodeFromURL(pathInfo);

        try {
            if (!ValidationUtil.isCurrencyCode(currencyCode)) {
                throw new ValidationException("It is not currency code!");
            }
            Currency targetCurrency = currencyDAO.findCurrencyByCode(currencyCode);
            if (targetCurrency != null) {
                jsonData = gson.toJson(targetCurrency);
            } else {
                ResponseHandlingUtil.currencyNotFoundMessage(resp);
                return;

            }
        } catch (SQLException e) {
            ResponseHandlingUtil.dataBaseMessage(resp);
            return;
        } catch (ValidationException e) {
            ResponseHandlingUtil.sendError(resp, 400, "It is not currency code!");
        }

        pw.write(jsonData);
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
