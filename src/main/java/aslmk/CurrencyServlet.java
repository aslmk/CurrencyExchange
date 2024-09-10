package aslmk;

import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class CurrencyServlet extends HttpServlet {
    private Database database = new Database();
    private Repository repository = new Repository(database);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        Gson gson = new Gson();

        String pathInfo = req.getPathInfo();
        String currencyCode = getCurrencyCodeFromURL(pathInfo);
        Currency targetCurrency;
        String jsonData;

        if (database.openConnection()) {
            if (currencyCode.equals("")) {
                resp.setStatus(400); // Код валюты отсутствует в адресе
            } else {
                targetCurrency = repository.findCurrencyByCode(currencyCode);
                if (targetCurrency != null) {
                    resp.setStatus(200); // Успех
                    jsonData = gson.toJson(targetCurrency);
                    pw.write(jsonData);
                } else resp.setStatus(404); // Валюта не найдена
            }
        }  else {
            resp.setStatus(500); // Ошибка (например, база данных недоступна)
        }
        database.closeConnection();
    }
    private String getCurrencyCodeFromURL(String url) {
        if (url == null || url.equals("/")) {
            return "";
        }
        String currencyUrl = url.substring(1);

        if (currencyUrl.contains("/")) {
            //currencyUrl = currencyUrl.substring(, url.indexOf("/"));
            return "";
        }
        return currencyUrl;

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
