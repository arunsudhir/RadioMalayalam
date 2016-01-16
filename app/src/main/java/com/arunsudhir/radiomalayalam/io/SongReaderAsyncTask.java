package com.arunsudhir.radiomalayalam.io;

import android.os.AsyncTask;

import com.arunsudhir.radiomalayalam.logging.Logger;
import com.arunsudhir.radiomalayalam.song.SongContent;
import com.arunsudhir.radiomalayalam.song.SongItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 9/22/2015.
 */
public class SongReaderAsyncTask extends AsyncTask<String, Integer, List<SongItem>> {

    private static final Logger LOG = new Logger(SongReaderAsyncTask.class);
    private static final String SONGS = "songs";
    private static final String SONG = "song";

    private final AsyncTaskPreAndPostExecutor<SongItem> preExecutor;
    private final String url;

    public SongReaderAsyncTask(AsyncTaskPreAndPostExecutor<SongItem> preExecutor, String songsUrl) {
        this.preExecutor = preExecutor;
        url = songsUrl;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        preExecutor.PreExecute();
    }

    @Override
    protected List<SongItem> doInBackground(String... params) {
        try {
            JSONArray songs = new JsonReader(preExecutor).getRemoteJsonData(url).getJSONArray(SONGS);
            List<SongItem> result = new ArrayList<>(songs.length());
            for (int i = 0; i < songs.length(); i++) {
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
                result.add(currSong);
                SongContent.ITEM_MAP.put(currSong.songName, currSong);
            }
            return result;
        } catch (JSONException e) {
            LOG.error(e, "Failed to parse songs from url '%s'", url);
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(List<SongItem> result) {
        super.onPostExecute(result);
        preExecutor.PostExecute(result);
    }
}
