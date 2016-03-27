package net.gahfy.nsamples.receivers;

import android.app.NotificationManager;
import android.app.RemoteInput;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import net.gahfy.nsamples.activities.NotificationsActivity;
import net.gahfy.nsamples.utils.UIUtils;

/**
 * This class is a receiver for printing a Toast on the screen
 */
public class LaunchToastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        // We get the remote from the Intent
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            // We get the typed text with the key
            CharSequence toastContent = remoteInput.getCharSequence(NotificationsActivity.NOTIFICATION_KEY_LAUNCH_TOAST);
            UIUtils.sendShortToast(context, toastContent != null ? toastContent.toString() : "null");
        }
        // Suppression de la notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NotificationsActivity.NOTIFICATION_ID_LAUNCH_TOAST);
    }
}
