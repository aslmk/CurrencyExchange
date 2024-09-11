package aslmk;

import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ExchangeRatesServlet extends HttpServlet {
    Database database = new Database();
    Repository repository = new Repository(database);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        Gson gson = new Gson();

        String jsonData;
        if (database.openConnection()) {
            jsonData = gson.toJson(repository.getExchangeRates());
            resp.setStatus(200);
            pw.write(jsonData);
        } else {
            resp.setStatus(500);
        }
        database.closeConnection();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/x-www-form-urlencoded");
        resp.setCharacterEncoding("UTF-8");

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        double rate = Double.parseDouble(req.getParameter("rate"));

        if (baseCurrencyCode == null || baseCurrencyCode.equals("") &&
        targetCurrencyCode == null || targetCurrencyCode.equals("") &&
                Double.isNaN(rate) || Double.toString(rate).equals("")) {
            resp.setStatus(400);
        } else {
            if (database.openConnection()) {
                if (repository.findCurrencyByCode(baseCurrencyCode) != null &&
                        repository.findCurrencyByCode(targetCurrencyCode) != null) {

                    if (repository.addExchangeRate(baseCurrencyCode, targetCurrencyCode, rate)) {
                        resp.setStatus(201);
                    } else {
                        resp.setStatus(409);
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
