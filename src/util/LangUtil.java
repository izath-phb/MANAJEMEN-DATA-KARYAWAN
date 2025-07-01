package util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangUtil {
    private static ResourceBundle bundle;

    public static void setBundle(String baseName, Locale locale) {
        System.out.println("DEBUG: setBundle called with baseName: " + baseName + ", locale: " + locale); // Baris ini
        try {
            bundle = ResourceBundle.getBundle(baseName, locale, LangUtil.class.getClassLoader());
            System.out.println("DEBUG: Successfully loaded bundle for locale: " + bundle.getLocale().getLanguage()); // Baris ini
        } catch (java.util.MissingResourceException e) {
            System.err.println("ERROR: MissingResourceException in LangUtil.setBundle for baseName: " + baseName + ", locale: " + locale); // Baris ini
            e.printStackTrace(); // Penting untuk melihat stack trace penuh jika terjadi lagi
        }
    }

    public static String get(String key) {
        if (bundle == null) {
            System.err.println("ERROR: LangUtil.bundle is NULL when trying to get key: " + key); // Baris ini
            return "Bundle not set!";
        }
        try {
            String value = bundle.getString(key);
            // System.out.println("DEBUG: Get key: '" + key + "' -> '" + value + "' (from locale: " + bundle.getLocale().getLanguage() + ")"); // Opsional, jika ingin lebih detail
            return value;
        } catch (java.util.MissingResourceException e) {
            System.err.println("ERROR: Key '" + key + "' not found in bundle for locale: " + bundle.getLocale().getLanguage()); // Baris ini
            return "?" + key + "?";
        }
    }
}