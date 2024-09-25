package aslmk.Utils;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Utils {
    public static String extractCodeFromURL(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }
        String substring = url.substring(1);

        if (substring.isEmpty() || substring.contains("/")) {
            return null;
        }
        return substring;

    }

    public static void setResponse(HttpServletResponse resp, Object object) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        Gson gson = new Gson();
        pw.write(gson.toJson(object));
        pw.flush();
    }
    public static void postResponse(HttpServletResponse resp, int statusCode, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/x-www-form-urlencoded");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        pw.write("{\"message\": \"" + message + "\"}");
        pw.flush();
    }
    public static void setResponse(HttpServletResponse resp, Object object, int statusCode, String contentType, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType(contentType);
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        PrintWriter pw = resp.getWriter();
        pw.write("{\"message\": \"" + message + "\"}");
        pw.write(gson.toJson(object));
        pw.flush();
    }
    public static void setResponse(HttpServletResponse resp, Object object, int statusCode, String contentType) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType(contentType);
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();
        PrintWriter pw = resp.getWriter();
        pw.write(gson.toJson(object));
        pw.flush();
    }
    public static void setResponse(HttpServletResponse resp, int statusCode, String contentType, String message) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType(contentType);
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        pw.write("{\"message\": \"" + message + "\"}");
        pw.flush();
    }
}
