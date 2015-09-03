package com.arunsudhir.radiomalayalam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.arunsudhir.radiomalayalam.io.JsonReader;
import com.arunsudhir.radiomalayalam.song.SongContent;
import com.arunsudhir.radiomalayalam.song.SongItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A list fragment representing a list of Songs. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link SongDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class SongListFragment extends ListFragment {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sDummyCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public SongListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: replace with a real list adapter.
        /*setListAdapter(new ArrayAdapter<SongItem>(
                getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                SongContent.ITEMS));*/
        new JsonReaderAsyncTask().execute();
       /* setListAdapter(new SongListAdapter(
                getActivity(),
                SongContent.ITEMS));*/


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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
        mCallbacks.onItemSelected(SongContent.ITEMS.get(position).id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
    }

    public class JsonReaderAsyncTask extends AsyncTask<String, Integer, ArrayList<SongItem>> {

        ArrayList<SongItem> songsList = new ArrayList<>();
        String url = "http://www.mywimbo.com/MalRadio/getTopListenedSongs.php?year1=2010&genre1=fast&language=malayalam";
        String SONGS = "songs";
        String SONG = "song";

        private ProgressDialog pDialog;

        protected void onPreExecute() {

            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected ArrayList<SongItem> doInBackground(String... params) {

            JsonReader jParser = new JsonReader();
            JSONObject json = jParser.getJSONData(url);
            try{
                JSONArray songs = json.getJSONArray(SONGS);
                for(int i=0;i<songs.length();i++){
                    JSONObject song = songs.getJSONObject(i);
                    JSONObject songObject = song.getJSONObject(SONG);
                    SongItem currSong = new SongItem();
                    currSong.songName = songObject.getString("SongName");
                    currSong.songPath = songObject.getString("SongPath");
                    currSong.id = songObject.getString("ID");
                    currSong.album = songObject.getString("Album");
                    currSong.singer1 = songObject.getString("Singer1");
                    currSong.singer2 = songObject.getString("Singer2");
                    currSong.music = songObject.getString("Music");
                    songsList.add(currSong);
                    SongContent.ITEM_MAP.put(currSong.songName, currSong);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return songsList;
        }
        @Override
        protected void onPostExecute(ArrayList<SongItem> result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            ListAdapter adapter = new SongListAdapter(getActivity(), result);
            setListAdapter(adapter);

        }
    }
}
