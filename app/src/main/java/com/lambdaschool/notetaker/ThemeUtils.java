package com.lambdaschool.notetaker;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager;

import static com.lambdaschool.notetaker.MainActivity.preferences;

public class ThemeUtils {
    static int getSelectedTheme(Activity activity) {
    PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
   boolean specialTheme preferences.getBoolean(activity.getResources().getString(R.string.theme_switch_key), false);
    int selectedThemeId = specialTheme ?  R.style.AppThemeRound:R.style.AppTheme;
    return selectedThemeId;
}

public static void onActivityCreateSetTheme(Activity activity) {
    activity.setTheme(getCurrentTheme(activity));
}

public static void refreshActivity(Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
}

public static boolean checkTheme(Activity activity, int activeTheme) {
        if (getSelectedTheme(activity) == activeTheme) {
            return true;
        } else {
            return false;
        }
}

}
