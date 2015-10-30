package com.arunsudhir.radiomalayalam.song;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunsudhir.radiomalayalam.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaylistCardActivityFragment extends Fragment {

    public PlaylistCardActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_playlist_card, container, false);
    }
}
