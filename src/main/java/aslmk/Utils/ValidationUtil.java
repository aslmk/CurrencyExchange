package aslmk.Utils;

public class ValidationUtil {
    public static boolean isCurrencyCode(String code) {
        return code != null && code.length() == 3 && code.matches("[A-Z]{3}");

    }
}
