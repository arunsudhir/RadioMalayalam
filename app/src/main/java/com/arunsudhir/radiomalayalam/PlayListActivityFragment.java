package com.arunsudhir.radiomalayalam;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.io.AsyncTaskPreAndPostExecutor;
import com.arunsudhir.radiomalayalam.io.PlaylistReaderAsyncTask;
import com.arunsudhir.radiomalayalam.playlist.PlaylistItem;
import com.arunsudhir.radiomalayalam.song.SongItem;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayListActivityFragment extends ListFragment {

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    public PlayListActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PlaylistReaderAsyncTask(new PlaylistProgressExecutor()).execute();
        }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        // mCallbacks = sDummyCallbacks;
        PlaylistItem selectedItem = (PlaylistItem) listView.getItemAtPosition(position);
        Intent songListIntent = new Intent(this.getActivity(), SongListActivity.class);
        String encodedString = selectedItem.getPlaylistUrl();
        encodedString = encodedString.replace(" ","%20");
        songListIntent.putExtra("url", encodedString);
        startActivity(songListIntent);
      }

    public class PlaylistProgressExecutor implements AsyncTaskPreAndPostExecutor<PlaylistItem>
    {
        ProgressDialog pDialog;

        @Override
        public void PreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Playlists ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        public void PostExecute(ArrayList<PlaylistItem> result) {
            pDialog.dismiss();
            ListAdapter adapter = new PlaylistListAdapter(getActivity(), result);
            setListAdapter(adapter);

            //serialize the currentPlaylist onto file
            FileOutputStream fos;
            ObjectOutputStream oos;

            try {
                fos = getActivity().openFileOutput(CommunicationConstants.CurrentPlaylist, Context.MODE_PRIVATE);
                oos = new ObjectOutputStream(fos);
                oos.writeObject(result);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
