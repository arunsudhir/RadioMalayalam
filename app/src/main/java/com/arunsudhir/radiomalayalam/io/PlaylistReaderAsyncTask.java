package com.arunsudhir.radiomalayalam.io;

import android.os.AsyncTask;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.logging.Logger;
import com.arunsudhir.radiomalayalam.playlist.PlaylistItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 10/24/2015.
 */
public class PlaylistReaderAsyncTask extends AsyncTask<String, Integer, List<PlaylistItem>> {
    private static final Logger LOG = new Logger(PlaylistReaderAsyncTask.class);
    // TODO: This should come from configs
    private static final String URL = "http://www.mywimbo.com/MalRadio/getPublicPlaylists.php";
    // TODO: This should be JSON annotated on the object that's being serialized.
    private static final String PLAYLISTS = "playlists";
    // TODO: This should be JSON annotated on the object that's being serialized.
    private static final String PLAYLIST = "playlist";

    private final AsyncTaskPreAndPostExecutor preExecutor;

    public PlaylistReaderAsyncTask(AsyncTaskPreAndPostExecutor preExecutor) {
        this.preExecutor = preExecutor;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        preExecutor.PreExecute();
    }

    @Override
    protected List<PlaylistItem> doInBackground(String... params) {
        JSONObject json = new JsonReader(preExecutor).getRemoteJsonData(URL);
        if(json !=null) {
            try {
                JSONArray playlists = json.getJSONArray(PLAYLISTS);
                List<PlaylistItem> playlistItems = new ArrayList<>(playlists.length());
                for (int i = 0; i < playlists.length(); i++) {
                    JSONObject playlist = playlists.getJSONObject(i);
                    JSONObject plObject = playlist.getJSONObject(PLAYLIST);
                    PlaylistItem currPlaylist = new PlaylistItem();
                    currPlaylist.playlistName = plObject.getString("Name");
                    currPlaylist.url = plObject.getString("URL");
                    currPlaylist.heroImageUrl = CommunicationConstants.imagesBaseUrl + plObject.getString("heroImage");
                    playlistItems.add(currPlaylist);
                    // SongContent.ITEM_MAP.put(currSong.songName, currSong);
                }
                return playlistItems;
            } catch (JSONException e) {
                LOG.error(e, "Failed to retrieve playlist");
            }
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(List<PlaylistItem> result) {
        super.onPostExecute(result);
        preExecutor.PostExecute(result);
    }
}
