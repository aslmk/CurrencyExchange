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
    public static boolean isExchangeRateCode(String code) {
        return code != null && code.length() == 6 && code.matches("[A-Z]{6}");
    }
    public static boolean isExchangeRateCode(String code, Double rate) {
        return code != null && code.length() == 6 && code.matches("[A-Z]{6}") && !Double.isNaN(rate) && !Double.toString(rate).equals("");
    }
    public static boolean isExchangeRateParameters(String baseCurrencyCode, String targetCurrencyCode, Double rate) {
        return (baseCurrencyCode != null && !baseCurrencyCode.equals("")) &&
                (targetCurrencyCode != null && !targetCurrencyCode.equals("")) &&
                (!Double.isNaN(rate) && !Double.toString(rate).equals(""));
    }
}
