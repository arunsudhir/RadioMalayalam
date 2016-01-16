package com.arunsudhir.radiomalayalam;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.logging.Logger;
import com.arunsudhir.radiomalayalam.service.PlayerService;
import com.arunsudhir.radiomalayalam.song.SongItem;

import java.net.URI;

/**
 * An activity representing a single Song detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SongListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link SongDetailFragment}.
 */
public class SongDetailActivity extends FragmentActivity {

    private static final Logger LOG = new Logger(SongDetailActivity.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_detail);

        // Show the Up button in the action bar.
       //getActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(SongDetailFragment.ARG_SONG_NAME,
                    getIntent().getStringExtra(SongDetailFragment.ARG_SONG_NAME));
            SongItem songItem = getIntent().getParcelableExtra("songItem");
            SongDetailFragment fragment = new SongDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .add(R.id.song_detail_container, fragment)
                    .commit();

            ImageButton fabButton = (ImageButton) findViewById(R.id.play_button);
            final Activity thisActivity = this;
            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent serviceIntent = new Intent(thisActivity, PlayerService.class);
                    try {
                        LOG.info("Toggling play/pause");
                        serviceIntent.putExtra("serviceCommand", "toggle");
                        startService(serviceIntent);
                    } catch (Exception e) {
                        LOG.error(e, "Failed to toggle state");
                    }
                }
            });

            Intent serviceIntent = new Intent(this, PlayerService.class);
            //serviceIntent.setComponent(new ComponentName("com.arunsudhir.radiomalayalam.service", "com.arunsudhir.radiomalayalam.service.PlayerService"));
            try {
                URI songUri = new URI("http", CommunicationConstants.songsHost, CommunicationConstants.songsRelativeUrl + songItem.songPath, null, null);
                LOG.info("Playing song: %s", songUri.toString());
                serviceIntent.setData(Uri.parse(songUri.toString()));
                serviceIntent.putExtra("currentSongId", songItem.getId());
                serviceIntent.putExtra("serviceCommand", "play");
                startService(serviceIntent);
            } catch (Exception e) {
                LOG.error(e, "Failed to play song: %s (@ %s)", songItem.getSongName(), songItem.getSongPath());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, SongListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ServiceReciever extends BroadcastReceiver
    {

        @Override
        public void onReceive(Context context, Intent intent) {
            SongItem songItem = getIntent().getParcelableExtra("currentSong");
        }
    }
}
