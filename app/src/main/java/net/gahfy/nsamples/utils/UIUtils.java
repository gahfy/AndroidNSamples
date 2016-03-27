package net.gahfy.nsamples.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * This class provides useful methods regarding the UI.
 */
public class UIUtils {
    /**
     * Displays a Toast for a short delay on the UI.
     * @param context Context in which the application is running
     * @param toastContent The text content of the Toast to display
     */
    public static void sendShortToast(Context context, String toastContent){
        Toast.makeText(context, toastContent, Toast.LENGTH_SHORT).show();
    }
}
