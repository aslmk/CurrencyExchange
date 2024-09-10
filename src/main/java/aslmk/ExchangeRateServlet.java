package aslmk;

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
        String exchangeRateCode = getExchangeRateCodeFromURL(pathInfo);
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

    private String getExchangeRateCodeFromURL(String url) {
        if (url == null || url.equals("/")) {
            return "";
        }
        String exchangeRateUrl = url.substring(1);

        if (exchangeRateUrl.contains("/")) {
            //currencyUrl = currencyUrl.substring(, url.indexOf("/"));
            return "";
        }
        return exchangeRateUrl;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
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
