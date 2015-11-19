package com.arunsudhir.radiomalayalam.io;

import android.os.AsyncTask;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.communication.DownloadImageTask;
import com.arunsudhir.radiomalayalam.playlist.PlaylistItem;
import com.arunsudhir.radiomalayalam.song.SongContent;
import com.arunsudhir.radiomalayalam.song.SongItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Arun on 10/24/2015.
 */
public class PlaylistReaderAsyncTask extends AsyncTask<String, Integer, ArrayList<PlaylistItem>>
    {
        ArrayList<PlaylistItem> plList = new ArrayList<>();
        String url = "http://www.mywimbo.com/MalRadio/getPublicPlaylists.php";
        String PLAYLISTS = "playlists";
        String PLAYLIST = "playlist";
        AsyncTaskPreAndPostExecutor PreExecutor;

        public PlaylistReaderAsyncTask(AsyncTaskPreAndPostExecutor preExecutor)
        {
            PreExecutor = preExecutor;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            PreExecutor.PreExecute();
        }

        @Override
        protected ArrayList<PlaylistItem> doInBackground(String... params) {

            JsonReader jParser = new JsonReader();
            JSONObject json = jParser.getJSONData(url);
            try{
                JSONArray playlists = json.getJSONArray(PLAYLISTS);
                for(int i=0;i<playlists.length();i++){
                    JSONObject playlist = playlists.getJSONObject(i);
                    JSONObject plObject = playlist.getJSONObject(PLAYLIST);
                    PlaylistItem currPlaylist = new PlaylistItem();
                    currPlaylist.playlistName = plObject.getString("Name");
                    currPlaylist.url = plObject.getString("URL");
                    currPlaylist.heroImageUrl = CommunicationConstants.imagesBaseUrl+plObject.getString("heroImage");
                    plList.add(currPlaylist);
                   // SongContent.ITEM_MAP.put(currSong.songName, currSong);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return plList;
        }
        @Override
        protected void onPostExecute(ArrayList<PlaylistItem> result) {
            super.onPostExecute(result);
            PreExecutor.PostExecute(result);
        }
}
