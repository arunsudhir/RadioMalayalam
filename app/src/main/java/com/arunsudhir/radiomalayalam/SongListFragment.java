package com.arunsudhir.radiomalayalam;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.io.AsyncTaskPreAndPostExecutor;
import com.arunsudhir.radiomalayalam.io.JsonReaderAsyncTask;
import com.arunsudhir.radiomalayalam.song.SongItem;


import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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

   // final Context context =  getActivity();
    // static AlertDialog.Builder builder1;
    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks;

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
        public void onItemSelected(SongItem item);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    /*private Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
            Intent serviceIntent = new Intent(PlayerService.class.getName());
            serviceIntent.putExtra("songUrl", "http://www.mywimbo.com/MalRadio/2015/Mili/Kanmaniye.mp3");
            getActivity().startService(serviceIntent);
        }
    };*/

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

        /*AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Write your message here.");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });*/
        new JsonReaderAsyncTask(new ShowProgressDialogExecutor()).execute();
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
       // mCallbacks = sDummyCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        // Notify the active callbacks interface (the activity, if the
        // fragment is attached to one) that an item has been selected.
       // mCallbacks = sDummyCallbacks;
       SongItem selectedItem = (SongItem) listView.getItemAtPosition(position);
        mCallbacks.onItemSelected(selectedItem); //SongContent.ITEMS.get(position).id
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

    public class ShowProgressDialogExecutor implements AsyncTaskPreAndPostExecutor
    {
        ProgressDialog  pDialog;

        @Override
        public void PreExecute() {
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        public void PostExecute(ArrayList<SongItem> result) {
            pDialog.dismiss();
            ListAdapter adapter = new SongListAdapter(getActivity(), result);
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
