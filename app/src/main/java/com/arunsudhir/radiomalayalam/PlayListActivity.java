package com.arunsudhir.radiomalayalam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.arunsudhir.radiomalayalam.io.PlaylistProgressExecutor;
import com.arunsudhir.radiomalayalam.io.PlaylistReaderAsyncTask;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class PlayListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new PlaylistReaderAsyncTask(new PlaylistProgressExecutor(this)).execute();
        setContentView(R.layout.activity_no_connection);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
