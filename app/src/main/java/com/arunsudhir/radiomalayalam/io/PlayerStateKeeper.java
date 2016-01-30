package com.arunsudhir.radiomalayalam.io;

import android.content.Context;

import com.arunsudhir.radiomalayalam.communication.CommunicationConstants;
import com.arunsudhir.radiomalayalam.song.SongItem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arun on 1/22/2016.
 */
public class PlayerStateKeeper {

    public static void WriteCurrentPlaylist(Context context, List<SongItem> curPlaylist)
    {
        //serialize the currentPlaylist onto file
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = context.openFileOutput(CommunicationConstants.CurrentPlaylist, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(curPlaylist);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<SongItem> ReadCurrentPlaylist(Context context)
    {
        ArrayList<SongItem> currPlaylist = null;
        try {
            FileInputStream fis = context.openFileInput(CommunicationConstants.CurrentPlaylist);
            ObjectInputStream ois = new ObjectInputStream(fis);
            currPlaylist = (ArrayList<SongItem>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currPlaylist;
    }

    public static void WriteCurrentSongIndex(Context context, int songIndex)
    {
        //serialize the currentPlaylist onto file
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = context.openFileOutput(CommunicationConstants.CurrentSongIndex, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeInt(songIndex);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int ReadCurrentSongIndex(Context context)
    {
        int currSongIndex = -1;
        try {
            FileInputStream fis = context.openFileInput(CommunicationConstants.CurrentSongIndex);
            ObjectInputStream ois = new ObjectInputStream(fis);
            currSongIndex = ois.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currSongIndex;
    }

}
