package aslmk.Utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseHandlingUtil {
    public void dbException(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something wrong with database!");
    }
    public void currencyNotFoundException(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Currency not found!");
    }
    public void notEnoughParameters(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Not enough parameters!");
    }
    public void alreadyExists(HttpServletResponse resp) throws IOException {
        resp.sendError(HttpServletResponse.SC_CONFLICT, "Currency already exists!");
    }
}
