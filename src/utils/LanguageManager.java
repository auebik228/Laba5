package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {
    private static Locale currentLocale = new Locale("en", "AU");
    private static ResourceBundle bundle = ResourceBundle.getBundle("resources/locales/messages", currentLocale);
    private static List<LanguageChangeListener> listeners = new ArrayList<>();

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("resources/locales/messages", currentLocale);
        notifyListeners();
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static String getString(String key) {
        return bundle.getString(key);
    }

    public static void addLanguageChangeListener(LanguageChangeListener listener) {
        listeners.add(listener);
    }

    private static void notifyListeners() {
        for (LanguageChangeListener listener : listeners) {
            listener.onLanguageChange(currentLocale);
        }
    }
}
