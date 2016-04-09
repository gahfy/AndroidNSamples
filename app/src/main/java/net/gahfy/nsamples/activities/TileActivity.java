package net.gahfy.nsamples.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import net.gahfy.nsamples.R;
import net.gahfy.nsamples.utils.SharedPreferencesUtils;

/**
 * This Activity shows the new features of Tiles
 */
public class TileActivity extends Activity{
    /** The TextView with the details for editing the preferences. */
    private TextView lblChoosePreference;

    /** The Switch to edit the preferences. */
    private Switch swChoosePreference;

    /** The tile updated receiver */
    private TileUpdatedReceiver receiver;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tile);

        receiver = new TileUpdatedReceiver();

        lblChoosePreference = (TextView) findViewById(R.id.lbl_choose_preference);
        swChoosePreference = (Switch) findViewById(R.id.sw_choose_preference);

        setNotificationsEnabled(SharedPreferencesUtils.getNotificationsEnabled(this), false, false);

        swChoosePreference.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setNotificationsEnabled(b, true, false);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        registerReceiver(receiver, new IntentFilter(TileUpdatedReceiver.ACTION));
    }

    @Override
    public void onStop(){
        super.onStop();
        unregisterReceiver(receiver);
    }

    /**
     * Sets whether the notifications are enabled or not and edit UI elements of the activity.
     * @param notificationsEnabled Whether the notifications are enabled or not
     * @param fromSwitch Whether this method is called from the switch or not
     * @param fromTile Whether this method is called from the tile or not
     */
    private void setNotificationsEnabled(boolean notificationsEnabled, boolean fromSwitch, boolean fromTile){
        if(!fromTile)
            SharedPreferencesUtils.putNotificationsEnabled(this, notificationsEnabled);

        if(lblChoosePreference != null)
            lblChoosePreference.setText(notificationsEnabled ? R.string.disable_notifications : R.string.enable_notifications);

        if(!fromSwitch && swChoosePreference != null)
            swChoosePreference.setChecked(notificationsEnabled);
    }

    /**
     * The class that will receives updates of the Tile
     */
    public class TileUpdatedReceiver extends BroadcastReceiver {
        /** The action this receiver should listen for */
        public static final String ACTION = "net.gahfy.nsamples.NOTIFICATION_UPDATED";

        /** The name of the extra which contains whether notifications are enabled or not */
        public static final String NOTIFICATION_ENABLED_EXTRA_NAME = "notificationsEnabled";

        @Override
        public void onReceive(Context context, Intent intent){
            boolean notificationEnabled = intent.getBooleanExtra(NOTIFICATION_ENABLED_EXTRA_NAME, false);
            setNotificationsEnabled(notificationEnabled, false, true);
        }
    }
}
