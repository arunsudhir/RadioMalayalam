package com.arunsudhir.radiomalayalam;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.arunsudhir.radiomalayalam.exception.GlobalExceptionHandler;
import com.arunsudhir.radiomalayalam.io.PlaylistProgressExecutor;
import com.arunsudhir.radiomalayalam.io.PlaylistReaderAsyncTask;

public class PlayListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new GlobalExceptionHandler(Thread.getDefaultUncaughtExceptionHandler(), this));
        setContentView(R.layout.activity_play_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new PlaylistReaderAsyncTask(new PlaylistProgressExecutor(this)).execute();
    }
}
