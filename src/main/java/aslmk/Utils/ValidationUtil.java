package aslmk.Utils;

public class ValidationUtil {
    public static boolean isCurrencyCode(String code) {
        return code != null && code.length() == 3 && code.matches("[A-Z]{3}");
    }
    public static boolean isParameters(String currencyFullName, String currencyCode, String currencySign) {
        return (currencyFullName != null && !currencyFullName.equals("")) &&
                (currencyCode != null && !currencyCode.equals("")) &&
                (currencySign != null && !currencySign.equals(""));
    }
}
