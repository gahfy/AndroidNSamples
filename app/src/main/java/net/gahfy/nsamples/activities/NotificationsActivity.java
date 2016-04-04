package net.gahfy.nsamples.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;

import net.gahfy.nsamples.R;
import net.gahfy.nsamples.receivers.LaunchToastReceiver;
import net.gahfy.nsamples.utils.NVersionUtils;
import net.gahfy.nsamples.utils.UIUtils;

/**
 * This Activity shows the new features of Notifications
 */
public class NotificationsActivity extends AppCompatActivity {
    /** The key of the typed text in the RemoteInput */
    public static final String NOTIFICATION_KEY_LAUNCH_TOAST = "toast_content";

    /** The list of emails used in notifications */
    private static final GahfyMail[] ACTIVITY_MAILS = new GahfyMail[]{
        new GahfyMail(1, "John Doe", "Call me back", "Lorem ipsum"),
        new GahfyMail(2, "Foo Bar", "Check it now", "Dolor sit"),
        new GahfyMail(3, "Gahfy", "News from the blog", "Amet Consectetur")
    };

    /** The notification manager in use for this Activity*/
    private NotificationManager mNotificationManager;

    /** The identifier of the notification for Toast */
    public static final int NOTIFICATION_ID_LAUNCH_TOAST = 1;
    /** The identifier of the notification for Mails */
    public static final int NOTIFICATION_ID_MAIL = 2;
    /** The identifier of the notification for Bank */
    public static final int NOTIFICATION_ID_BANK = 3;
    /** The base identifier, to which the id of mail will be added, for single mail notifications */
    public static final int NOTIFICATION_ID_SINGLE_MAIL_BASE = 1000;

    /** Unique key for the group of notifications */
    private static final String GROUP_KEY_MAIL = "group_key_mail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // Setting the notification manager
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Cancel all notifications related to this activity
        cancelNotifications();

        // Setting the UI elements in the view
        final EditText txtLaunchToast = (EditText) findViewById(R.id.txt_launch_toast);
        Button btLaunchToast = (Button) findViewById(R.id.bt_launch_toast);
        Button btLaunchNotificationSendToast = (Button) findViewById(R.id.bt_launch_notification_send_toast);
        Button btLaunchNotificationEmail = (Button) findViewById(R.id.bt_launch_notification_email);
        Button btLaunchNotificationBank = (Button) findViewById(R.id.bt_launch_notification_bank);

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

        if(btLaunchNotificationBank != null)
            btLaunchNotificationBank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    launchBankOperationNotification();
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
        notificationManager.cancel(NOTIFICATION_ID_BANK);
    }

    /**
     * Launches a notification to notify the user of a new operation on the bank account.
     */
    private void launchBankOperationNotification(){
        double amount = 12.34;

        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_account_balance_white_24dp);

        if(NVersionUtils.isAtLeastN()) {
            // Instantiate the RemoteViews with dedicated layout
            RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_bank_operation);
            // Change the content of the TextView
            remoteViews.setTextViewText(R.id.lbl_operation_amount, getString(R.string.bank_operation_custom_notification_amount_format, amount));
            // Change the color of the TextView
            remoteViews.setTextColor(R.id.lbl_operation_amount, getColor((amount >= 0.0) ? R.color.color_positive : R.color.color_negative));

            // Finalize build of the notification
            mBuilder.setStyle(new Notification.DecoratedCustomViewStyle())
                    .setCustomContentView(remoteViews);
        }
        else{
            mBuilder.setContentTitle(getString(R.string.bank_operation_notification_title))
                    .setContentText(getString(R.string.bank_operation_notification_text, amount));
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
        mNotificationManager.notify(NOTIFICATION_ID_BANK, UIUtils.buildNotification(mBuilder));
    }

    /**
     * Launches a notification to notify the user he received messages.
     */
    private void launchMailNotification(){
        String mainNotificationTitle = String.format(getResources().getQuantityString(R.plurals.mail_notification_title, ACTIVITY_MAILS.length), ACTIVITY_MAILS.length);

        Notification.Builder mBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_mail_white_24dp)
                .setContentTitle(mainNotificationTitle)
                .setContentText(mainNotificationTitle);

        if(NVersionUtils.isAtLeastN()){
            mBuilder.setGroup(GROUP_KEY_MAIL);
            mBuilder.setGroupSummary(true);
        }
        else {
            // Builds a multiline notification
            Notification.InboxStyle inboxStyle =
                    new Notification.InboxStyle();
            inboxStyle.setBigContentTitle(mainNotificationTitle);
            for (GahfyMail currentGahfyMail : ACTIVITY_MAILS) {
                inboxStyle.addLine(String.format("%s - %s", currentGahfyMail.sender, currentGahfyMail.subject));
            }
            mBuilder.setStyle(inboxStyle);
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
        mNotificationManager.notify(NOTIFICATION_ID_MAIL, UIUtils.buildNotification(mBuilder));
        if(NVersionUtils.isAtLeastN()) {
            for (GahfyMail currentGahfyMail : ACTIVITY_MAILS) {
                mBuilder = new Notification.Builder(this)
                        .setSmallIcon(R.drawable.ic_mail_white_24dp)
                        .setContentTitle(currentGahfyMail.sender)
                        .setContentText(String.format("%s - %s", currentGahfyMail.subject, currentGahfyMail.preview));
                mBuilder.setGroup(GROUP_KEY_MAIL);
                mNotificationManager.notify(NOTIFICATION_ID_SINGLE_MAIL_BASE + (int) currentGahfyMail.id, UIUtils.buildNotification(mBuilder));
            }
        }
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

        if(NVersionUtils.isAtLeastN()) {
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

    /**
     * This class is the model for emails used in bundled notifications.
     */
    static class GahfyMail{
        /** The unique identifier of the email. */
        public long id;

        /** The sender of the email. */
        public String sender;

        /** The subject of the email. */
        public String subject;

        /** The preview of the email. */
        public String preview;

        /**
         * Instantiates a new GahfyMail object.
         * @param id The unique identifier of the email to set
         * @param sender The sender of the email to set
         * @param subject The subject of the email to set
         * @param preview The preview of the email to set
         */
        GahfyMail(long id, String sender, String subject, String preview){
            this.id = id;
            this.sender = sender;
            this.subject = subject;
            this.preview = preview;
        }
    }
}
