package net.gahfy.nsamples.utils;

import android.app.Notification;
import android.content.Context;
import android.os.Build;
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

    /**
     * Builds a Notification.
     * @param builder the Builder to use to build the notification
     * @return The notification that has been built
     */
    public static Notification buildNotification(Notification.Builder builder){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            return builder.build();
        return buildNotificationBeforeJellyBean(builder);
    }

    /**
     * Builds a Notification for versions before Jelly Beans (that does not have build() method.
     * @param builder the Builder to use to build the notification
     * @return The notification that has been built
     */
    @SuppressWarnings("deprecation")
    private static Notification buildNotificationBeforeJellyBean(Notification.Builder builder){
        return builder.getNotification();
    }
}
