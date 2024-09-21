package aslmk.Utils;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Utils {
    public static String getCurrencyCodeFromURL(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        String currencyUrl = url.substring(1);

        if (currencyUrl.isEmpty() || currencyUrl.contains("/")) {
            return null;
        }
        return currencyUrl;

    }
    public static String getExchangeRateCodeFromURL(String url) {
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

    public static void setResponse(HttpServletResponse resp, Object object) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        Gson gson = new Gson();
        pw.write(gson.toJson(object));
    }
    public static void postResponse(HttpServletResponse resp, int statusCode) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/x-www-form-urlencoded");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        pw.write("{\"message\": \"" + "Currency successfully added!" + "\"}");
        pw.flush();
    }
}
