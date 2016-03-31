package net.gahfy.nsamples.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.gahfy.nsamples.R;
import net.gahfy.nsamples.receivers.LaunchToastReceiver;
import net.gahfy.nsamples.utils.UIUtils;

/**
 * This Activity shows the new features of Notifications
 */
public class NotificationsActivity extends AppCompatActivity {
    /** The key of the typed text in the RemoteInput */
    public static final String NOTIFICATION_KEY_LAUNCH_TOAST = "toast_content";

    /** The identifier of the notification for Toast */
    public static final int NOTIFICATION_ID_LAUNCH_TOAST = 1;
    /** The identifier of the notification for Mails */
    public static final int NOTIFICATION_ID_MAIL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Cancel all notifications related to this activity
        cancelNotifications();

        // Setting the UI elements in the view
        final EditText txtLaunchToast = (EditText) findViewById(R.id.txt_launch_toast);
        Button btLaunchToast = (Button) findViewById(R.id.bt_launch_toast);
        Button btLaunchNotificationSendToast = (Button) findViewById(R.id.bt_launch_notification_send_toast);
        Button btLaunchNotificationEmail = (Button) findViewById(R.id.bt_launch_notification_email);

        // Setting the actions of the buttons
        if(btLaunchToast != null)
            btLaunchToast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(txtLaunchToast != null)
                        UIUtils.sendShortToast(NotificationsActivity.this, txtLaunchToast.getText().toString());
                }
            });

        if(btLaunchNotificationSendToast != null)
            btLaunchNotificationSendToast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchSendToastNotification();
                }
            });

        if(btLaunchNotificationEmail != null)
            btLaunchNotificationEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchMailNotification();
                }
            });
    }

    /**
     * Cancels all the notifications related to this Activity
     */
    public void cancelNotifications(){
        // Cancel the notifications
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID_LAUNCH_TOAST);
        notificationManager.cancel(NOTIFICATION_ID_MAIL);
    }

    /**
     * Launches a notification to notify the user he received messages.
     */
    private void launchMailNotification(){
        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_mail_white_24dp)
                .setContentTitle(getString(R.string.mail_notification_title))
                .setContentText(getString(R.string.mail_notification_title));

        // Builds a multiline notification
        Notification.InboxStyle inboxStyle =
                new Notification.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.mail_notification_title));
        for (int i=0; i < 3; i++) {
            inboxStyle.addLine(String.format("%s - %s", getResources().getStringArray(R.array.mail_senders)[i], getResources().getStringArray(R.array.mail_preview)[i]));
        }
        mBuilder.setStyle(inboxStyle);

        // The activity that the notification will redirect to
        Intent resultIntent = new Intent(this, NotificationsActivity.class);

        // The stackbuilder here allow to leave the application when pressing return
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Finally launching the notification
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID_MAIL, UIUtils.buildNotification(mBuilder));

    }

    /**
     * Launches a Notification to notify the user he's allowed to send a toast
     */
    private void launchSendToastNotification(){
        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_mail_white_24dp)
                        .setContentTitle(getString(R.string.launch_toast_notification_title))
                        .setContentText(getString(R.string.launch_toast_notification_text));

        if(Build.VERSION.RELEASE.startsWith("N") || Build.VERSION.RELEASE.startsWith("7")) {
            // We instantiate a RemoteInput. We will be able to retrieved typed text with
            // the key NOTIFICATION_KEY_LAUNCH_TOAST
            RemoteInput remoteInput = new RemoteInput.Builder(NOTIFICATION_KEY_LAUNCH_TOAST)
                    .setLabel(getString(R.string.write_toast))
                    .build();

            // We instantiate a new action to which we add the RemoteInput
            // Adding RemoteInput to Android < N will produce nothing (no crash, but no text field)
            Icon icon = Icon.createWithResource(this.getApplicationContext(), R.drawable.ic_mail_white_24dp);
            Notification.Action action =
                    new Notification.Action.Builder(icon,
                            getString(R.string.launch_toast_action), getToastBroadcastPendingIntent())
                            .addRemoteInput(remoteInput)
                            .build();

            // We finally add the action to the notification Builder
            mBuilder.addAction(action);
        }

        // The activity that the notification will redirect to
        Intent resultIntent = new Intent(this, NotificationsActivity.class);

        // The stackbuilder here allow to leave the application when pressing return
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(NotificationsActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // Finally launching the notification
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID_LAUNCH_TOAST, UIUtils.buildNotification(mBuilder));
    }

    /**
     * Returns the PendingIntent to send to launch a broadcast.
     * @return the PendingIntent to send to launch a broadcast
     */
    private PendingIntent getToastBroadcastPendingIntent(){
        Intent intent = new Intent(this, LaunchToastReceiver.class);
        return PendingIntent.getBroadcast(this.getApplicationContext(), 0, intent, 0);
    }
}
