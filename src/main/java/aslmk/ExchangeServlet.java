package aslmk;

import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ExchangeServlet extends HttpServlet {
    private Database database = new Database();
    private  Repository repository = new Repository(database);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/x-www-form-urlencoded");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        Gson gson = new Gson();
        String jsonData;

        String fromCurrency = req.getParameter("from");
        String toCurrency = req.getParameter("to");
        double amount = Double.parseDouble(req.getParameter("amount"));

        if (fromCurrency == null || fromCurrency.equals("") &&
                toCurrency == null || toCurrency.equals("") &&
                Double.isNaN(amount) || Double.toString(amount).equals("")) {
            resp.setStatus(400);
        } else {
            if (database.openConnection()) {
                if (repository.findCurrencyByCode(fromCurrency) != null &&
                        repository.findCurrencyByCode(toCurrency) != null) {
                    Exchange exchange = repository.exchange(fromCurrency, toCurrency, amount);
                    if (exchange != null) {
                        resp.setStatus(201);
                        jsonData = gson.toJson(exchange);
                        pw.write(jsonData);
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
