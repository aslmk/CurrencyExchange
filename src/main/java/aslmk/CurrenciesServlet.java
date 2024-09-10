package aslmk;

import com.google.gson.Gson;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CurrenciesServlet extends HttpServlet {
    private Database database = new Database();
    private Repository repository = new Repository(database);
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        Gson gson = new Gson();

        String jsonData;
        if (database.openConnection()) {
            jsonData = gson.toJson(repository.getCurrencies());
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

        String currencyFullName = req.getParameter("name");
        String currencyCode = req.getParameter("code");
        String currencySign = req.getParameter("sign");

        // если нужно поле формы отсутсвует, то вернуть статус 400
        if ((currencyFullName == null || currencyFullName.equals("")) ||
            (currencyCode == null || currencyCode.equals("")) ||
            (currencySign == null || currencySign.equals(""))) {
            resp.setStatus(400);
        } else {
            if (database.openConnection()) {
                if (repository.addCurrency(currencyFullName, currencyCode, currencySign)) resp.setStatus(201);
                else resp.setStatus(409);// if true then status 201 else 409
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
