package util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LangUtil {
    private static ResourceBundle bundle;

    /**
     * Mengatur ResourceBundle aktif berdasarkan base name dan kode bahasa.
     * @param baseName Nama dasar file properti (misalnya, "lang" atau "messages")
     * @param locale Objek Locale untuk bahasa yang diinginkan
     */
    public static void setBundle(String baseName, Locale locale) {
        // ClassLoader diperlukan untuk memastikan file properti ditemukan
        // terlepas dari struktur package.
        bundle = ResourceBundle.getBundle(baseName, locale, LangUtil.class.getClassLoader());
    }

    /**
     * Mendapatkan string dari ResourceBundle yang sedang aktif.
     * @param key Kunci dari string yang ingin diambil
     * @return Nilai string dari kunci yang diberikan
     */
    public static String get(String key) {
        if (bundle == null) {
            return "Bundle not set!";
        }
        try {
            return bundle.getString(key);
        } catch (java.util.MissingResourceException e) {
            // Mengembalikan penanda yang jelas jika kunci tidak ditemukan
            return "?" + key + "?";
        }
    }
}