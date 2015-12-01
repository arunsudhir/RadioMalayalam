package com.arunsudhir.radiomalayalam.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.logging.Logger;
import com.arunsudhir.radiomalayalam.song.SongItem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Arun on 9/2/2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnInfoListener,
        MediaPlayer.OnBufferingUpdateListener {

    private static final Logger LOG = new Logger(PlayerService.class);

    private MediaPlayer _mediaPlayer = new MediaPlayer();
    private String _songUrl;
    private String _currentSongId;
    private String serviceCommand;
    private int _currentSongPostion = -1;
    private ArrayList<SongItem> _currPlaylist = new ArrayList<>();

    @Override
    public void onCreate() {
        NotificationManager mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Display a notification about us starting.  We put an icon in the status bar.
        //mNM.notify("sdsds",12,new Notification(new Parcel()));
        //showNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        serviceCommand = intent.getStringExtra("serviceCommand");
        if(serviceCommand.equals("play")){
            _songUrl = intent.getData().toString();
            _currentSongId = intent.getStringExtra("currentSongId");
            serviceCommand = intent.getStringExtra("serviceCommand");
            if (_mediaPlayer != null) {
                if (_mediaPlayer.isPlaying()) {
                    _mediaPlayer.stop();
                    _mediaPlayer.reset();
                }
            } else {
                _mediaPlayer = new MediaPlayer();
            }
            try {
                _mediaPlayer.setDataSource(_songUrl);
                _mediaPlayer.prepare();
                _mediaPlayer.setOnCompletionListener(this);
                _mediaPlayer.start();
            } catch (IOException ex) {
                // pass an intent saying that the song wasnt found
            }

            // now get the current playlist
            ArrayList<SongItem> currentPlaylist = null;
            try {
                FileInputStream fis = openFileInput(CommunicationConstants.CurrentPlaylist);
                ObjectInputStream ois = new ObjectInputStream(fis);
                _currPlaylist = (ArrayList<SongItem>) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < _currPlaylist.size(); i++) {
                if (_currPlaylist.get(i).getId().equals(_currentSongId)) {
                    _currentSongPostion = i;
                    break;
                }
            }
        }
        else if(serviceCommand.equals("toggle"))
        {
            if (_mediaPlayer != null) {
                if (_mediaPlayer.isPlaying()) {
                    _mediaPlayer.pause();
                    //_mediaPlayer.reset();
                }
                else if(!_mediaPlayer.isPlaying())
                {
                    _mediaPlayer.start();
                }
            }
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        _currentSongPostion = (_currentSongPostion + 1) % (_currPlaylist.size());
        String path = _currPlaylist.get(_currentSongPostion).songPath;
        URI songUri = null;
        try {
            songUri = new URI("http", CommunicationConstants.songsHost, CommunicationConstants.songsRelativeUrl + path, null, null);
            LOG.info("Playing song: %s", songUri.toString());
            _mediaPlayer.stop();
            _mediaPlayer.reset();
            _mediaPlayer.setDataSource(songUri.toString());
            _mediaPlayer.prepare();
            _mediaPlayer.start();

        } catch (Exception e) {
            LOG.error(e, "Failed to play song: %s", songUri);
        }
    }
}
