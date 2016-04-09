package net.gahfy.nsamples.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import net.gahfy.nsamples.R;
import net.gahfy.nsamples.utils.SharedPreferencesUtils;

/**
 * This Activity shows the new features of Tiles
 */
public class TileActivity extends AppCompatActivity{
    /** The TextView with the details for editing the preferences. */
    private TextView lblChoosePreference;

    /** The Switch to edit the preferences. */
    private Switch swChoosePreference;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_tile);

        lblChoosePreference = (TextView) findViewById(R.id.lbl_choose_preference);
        swChoosePreference = (Switch) findViewById(R.id.sw_choose_preference);

        setNotificationsEnabled(SharedPreferencesUtils.getNotificationsEnabled(this), false);

        swChoosePreference.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setNotificationsEnabled(b, true);
            }
        });
    }

    /**
     * Sets whether the notifications are enabled or not and edit UI elements of the activity.
     * @param notificationsEnabled Whether the notifications are enabled or not
     * @param fromSwitch Whether this method is called from the switch or not
     */
    private void setNotificationsEnabled(boolean notificationsEnabled, boolean fromSwitch){
        SharedPreferencesUtils.putNotificationsEnabled(this, notificationsEnabled);
        lblChoosePreference.setText(notificationsEnabled ? R.string.disable_notifications : R.string.enable_notifications);
        if(!fromSwitch)
            swChoosePreference.setChecked(notificationsEnabled);
    }
}
