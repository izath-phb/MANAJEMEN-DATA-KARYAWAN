package app;

import java.util.Locale;
import java.util.ResourceBundle;

public class MultiLanguage {

    public static String getMessage(String key, String languageCode) {
        Locale locale = new Locale(languageCode);
        ResourceBundle bundle = ResourceBundle.getBundle("resources.resources", locale);
        return bundle.getString(key);
    }
}
