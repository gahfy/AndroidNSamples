package net.gahfy.nsamples.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.gahfy.nsamples.R;

/**
 * This activity is the first activity of the application.
 * Its main role is to redirect the user to the other activities.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btGoToNotifications = (Button) findViewById(R.id.bt_go_to_notifications);
        Button btGoToTiles = (Button) findViewById(R.id.bt_go_to_tiles);

        if(btGoToNotifications != null)
            btGoToNotifications.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, NotificationsActivity.class);
                    startActivity(intent);
                }
            });


        if(btGoToTiles != null)
            btGoToTiles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, TileActivity.class);
                    startActivity(intent);
                }
            });
    }
}
