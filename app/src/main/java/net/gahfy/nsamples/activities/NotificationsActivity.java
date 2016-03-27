package net.gahfy.nsamples.activities;

import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        final EditText txtLaunchToast = (EditText) findViewById(R.id.txt_launch_toast);
        Button btLaunchToast = (Button) findViewById(R.id.bt_launch_toast);

        btLaunchToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIUtils.sendShortToast(NotificationsActivity.this, txtLaunchToast.getText().toString());
            }
        });

    }
}
