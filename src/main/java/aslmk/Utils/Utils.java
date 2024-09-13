package aslmk.Utils;

public class Utils {
    public static String getCurrencyCodeFromURL(String url) {
        if (url == null || url.equals("/")) {
            return "";
        }
        String currencyUrl = url.substring(1);

        if (currencyUrl.contains("/")) {
            return "";
        }
        return currencyUrl;

    }
}
