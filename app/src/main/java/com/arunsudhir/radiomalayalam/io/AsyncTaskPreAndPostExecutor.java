package com.arunsudhir.radiomalayalam.io;

import com.arunsudhir.radiomalayalam.song.SongItem;

import java.util.ArrayList;

/**
 * Created by Arun on 9/22/2015.
 */
public interface AsyncTaskPreAndPostExecutor<T> {
    public void PreExecute();

    public void PostExecute(ArrayList<T> result);
}
