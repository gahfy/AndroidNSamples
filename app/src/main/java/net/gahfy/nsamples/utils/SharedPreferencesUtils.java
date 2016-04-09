package net.gahfy.nsamples.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class is a library of useful methods about Shared Preferences
 */
public class SharedPreferencesUtils {
    /** The name of the shared preferences of the application */
    private static final String PREFERENCES_NAME = "net.gahfy.nsamples.preferences";

    /**
     * The name of the preference in which is stored whether the notifications are enabled or not.
     */
    private static final String PREFERENCE_NAME_NOTIFICATIONS_ENABLED = "NotificationsEnabled";

    /**
     * Sets whether the notifications are enabled or not.
     * @param context Context in which the application is running
     * @param notificationsEnabled Whether the notifications are enabled or not to set
     */
    public static void putNotificationsEnabled(Context context, boolean notificationsEnabled){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFERENCE_NAME_NOTIFICATIONS_ENABLED, notificationsEnabled);
        editor.apply();
    }

    /**
     * Returns whether the notifications are enabled or not.
     * @param context Context in which the application is running
     * @return Whether the notifications are enabled or not
     */
    public static boolean getNotificationsEnabled(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREFERENCE_NAME_NOTIFICATIONS_ENABLED, false);
    }
}
