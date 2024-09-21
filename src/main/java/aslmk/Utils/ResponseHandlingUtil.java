package aslmk.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseHandlingUtil {
    public static void dbException(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something wrong with database!");
    }
    public static void currencyNotFoundException(HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter pw = resp.getWriter();
        pw.write("{\"message\": \"Currency not found!\"}");
        pw.flush();
    }
    public void notEnoughParameters(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not enough parameters!");
    }
    public void alreadyExists(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_CONFLICT, "Currency already exists!");
    }

}
