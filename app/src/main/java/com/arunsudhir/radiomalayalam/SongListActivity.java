package com.arunsudhir.radiomalayalam;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.service.PlayerService;
import com.arunsudhir.radiomalayalam.song.SongItem;

import java.net.URI;


/**
 * An activity representing a list of Songs. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SongDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SongListFragment} and the item details
 * (if present) is a {@link SongDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link SongListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class SongListActivity extends FragmentActivity
        implements SongListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        if (findViewById(R.id.song_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((SongListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.song_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link SongListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(SongItem songItem) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
           /* Bundle arguments = new Bundle();
            arguments.putString(SongDetailFragment.ARG_ITEM_ID, id);
            SongDetailFragment fragment = new SongDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.song_detail_container, fragment)
                    .commit();*/

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            /*Intent detailIntent = new Intent(this, SongDetailActivity.class);
            detailIntent.putExtra(SongDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);*/
        }
        Intent serviceIntent = new Intent(this, PlayerService.class);
        //serviceIntent.setComponent(new ComponentName("com.arunsudhir.radiomalayalam.service", "com.arunsudhir.radiomalayalam.service.PlayerService"));
        try {
            URI songUri = new URI("http", CommunicationConstants.songsHost, CommunicationConstants.songsRelativeUrl + songItem.songPath);
            Log.i("songBath",songUri.toString());
            serviceIntent.setData(Uri.parse(songUri.toString()));
            serviceIntent.putExtra("currentSongId", songItem.getId());
            startService(serviceIntent);
        }
        catch(Exception e)
        {

        }
    }

}
