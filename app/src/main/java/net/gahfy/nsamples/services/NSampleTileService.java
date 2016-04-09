package net.gahfy.nsamples.services;

import android.content.Intent;
import android.graphics.drawable.Icon;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import net.gahfy.nsamples.R;
import net.gahfy.nsamples.activities.TileActivity;
import net.gahfy.nsamples.utils.SharedPreferencesUtils;

/** This service allows us to manage tile of settings */
public class NSampleTileService extends TileService {
    @Override
    public void onStartListening() {
        super.onStartListening();
        this.setCurrentState(SharedPreferencesUtils.getNotificationsEnabled(this) ? Tile.STATE_ACTIVE : Tile.STATE_INACTIVE);
    }

    @Override
    public void onClick() {
        super.onClick();
        Tile tile = getQsTile();
        switch (tile.getState()) {
            case Tile.STATE_ACTIVE:
                setCurrentState(Tile.STATE_INACTIVE);
                break;
            case Tile.STATE_INACTIVE:
                setCurrentState(Tile.STATE_ACTIVE);
                break;
            case Tile.STATE_UNAVAILABLE:
                break;
        }
    }

    /**
     * Performs all operations needed when the state of a tile should be changed.
     * @param state The new state of the tile
     */
    private void setCurrentState(int state){
        Tile tile = getQsTile();
        tile.setState(state);

        switch (state) {
            case Tile.STATE_ACTIVE:
                tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_notifications_active_white_24dp));
                tile.setLabel(getString(R.string.tile_label_enabled));
                break;
            case Tile.STATE_INACTIVE:
                tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_notifications_off_white_24dp));
                tile.setLabel(getString(R.string.tile_label_disabled));
                break;
            case Tile.STATE_UNAVAILABLE:
                tile.setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.ic_notifications_none_white_24dp));
                tile.setLabel(getString(R.string.tile_label));
                break;
        }
        tile.updateTile();

        // Updating Shared Preferences
        SharedPreferencesUtils.putNotificationsEnabled(this, state == Tile.STATE_ACTIVE);

        // Sending Broadcast
        Intent broadcastIntent = new Intent(TileActivity.TileUpdatedReceiver.ACTION);
        broadcastIntent.putExtra(TileActivity.TileUpdatedReceiver.NOTIFICATION_ENABLED_EXTRA_NAME, state == Tile.STATE_ACTIVE);
        sendBroadcast(broadcastIntent);
    }
}