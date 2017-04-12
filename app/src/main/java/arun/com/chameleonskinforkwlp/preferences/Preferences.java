package arun.com.chameleonskinforkwlp.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

public class Preferences {
    public static final String THEME_PREF = "theme_preference";
    public static final int LOLLIPOP = 0;
    public static final int MARSHMALLOW = 1;
    private static Preferences INSTANCE;

    private final Context context;

    public Preferences(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    public static synchronized Preferences get(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new Preferences(context);
        }
        return INSTANCE;
    }

    /**
     * Returns default shared preferences.
     *
     * @return {@link SharedPreferences} instance
     */
    @NonNull
    private SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setTheme(int theme) {
        getDefaultSharedPreferences().edit().putInt(THEME_PREF, theme).apply();
    }

    public int getTheme() {
        return getDefaultSharedPreferences().getInt(THEME_PREF, LOLLIPOP);
    }

    public int toggleTheme() {
        if (getTheme() == LOLLIPOP) {
            setTheme(MARSHMALLOW);
        } else {
            setTheme(LOLLIPOP);
        }
        return getTheme();
    }
}
