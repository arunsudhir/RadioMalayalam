package com.arunsudhir.radiomalayalam;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arunsudhir.radiomalayalam.io.PlaylistProgressExecutor;
import com.arunsudhir.radiomalayalam.io.PlaylistReaderAsyncTask;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlayListActivityFragment extends Fragment {

    private View _rootView;
    public PlayListActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PlaylistReaderAsyncTask(new PlaylistProgressExecutor(getActivity())).execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _rootView = inflater.inflate(R.layout.fragment_play_list, container, false);
        AdView mAdView = (AdView) _rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return _rootView;
    }



}
