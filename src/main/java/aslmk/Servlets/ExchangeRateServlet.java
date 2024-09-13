package aslmk.Servlets;

import aslmk.Database;
import aslmk.Models.ExchangeRate;
import aslmk.Repository;
import aslmk.Utils.Utils;
import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ExchangeRateServlet extends HttpServlet {
    Database database = new Database();
    Repository repository = new Repository(database);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        Gson gson = new Gson();

        String pathInfo = req.getPathInfo();
        String exchangeRateCode = Utils.getExchangeRateCodeFromURL(pathInfo);
        String jsonData;

        if (exchangeRateCode.length() != 6) {
            resp.setStatus(400);
        }
        else {
            String baseCurrencyCode = exchangeRateCode.substring(0,3);
            String targetCurrencyCode = exchangeRateCode.substring(3, 6);
            if (database.openConnection()) {
                ExchangeRate exchangeRate = repository.findExchangeRateByCode(baseCurrencyCode, targetCurrencyCode);
                if (exchangeRate != null) {
                    resp.setStatus(200); // Успех
                    jsonData = gson.toJson(exchangeRate);
                    pw.write(jsonData);
                } else {
                    resp.setStatus(404);
                }
            } else {
                resp.setStatus(500);
            }
            database.closeConnection();


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
        resp.setContentType("application/x-www-form-urlencoded");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        String exchangeRateCode = Utils.getExchangeRateCodeFromURL(pathInfo);

        double rate = Double.parseDouble(req.getParameter("rate"));

        if (exchangeRateCode.length() != 6 && Double.isNaN(rate) || Double.toString(rate).equals("")) {
            resp.setStatus(400);
        } else {
            String baseCurrencyCode = exchangeRateCode.substring(0,3);
            String targetCurrencyCode = exchangeRateCode.substring(3, 6);

            if (database.openConnection()) {
                if (repository.findCurrencyByCode(baseCurrencyCode) != null &&
                        repository.findCurrencyByCode(targetCurrencyCode) != null) {
                    if (repository.updateRate(baseCurrencyCode, targetCurrencyCode, rate)) {
                        resp.setStatus(200);
                    }
                } else {
                    resp.setStatus(404);
                }
            } else {
                resp.setStatus(500);
            }
            database.closeConnection();
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
