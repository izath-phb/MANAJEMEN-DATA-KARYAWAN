package util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangUtil {
    private static ResourceBundle bundle;

    public static void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        bundle = ResourceBundle.getBundle("lang", locale);
    }

    public static String get(String key) {
        return bundle.getString(key);
    }
}
