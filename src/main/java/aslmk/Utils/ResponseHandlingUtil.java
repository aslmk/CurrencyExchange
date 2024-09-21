package aslmk.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseHandlingUtil {

    public static void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        PrintWriter pw = resp.getWriter();
        pw.write("{\"message\": \"" + message + "\"}");
        pw.flush();
    }

    public static void dataBaseMessage(HttpServletResponse resp) throws IOException {
        sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something wrong with database!");
    }
    public static void currencyNotFoundMessage(HttpServletResponse resp) throws IOException {
        sendError(resp, HttpServletResponse.SC_NOT_FOUND, "Currency not found!");
    }
    public void notEnoughParametersMessage(HttpServletResponse resp) throws IOException {
        sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Not enough parameters!");
    }
    public void alreadyExistsMessage(HttpServletResponse resp) throws IOException {
        sendError(resp, HttpServletResponse.SC_CONFLICT, "Currency already exists!");
    }

}
