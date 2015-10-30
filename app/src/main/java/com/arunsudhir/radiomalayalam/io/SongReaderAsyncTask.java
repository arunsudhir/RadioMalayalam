package com.arunsudhir.radiomalayalam.io;

import android.os.AsyncTask;

import com.arunsudhir.radiomalayalam.song.SongContent;
import com.arunsudhir.radiomalayalam.song.SongItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Arun on 9/22/2015.
 */
public class SongReaderAsyncTask extends AsyncTask<String, Integer, ArrayList<SongItem>> {

    ArrayList<SongItem> songsList = new ArrayList<>();
    String url = "http://www.mywimbo.com/MalRadio/getTopListenedSongs.php?year1=2015&language=malayalam";
    String SONGS = "songs";
    String SONG = "song";
    AsyncTaskPreAndPostExecutor PreExecutor;

    public SongReaderAsyncTask(AsyncTaskPreAndPostExecutor preExecutor, String songsUrl)
    {
        PreExecutor = preExecutor;
        url = songsUrl;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        PreExecutor.PreExecute();
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
        PreExecutor.PostExecute(result);
    }
}
