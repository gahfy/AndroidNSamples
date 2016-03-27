package net.gahfy.nsamples.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.gahfy.nsamples.R;
import net.gahfy.nsamples.utils.UIUtils;

/**
 * This Activity shows the new features of Notifications
 */
public class NotificationsActivity extends AppCompatActivity {
    private static final int NOTIFICATION_ID_LAUNCH_TOAST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Cancel all notifications related to this activity
        cancelNotifications();

        // Setting the UI elements in the view
        final EditText txtLaunchToast = (EditText) findViewById(R.id.txt_launch_toast);
        Button btLaunchToast = (Button) findViewById(R.id.bt_launch_toast);
        Button btLaunchNotification = (Button) findViewById(R.id.bt_launch_notification);

        // Setting the actions of the buttons
        if(btLaunchToast != null)
        btLaunchToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtLaunchToast != null)
                    UIUtils.sendShortToast(NotificationsActivity.this, txtLaunchToast.getText().toString());
            }
        });

        if(btLaunchNotification != null)
            btLaunchNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchSendToastNotification();
                }
            });
    }

    /**
     * Cancels all the notifications related to this Activity
     */
    public void cancelNotifications(){
        // Clear the notifications
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID_LAUNCH_TOAST);
    }

    /**
     * Launches a Notification that redirects to this Activity
     */
    public void launchSendToastNotification(){
        Notification.Builder mBuilder =
                new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_mail_white_24dp)
                        .setContentTitle(getString(R.string.launch_toast_notification_title))
                        .setContentText(getString(R.string.launch_toast_notification_text));

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
}
